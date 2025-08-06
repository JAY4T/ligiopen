package com.admin.ligiopen.data.network.repository

import com.admin.ligiopen.data.network.models.auth.UserLoginRequestBody
import com.admin.ligiopen.data.network.models.auth.UserLoginResponseBody
import com.admin.ligiopen.data.network.models.club.ChangeClubStatusRequestBody
import com.admin.ligiopen.data.network.models.club.ChangeClubStatusResponseBody
import com.admin.ligiopen.data.network.models.club.ClubAdditionRequest
import com.admin.ligiopen.data.network.models.club.ClubResponseBody
import com.admin.ligiopen.data.network.models.club.ClubUpdateRequest
import com.admin.ligiopen.data.network.models.club.ClubsResponseBody
import com.admin.ligiopen.data.network.models.match.commentary.CommentaryCreationRequest
import com.admin.ligiopen.data.network.models.match.commentary.CommentaryUpdateRequest
import com.admin.ligiopen.data.network.models.match.commentary.FullMatchResponseBody
import com.admin.ligiopen.data.network.models.match.commentary.MatchCommentaryResponseBody
import com.admin.ligiopen.data.network.models.match.fixture.FixtureCreationRequest
import com.admin.ligiopen.data.network.models.match.fixture.FixtureResponseBody
import com.admin.ligiopen.data.network.models.match.fixture.FixtureUpdateRequest
import com.admin.ligiopen.data.network.models.match.fixture.FixturesResponseBody
import com.admin.ligiopen.data.network.models.match.location.LocationCreationRequest
import com.admin.ligiopen.data.network.models.match.location.LocationUpdateRequest
import com.admin.ligiopen.data.network.models.match.location.MatchLocationResponseBody
import com.admin.ligiopen.data.network.models.match.location.MatchLocationsResponseBody
import com.admin.ligiopen.data.network.models.news.NewsAdditionRequestBody
import com.admin.ligiopen.data.network.models.news.NewsItemAdditionRequestBody
import com.admin.ligiopen.data.network.models.news.NewsResponseBody
import com.admin.ligiopen.data.network.models.news.NewsStatusUpdateRequestBody
import com.admin.ligiopen.data.network.models.news.SingleNewsItemResponseBody
import com.admin.ligiopen.data.network.models.news.SingleNewsResponseBody
import com.admin.ligiopen.data.network.models.player.PlayerResponseBody
import com.admin.ligiopen.data.network.models.player.PlayerUpdateRequest
import com.admin.ligiopen.data.network.models.user.AdminSetRequestBody
import com.admin.ligiopen.data.network.models.user.ClubAdminSetRequestBody
import com.admin.ligiopen.data.network.models.user.UserResponseBody
import com.admin.ligiopen.data.network.models.user.UsersResponseBody
import okhttp3.MultipartBody
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun login(userLoginRequestBody: UserLoginRequestBody): Response<UserLoginResponseBody> =
        apiService.login(userLoginRequestBody)

    override suspend fun createMatchLocation(
        token: String,
        locationCreationRequest: LocationCreationRequest
    ): Response<MatchLocationResponseBody> =
        apiService.createMatchLocation(
            token = "Bearer $token",
            locationCreation = locationCreationRequest
        )

    override suspend fun updateMatchLocation(
        token: String,
        locationUpdate: LocationUpdateRequest
    ): Response<MatchLocationResponseBody> =
        apiService.updateMatchLocation(
            token = "Bearer $token",
            locationUpdate = locationUpdate
        )

    override suspend fun uploadMatchLocationPhotos(
        token: String,
        locationId: Int,
        files: List<MultipartBody.Part>
    ): Response<MatchLocationResponseBody> =
        apiService.uploadMatchLocationPhotos(
            token = "Bearer $token",
            locationId = locationId,
            files = files
        )

    override suspend fun removeMatchLocationFile(
        token: String,
        locationId: Int,
        fileId: Int
    ): Response<MatchLocationResponseBody> =
        apiService.removeMatchLocationFile(
            token = "Bearer $token",
            locationId = locationId,
            fileId = fileId
        )

    override suspend fun getMatchLocation(
        token: String,
        locationId: Int
    ): Response<MatchLocationResponseBody> =
        apiService.getMatchLocation(
            token = "Bearer $token",
            locationId = locationId
        )

    override suspend fun getMatchLocations(
        token: String,
        venueName: String?,
        locationName: String?
    ): Response<MatchLocationsResponseBody> =
        apiService.getMatchLocations(
            token = "Bearer $token",
            venueName = venueName,
            locationName = locationName
        )

    override suspend fun createMatchFixture(
        token: String,
        fixtureCreation: FixtureCreationRequest
    ): Response<FixtureResponseBody> =
        apiService.createMatchFixture(
            token = "Bearer $token",
            fixtureCreation = fixtureCreation
        )

    override suspend fun updateMatchFixture(
        token: String,
        fixtureUpdate: FixtureUpdateRequest
    ): Response<FixtureResponseBody> =
        apiService.updateMatchFixture(
            token = "Bearer $token",
            fixtureUpdate = fixtureUpdate
        )

    override suspend fun getMatchFixture(
        token: String,
        fixtureId: Int
    ): Response<FixtureResponseBody> =
        apiService.getMatchFixture(
            token = "Bearer $token",
            fixtureId = fixtureId
        )

    override suspend fun getMatchFixtures(token: String, status: String?): Response<FixturesResponseBody> =
        apiService.getMatchFixtures(
            token = "Bearer $token",
            status = status
        )

    override suspend fun uploadMatchCommentary(
        token: String,
        commentaryCreationRequest: CommentaryCreationRequest
    ): Response<MatchCommentaryResponseBody> =
        apiService.uploadMatchCommentary(
            token = "Bearer $token",
            commentaryCreationRequest = commentaryCreationRequest
        )

    override suspend fun updateMatchCommentary(
        token: String,
        commentaryUpdateRequest: CommentaryUpdateRequest
    ): Response<MatchCommentaryResponseBody> =
        apiService.updateMatchCommentary(
            token = "Bearer $token",
            commentaryUpdateRequest = commentaryUpdateRequest
        )

    override suspend fun uploadMatchCommentaryFiles(
        token: String,
        commentaryId: Int,
        files: MutableList<MultipartBody.Part?>
    ): Response<MatchCommentaryResponseBody> =
        apiService.uploadMatchCommentaryFiles(
            token = "Bearer $token",
            commentaryId = commentaryId,
            files = files
        )

    override suspend fun getMatchCommentary(
        token: String,
        commentaryId: Int
    ): Response<MatchCommentaryResponseBody> =
        apiService.getMatchCommentary(
            token = "Bearer $token",
            commentaryId = commentaryId
        )

    override suspend fun getFullMatchDetails(
        token: String,
        postMatchAnalysisId: Int
    ): Response<FullMatchResponseBody> =
        apiService.getFullMatchDetails(
            token = "Bearer $token",
            postMatchAnalysisId = postMatchAnalysisId
        )

    override suspend fun getClubs(
        token: String,
        clubName: String?,
        divisionId: Int?,
        userId: Int,
        favorite: Boolean?,
        status: String?
    ): Response<ClubsResponseBody> =
        apiService.getClubs(
            token = "Bearer $token",
            clubName = clubName,
            divisionId = divisionId,
            userId = userId,
            favorite = favorite,
            status = status
        )

    override suspend fun getClub(token: String, clubId: Int): Response<ClubResponseBody> =
        apiService.getClub(
            token = "Bearer $token",
            clubId = clubId
        )

    override suspend fun addClub(
        token: String,
        clubAdditionRequest: ClubAdditionRequest,
        logo: MultipartBody.Part
    ): Response<ClubResponseBody> =
        apiService.addClub(
            token = "Bearer $token",
            clubAdditionRequest = clubAdditionRequest,
            logo = logo
        )

    override suspend fun changeClubLogo(
        token: String,
        clubId: Int,
        clubLogo: MultipartBody.Part
    ): Response<ClubResponseBody> =
        apiService.changeClubLogo(
            token = "Bearer $token",
            clubId = clubId,
            clubLogo = clubLogo
        )

    override suspend fun changeClubPhoto(
        token: String,
        clubId: Int,
        photo: MultipartBody.Part
    ): Response<ClubResponseBody> =
        apiService.changeClubPhoto(
            token = "Bearer $token",
            clubId = clubId,
            photo = photo
        )

    override suspend fun getPlayer(token: String, playerId: Int): Response<PlayerResponseBody> =
        apiService.getPlayer(
            token = "Bearer $token",
            playerId = playerId
        )

    override suspend fun updatePlayerDetails(
        token: String,
        playerUpdateRequest: PlayerUpdateRequest
    ): Response<PlayerResponseBody> =
        apiService.updatePlayerDetails(
            token = "Bearer $token",
            playerUpdateRequest = playerUpdateRequest
        )

    override suspend fun getAllNews(
        token: String,
        status: String?
    ): Response<NewsResponseBody> =
        apiService.getAllNews(
            token = "Bearer $token",
            status = status
        )

    override suspend fun getSingleNews(
        token: String,
        newsId: Int
    ): Response<SingleNewsResponseBody> =
        apiService.getSingleNews(
            token = "Bearer $token",
            newsId = newsId
        )

    override suspend fun publishNews(
        token: String,
        newsAdditionRequestBody: NewsAdditionRequestBody,
        coverPhoto: MultipartBody.Part
    ): Response<SingleNewsResponseBody> =
        apiService.publishNews(
            token = "Bearer $token",
            newsAdditionRequestBody = newsAdditionRequestBody,
            coverPhoto = coverPhoto
        )

    override suspend fun publishNewsItem(
        token: String,
        newsItemAdditionRequestBody: NewsItemAdditionRequestBody,
        photo: MultipartBody.Part
    ): Response<SingleNewsItemResponseBody> =
        apiService.publishNewsItem(
            token = "Bearer $token",
            newsItemAdditionRequestBody = newsItemAdditionRequestBody,
            photo = photo
        )

    override suspend fun changeClubStatus(
        token: String,
        changeClubStatusRequestBody: ChangeClubStatusRequestBody
    ): Response<ChangeClubStatusResponseBody> =
        apiService.changeClubStatus(
            token = "Bearer $token",
            changeClubStatusRequestBody = changeClubStatusRequestBody
        )

    override suspend fun changeNewsStatus(
        token: String,
        newsStatusUpdateRequestBody: NewsStatusUpdateRequestBody
    ): Response<SingleNewsResponseBody> =
        apiService.changeNewsStatus(
            token = "Bearer $token",
            newsStatusUpdateRequestBody = newsStatusUpdateRequestBody
        )

    override suspend fun updateClubDetails(
        token: String,
        clubUpdateRequest: ClubUpdateRequest
    ): Response<ClubResponseBody> =
        apiService.updateClubDetails(
            token = "Bearer $token",
            clubUpdateRequest = clubUpdateRequest
        )

    override suspend fun getUser(token: String, userId: Int): Response<UserResponseBody> =
        apiService.getUser(
            token = "Bearer $token",
            userId = userId
        )

    override suspend fun getUsers(
        token: String,
        username: String?,
        role: String?
    ): Response<UsersResponseBody> =
        apiService.getUsers(
            token = "Bearer $token",
            username = username,
            role = role
        )

    override suspend fun setSuperAdmin(
        token: String,
        adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody> =
        apiService.setSuperAdmin(
            token = "Bearer $token",
            adminSetRequestBody = adminSetRequestBody
        )

    override suspend fun setTeamAdmin(
        token: String,
        clubAdminSetRequestBody: ClubAdminSetRequestBody
    ): Response<UserResponseBody> =
        apiService.setTeamAdmin(
            token = "Bearer $token",
            clubAdminSetRequestBody = clubAdminSetRequestBody
        )

    override suspend fun setContentAdmin(
        token: String,
        adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody> =
        apiService.setContentAdmin(
            token = "Bearer $token",
            adminSetRequestBody = adminSetRequestBody
        )
}