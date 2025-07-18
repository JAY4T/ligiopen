package com.jabulani.ligiopen.service.club;

import com.google.gson.Gson;
import com.jabulani.ligiopen.config.Constants;
import com.jabulani.ligiopen.config.DiskMultipartFile;
import com.jabulani.ligiopen.dao.club.ClubDao;
import com.jabulani.ligiopen.dao.club.PlayerDao;
import com.jabulani.ligiopen.dao.league.LeagueDao;
import com.jabulani.ligiopen.dao.match.MatchDao;
import com.jabulani.ligiopen.dao.user.UserAccountDao;
import com.jabulani.ligiopen.model.aws.File;
import com.jabulani.ligiopen.model.club.entity.*;
import com.jabulani.ligiopen.model.dto.*;
import com.jabulani.ligiopen.model.dto.mapper.ClubMapper;
import com.jabulani.ligiopen.model.dto.mapper.LeagueMapper;
import com.jabulani.ligiopen.model.dto.mapper.PlayerMapper;
import com.jabulani.ligiopen.model.match.PlayerState;
import com.jabulani.ligiopen.model.match.entity.MatchLocation;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import com.jabulani.ligiopen.service.aws.AwsService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl implements ClubService{

    private final ClubDao clubDao;
    private final LeagueDao leagueDao;
    private final MatchDao matchDao;
    private final PlayerDao playerDao;
    private final AwsService awsService;
    private final ClubMapper clubMapper;
    private final PlayerMapper playerMapper;
    private final UserAccountDao userAccountDao;
    private final LeagueMapper leagueMapper;
    @Autowired
    public ClubServiceImpl(
            ClubDao clubDao,
            LeagueDao leagueDao,
            PlayerDao playerDao,
            MatchDao matchDao,
            AwsService awsService,
            ClubMapper clubMapper,
            PlayerMapper playerMapper,
            UserAccountDao userAccountDao,
            LeagueMapper leagueMapper
    ) {
        this.clubDao = clubDao;
        this.leagueDao = leagueDao;
        this.playerDao = playerDao;
        this.matchDao = matchDao;
        this.awsService = awsService;
        this.clubMapper = clubMapper;
        this.playerMapper = playerMapper;
        this.userAccountDao = userAccountDao;
        this.leagueMapper = leagueMapper;
    }
    @Transactional
    @Override
    public ClubDetailsDto addClub(AddClubDto addClubDto, MultipartFile logo) throws IOException {
        String fileName = null;

        if(logo != null) {
            fileName = awsService.uploadFile(Constants.BUCKET_NAME, logo);
        }

        League league = leagueDao.getLeagueById(addClubDto.getDivisionId());

        MatchLocation matchLocation = matchDao.getMatchLocationById(addClubDto.getHomeId());

        Club club = Club.builder()
                .name(addClubDto.getName())
                .clubAbbreviation(addClubDto.getClubAbbreviation())
                .description(addClubDto.getDescription())
                .country(addClubDto.getCountry())
                .county(addClubDto.getCounty())
                .town(addClubDto.getTown())
                .startedOn(addClubDto.getStartedOn())
                .createdAt(LocalDateTime.now())
                .archived(false)
                .playerClubs(new ArrayList<>())
                .files(new ArrayList<>())
                .clubMainPhoto(null)
                .league(league)
                .home(matchLocation)
                .managers(new ArrayList<>())
                .clubStatus(ClubStatus.PENDING)
                .build();

        File file = File.builder()
                .name(fileName)
                .clubAsLogo(club)
                .build();

        club.setClubLogo(file);

        return clubMapper.clubDetailsDto(clubDao.addClub(club));
    }

    @Transactional
    @Override
    public Map<String, Object> addClubsFromAirtable() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> airtableClubs = getAirtableClubs();
        List<AirtableClubDto> airtableClubDtos = new ArrayList<>();

        // Convert Airtable records to AirtableClubDto objects
        List<Map<String, Object>> records = (List<Map<String, Object>>) airtableClubs.get("records");
        for (Map<String, Object> record : records) {
            Map<String, Object> fields = (Map<String, Object>) record.get("fields");

            // Get team logo URL (taking the first image if multiple exist)
            String teamLogoUrl = "";
            if (fields.containsKey("Team Logo")) {
                List<Map<String, Object>> teamLogos = (List<Map<String, Object>>) fields.get("Team Logo");
                if (!teamLogos.isEmpty()) {
                    teamLogoUrl = (String) teamLogos.get(0).get("url");
                }
            }

            AirtableClubDto dto = AirtableClubDto.builder()
                    .teamName((String) fields.get("Team Name"))
                    .teamLogoUrl(teamLogoUrl)
                    .homeStadium((String) fields.get("Home Stadium"))
                    .leagueName((String) fields.get("League"))
                    .build();

            airtableClubDtos.add(dto);
        }

        // Process each club
        for (AirtableClubDto airtableClubDto : airtableClubDtos) {
            Boolean leagueExists = leagueDao.leagueExists(airtableClubDto.getLeagueName());
            Boolean stadiumExists = matchDao.matchLocationExists(airtableClubDto.getHomeStadium());

            League league = new League();
            MatchLocation stadium = new MatchLocation();

            if (!leagueExists) {
                league = League.builder()
                        .name(airtableClubDto.getLeagueName())
                        .clubs(new ArrayList<>())
                        .build();
                leagueDao.createLeague(league);
            } else {
                league = leagueDao.getLeagueByName(airtableClubDto.getLeagueName());
            }

            if (!stadiumExists) {
                stadium = MatchLocation.builder()
                        .venueName(airtableClubDto.getHomeStadium())
                        .build();
                matchDao.createMatchLocation(stadium);
            } else {
                stadium = matchDao.getMatchLocationByName(airtableClubDto.getHomeStadium());
            }

            // Download and upload team logo
            File clubLogoFile = null;
            if (airtableClubDto.getTeamLogoUrl() != null && !airtableClubDto.getTeamLogoUrl().isEmpty()) {
                try {
                    // Download to local disk
                    URL url = new URL(airtableClubDto.getTeamLogoUrl());
                    String originalFileName = airtableClubDto.getTeamLogoUrl()
                            .substring(airtableClubDto.getTeamLogoUrl().lastIndexOf('/') + 1);
                    java.io.File directory = new java.io.File("/home/mbogo/Desktop/ligiopen/team-logos/");
                    if (!directory.exists()) directory.mkdirs();
                    java.io.File downloadedFile = new java.io.File(directory, originalFileName);
                    try (InputStream in = url.openStream();
                         FileOutputStream out = new FileOutputStream(downloadedFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }

                    Path path = downloadedFile.toPath();
                    String contentType = Files.probeContentType(path);

// wrap it
                    MultipartFile multipart = new DiskMultipartFile(downloadedFile,
                            contentType != null
                                    ? contentType
                                    : "application/octet-stream");

// now upload
                    String awsFileName = awsService.uploadFile(Constants.BUCKET_NAME, multipart);
                    String awsFileUrl  = awsService.getFileUrl(Constants.BUCKET_NAME, awsFileName);

// build your JPA File entity
                    clubLogoFile = File.builder()
                            .name(awsFileName)
                            .build();

// then delete downloadedFile as before
                    downloadedFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            Club club = Club.builder()
                    .name(airtableClubDto.getTeamName())
                    .clubAbbreviation(airtableClubDto.getTeamName().substring(0, 3))
                    .home(stadium)
                    .description(airtableClubDto.getTeamName())
                    .country("Kenya")
                    .startedOn(LocalDate.now())
                    .createdAt(LocalDateTime.now())
                    .archived(false)
                    .managers(new ArrayList<>())
                    .coaches(new ArrayList<>())
                    .files(new ArrayList<>())
                    .clubLogo(clubLogoFile)
                    .league(league)
                    .home(stadium)
                    .build();

            clubDao.addClub(club);

        }

        resultMap.put("SUCCESS", "true");

        return resultMap;
    }

    public Map<String, Object> getAirtableClubs() throws Exception {
        String token = "patkXaKWLZw016V0B.31eaaad7cc610619c6a6425b515f845f5b4d0859412c10729d6566b4f19f372d";
        String baseId = "appdP104tjv2VMY6I";
        String tableName = "Team Profiles";

        String url = String.format("https://api.airtable.com/v0/%s/%s",
                baseId,
                tableName.replace(" ", "%20"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception(String.format(
                    "Failed to fetch Airtable data. Status: %d, Response: %s",
                    response.statusCode(),
                    response.body()
            ));
        }

        return new Gson().fromJson(response.body(), Map.class);
    }

    @Transactional
    @Override
    public ClubDetailsDto updateClubDetails(UpdateClubDto updateClubDto) {
        Club club = clubDao.getClubById(updateClubDto.getClubId());

        if(updateClubDto.getHomeId() != null) {
            MatchLocation matchLocation = matchDao.getMatchLocationById(updateClubDto.getHomeId());
            if(club.getHome() != matchLocation) {
                club.setHome(matchLocation);
            }
        }

        if(updateClubDto.getDivisionId() != null) {
            League league = leagueDao.getLeagueById(updateClubDto.getDivisionId());
            if(club.getLeague() != league) {
                club.setLeague(league);
            }
        }


        if(!Objects.equals(club.getName(), updateClubDto.getName())) {
            club.setName(updateClubDto.getName());
        }

        if(!Objects.equals(club.getDescription(), updateClubDto.getDescription())) {
            club.setDescription(updateClubDto.getDescription());
        }

        if(!Objects.equals(club.getCountry(), updateClubDto.getCountry())) {
            club.setCountry(updateClubDto.getCountry());
        }

        if(!Objects.equals(club.getCounty(), updateClubDto.getCounty())) {
            club.setCounty(updateClubDto.getCounty());
        }

        if(!Objects.equals(club.getTown(), updateClubDto.getTown())) {
            club.setTown(updateClubDto.getTown());
        }

        if(!Objects.equals(club.getClubAbbreviation(), updateClubDto.getClubAbbreviation())) {
            club.setClubAbbreviation(updateClubDto.getClubAbbreviation());
        }

        return clubMapper.clubDetailsDto(clubDao.updateClub(club));
    }
    @Transactional
    @Override
    public ClubDetailsDto updateClubLogo(Integer clubId, MultipartFile logo) throws IOException {
        Club club = clubDao.getClubById(clubId);

        File clubLogo = club.getClubLogo();

        if(logo != null) {

            if(club.getClubLogo() != null) {
                awsService.deleteFile(Constants.BUCKET_NAME, clubLogo.getName());
            }

            clubLogo.setName(awsService.uploadFile(Constants.BUCKET_NAME, logo));
        }

        return clubMapper.clubDetailsDto(clubDao.updateClub(club));
    }
    @Transactional
    @Override
    public ClubDetailsDto setClubMainPhoto(Integer clubId, MultipartFile photo) throws IOException {

        String fileName = null;

        if(photo != null) {
            fileName = awsService.uploadFile(Constants.BUCKET_NAME, photo);
        }


        Club club = clubDao.getClubById(clubId);

        if(club.getClubMainPhoto() == null) {
            File file = File.builder()
                    .name(fileName)
                    .clubAsMainPhoto(club)
                    .build();

            club.setClubMainPhoto(file);
        } else {

            File file = club.getClubMainPhoto();

            awsService.deleteFile(Constants.BUCKET_NAME, file.getName());

            file.setName(fileName);
        }


        return clubMapper.clubDetailsDto(clubDao.updateClub(club));
    }

    @Transactional
    @Override
    public ClubDetailsDto uploadClubFiles(Integer clubId, MultipartFile[] files) throws IOException {
        Club club = clubDao.getClubById(clubId);

        for (MultipartFile file : files) {
            File clubFile = File.builder()
                    .club(club)
                    .name(awsService.uploadFile(Constants.BUCKET_NAME, file))
                    .build();
            club.getFiles().add(clubFile);
        }

        return clubMapper.clubDetailsDto(clubDao.updateClub(club));
    }

    @Override
    public ClubDetailsDto getClubById(Integer id) {
        return clubMapper.clubDetailsDto(clubDao.getClubById(id));
    }


    @Override
    public List<ClubDetailsDto> getClubs(String clubName, Integer divisionId, Boolean favorite, Integer userId, String status) {
        UserAccount userAccount = userAccountDao.getUserAccountById(userId);
        return clubDao.getClubs(clubName, divisionId, favorite, userId, status).stream().map(club -> clubMapper.clubDetailsDto2(club, userAccount)).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public PlayerDto addPlayer(AddPlayerDto addPlayerDto, MultipartFile mainPhoto) throws IOException {

        Club club = clubDao.getClubById(addPlayerDto.getClubId());


        String mainPic = null;

        if(mainPhoto != null) {
            mainPic = awsService.uploadFile(Constants.BUCKET_NAME, mainPhoto);
        }

        File file = File.builder()
                .name(mainPic)
                .build();

        Player player = Player.builder()
                .username(addPlayerDto.getUsername())
                .mainPhoto(file)
                .number(addPlayerDto.getNumber())
                .playerPosition(addPlayerDto.getPlayerPosition())
                .playerState(PlayerState.ACTIVE)
                .age(addPlayerDto.getAge())
                .height(addPlayerDto.getHeight())
                .weight(addPlayerDto.getWeight())
                .country(addPlayerDto.getCountry())
                .county(addPlayerDto.getCounty())
                .town(addPlayerDto.getTown())
                .playerClubs(new ArrayList<>())
                .files(new ArrayList<>())
                .playerClubs()
                .build();

        file.setPlayer(player);
        player.getFiles().add(file);


        return playerMapper.playerDto(playerDao.addPlayer(player));
    }

    @Override
    public PlayerDto getPlayerById(Integer playerId) {
        return playerMapper.playerDto(playerDao.getPlayerById(playerId));
    }

    @Transactional
    @Override
    public PlayerDto signPlayer(SignPlayerDto signPlayerDto) {
        Player player = playerDao.getPlayerById(signPlayerDto.getPlayerId());

        Club newClub = clubDao.getClubById(signPlayerDto.getNewClubId());

        List<PlayerClub> activePlayers = newClub.getPlayerClubs().stream().filter(playerClub -> playerClub.getPlayer().getPlayerState().equals(PlayerState.ACTIVE)).toList();

        if(activePlayers.size() > 10) {
            player.setPlayerState(PlayerState.BENCH);
        }

        PlayerClub oldPlayerClub = player.getPlayerClubs().stream()
                .filter(playerClub1 -> playerClub1.getPlayer().getId().equals(player.getId()) && !playerClub1.getArchived())
                .findFirst()
                .orElse(null);

        if(oldPlayerClub != null) {
            oldPlayerClub.setArchived(true);
            oldPlayerClub.setArchivedAt(LocalDateTime.now());
        }

        PlayerClub playerClub = PlayerClub.builder()
                .player(player)
                .club(newClub)
                .createdAt(LocalDateTime.now())
                .archived(false)
                .build();

        newClub.getPlayerClubs().add(playerClub);

        clubDao.updateClub(newClub);

//        player.getPlayerClubs().add(playerClub);


        return playerMapper.playerDto(playerDao.updatePlayer(player));
    }
    @Transactional
    @Override
    public PlayerDto removePlayerFromTeam(UnsignPlayerDto unsignPlayerDto) {
        Player player = playerDao.getPlayerById(unsignPlayerDto.getPlayerId());
        Club club = clubDao.getClubById(unsignPlayerDto.getClubId());

        if (club != null && player != null) {
            PlayerClub playerClub = player.getPlayerClubs().stream()
                    .filter(pc -> pc.getPlayer().getId().equals(player.getId()) && !pc.getArchived())
                    .findFirst()
                    .orElse(null);

            if (playerClub != null) {
                playerClub.setArchived(true);
                playerClub.setArchivedAt(LocalDateTime.now());
            } else {
                throw new IllegalStateException("Player is not currently active in the specified club.");
            }

        } else {
            throw new IllegalArgumentException("Player or Club not found.");
        }


        return playerMapper.playerDto(playerDao.updatePlayer(player));
    }
    @Transactional
    @Override
    public PlayerDto updatePlayerDetails(UpdatePlayerDto updatePlayerDto) {
        Player player = playerDao.getPlayerById(updatePlayerDto.getPlayerId());

        if(!Objects.equals(player.getUsername(), updatePlayerDto.getUsername())) {
            player.setUsername(updatePlayerDto.getUsername());
        }

        if(!Objects.equals(player.getHeight(), updatePlayerDto.getHeight())) {
            player.setHeight(updatePlayerDto.getHeight());
        }

        if(!Objects.equals(player.getWeight(), updatePlayerDto.getWeight())) {
            player.setWeight(updatePlayerDto.getWeight());
        }

        if(!Objects.equals(player.getNumber(), updatePlayerDto.getNumber())) {
            player.setNumber(updatePlayerDto.getNumber());
        }

        if(player.getPlayerPosition() != updatePlayerDto.getPlayerPosition()) {
            player.setPlayerPosition(updatePlayerDto.getPlayerPosition());
        }

        if(!Objects.equals(player.getCountry(), updatePlayerDto.getCountry())) {
            player.setCountry(updatePlayerDto.getCountry());
        }

        if(!Objects.equals(player.getCounty(), updatePlayerDto.getCounty())) {
            player.setCounty(updatePlayerDto.getCounty());
        }

        if(!Objects.equals(player.getTown(), updatePlayerDto.getTown())) {
            player.setTown(updatePlayerDto.getTown());
        }

        return playerMapper.playerDto(playerDao.updatePlayer(player));
    }
    @Transactional
    @Override
    public PlayerDto updatePlayerMainPhoto(Integer playerId, MultipartFile mainPhoto) throws IOException {
        Player player = playerDao.getPlayerById(playerId);
        File mainPic = player.getMainPhoto();

        if(mainPhoto != null) {
            mainPic.setName(awsService.uploadFile(Constants.BUCKET_NAME, mainPhoto));
        }

        return playerMapper.playerDto(playerDao.updatePlayer(player));
    }
    @Transactional
    @Override
    public PlayerDto uploadPlayerFiles(Integer playerId, MultipartFile[] files) throws IOException {
        Player player = playerDao.getPlayerById(playerId);

        for (MultipartFile file : files) {
            File clubFile = File.builder()
                    .player(player)
                    .name(awsService.uploadFile(Constants.BUCKET_NAME, file))
                    .build();
            player.getFiles().add(clubFile);
        }

        return playerMapper.playerDto(playerDao.updatePlayer(player));
    }

    @Override
    public List<PlayerDto> getPlayers() {
        return playerDao.getPlayers().stream().map(playerMapper::playerDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookmarkSuccessDto bookmarkClub(BookmarkClubDto bookmarkClubDto) {
        UserAccount userAccount = userAccountDao.getUserAccountById(bookmarkClubDto.getUserId());
        Club club = clubDao.getClubById(bookmarkClubDto.getClubId());

        if(userAccount.getFavoriteClubs().contains(club)) {
            throw new IllegalStateException("Club is already favorite.");
        }

        userAccount.getFavoriteClubs().add(club);
        club.getBookmarkedBy().add(userAccount);

        BookmarkSuccessDto bookmarkSuccessDto = BookmarkSuccessDto.builder()
                .clubId(club.getId())
                .success(true)
                .build();


        return bookmarkSuccessDto;
    }

    @Override
    public UserBookmarkedClubsDto getUserFavoriteClubs(Integer userId) {
        UserAccount userAccount = userAccountDao.getUserAccountById(userId);
        return clubMapper.userBookmarkedClubDto(userAccount);
    }

    @Transactional
    @Override
    public ClubDetailsMin updateClubStatus(ClubStatusUpdateDto clubStatusUpdateDto) {
        Club club = clubDao.getClubById(clubStatusUpdateDto.getClubId());
        club.setClubStatus(clubStatusUpdateDto.getClubStatus());
        return clubMapper.clubDetailsMinDto(clubDao.updateClub(club));
    }

    @Transactional
    @Override
    public String makeAllClubsPending() {
        List<Club> clubs = clubDao.getAllClubs();
        for(Club club : clubs) {
            club.setClubStatus(ClubStatus.PENDING);
            clubDao.updateClub(club);
        }
        return "Clubs status changed to pending";
    }
}
