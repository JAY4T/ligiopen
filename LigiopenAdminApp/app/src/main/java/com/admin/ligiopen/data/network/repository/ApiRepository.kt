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
import retrofit2.http.Query

interface ApiRepository {

    suspend fun login(userLoginRequestBody: UserLoginRequestBody): Response<UserLoginResponseBody>

    //    Create match location
    suspend fun createMatchLocation(
        token: String,
        locationCreationRequest: LocationCreationRequest
    ): Response<MatchLocationResponseBody>

//    Update match location
    suspend fun updateMatchLocation(
        token: String,
        locationUpdate: LocationUpdateRequest
    ): Response<MatchLocationResponseBody>

//    Upload match location photos
    suspend fun uploadMatchLocationPhotos(
        token: String,
        locationId: Int,
        files: List<MultipartBody.Part>,
    ): Response<MatchLocationResponseBody>

    //    Remove match location file
    suspend fun removeMatchLocationFile(
        token: String,
        locationId: Int,
        fileId: Int,
    ): Response<MatchLocationResponseBody>

//    Get match location
    suspend fun getMatchLocation(
        token: String,
        locationId: Int,
    ): Response<MatchLocationResponseBody>

//    Get match locations
    suspend fun getMatchLocations(
        token: String,
        venueName: String?,
        locationName: String?
    ): Response<MatchLocationsResponseBody>

    //    Create match fixture
    suspend fun createMatchFixture(
        token: String,
        fixtureCreation: FixtureCreationRequest
    ): Response<FixtureResponseBody>

    //    Update match fixture
    suspend fun updateMatchFixture(
        token: String,
        fixtureUpdate: FixtureUpdateRequest
    ): Response<FixtureResponseBody>

    //    Get match fixture
    suspend fun getMatchFixture(
        token: String,
        fixtureId: Int,
    ): Response<FixtureResponseBody>

    //    Get match fixtures
    suspend fun getMatchFixtures(
        token: String,
        status: String?,
    ): Response<FixturesResponseBody>

    //    Upload match commentary
    suspend fun uploadMatchCommentary(
        token: String,
        commentaryCreationRequest: CommentaryCreationRequest
    ): Response<MatchCommentaryResponseBody>

    //    Update match commentary
    suspend fun updateMatchCommentary(
        token: String,
        commentaryUpdateRequest: CommentaryUpdateRequest
    ): Response<MatchCommentaryResponseBody>

    //    Upload commentary files
    suspend fun uploadMatchCommentaryFiles(
        token: String,
        commentaryId: Int,
        files: MutableList<MultipartBody.Part?>,
    ): Response<MatchCommentaryResponseBody>

    //    Get match commentary
    suspend fun getMatchCommentary(
        token: String,
        commentaryId: Int,
    ): Response<MatchCommentaryResponseBody>

    //    Get full match details
    suspend fun getFullMatchDetails(
        token: String,
        postMatchAnalysisId: Int,
    ): Response<FullMatchResponseBody>

    //    Get clubs
    suspend fun getClubs(
        token: String,
        clubName: String?,
        divisionId: Int?,
        userId: Int,
        favorite: Boolean?,
        status: String?
    ): Response<ClubsResponseBody>

    //    Get club
    suspend fun getClub(
        token: String,
        clubId: Int,
    ): Response<ClubResponseBody>

    //    Add club
    suspend fun addClub(
        token: String,
        clubAdditionRequest: ClubAdditionRequest,
        logo: MultipartBody.Part
    ): Response<ClubResponseBody>

    //    Update club logo
    suspend fun changeClubLogo(
        token: String,
        clubId: Int,
        clubLogo: MultipartBody.Part
    ): Response<ClubResponseBody>

    //    Update club photo
    suspend fun changeClubPhoto(
        token: String,
        clubId: Int,
        photo: MultipartBody.Part
    ): Response<ClubResponseBody>

    //    Get player
    suspend fun getPlayer(
        token: String,
        playerId: Int,
    ): Response<PlayerResponseBody>

    //    Update player details
    suspend fun updatePlayerDetails(
        token: String,
        playerUpdateRequest: PlayerUpdateRequest
    ): Response<PlayerResponseBody>

    //    Get all news

    suspend fun getAllNews(
        token: String,
        status: String?
    ): Response<NewsResponseBody>

    //    Get single news

    suspend fun getSingleNews(
        token: String,
        newsId: Int,
    ): Response<SingleNewsResponseBody>

    //    Publish news
    suspend fun publishNews(
        token: String,
        newsAdditionRequestBody: NewsAdditionRequestBody,
        coverPhoto: MultipartBody.Part
    ): Response<SingleNewsResponseBody>

    //    Publish news item
    suspend fun publishNewsItem(
        token: String,
        newsItemAdditionRequestBody: NewsItemAdditionRequestBody,
        photo: MultipartBody.Part,
    ) : Response<SingleNewsItemResponseBody>

//    Change club status
    suspend fun changeClubStatus(
        token: String,
        changeClubStatusRequestBody: ChangeClubStatusRequestBody
    ): Response<ChangeClubStatusResponseBody>

//    Change news status
    suspend fun changeNewsStatus(
        token: String,
        newsStatusUpdateRequestBody: NewsStatusUpdateRequestBody
    ): Response<SingleNewsResponseBody>

    suspend fun updateClubDetails(
        token: String,
        clubUpdateRequest: ClubUpdateRequest
    ): Response<ClubResponseBody>

    //    Get user
    suspend fun getUser(
        token: String,
        userId: Int
    ): Response<UserResponseBody>

    //    Get users
    suspend fun getUsers(
        token: String,
        username: String?,
        role: String?
    ): Response<UsersResponseBody>

    //    Set super admin
    suspend fun setSuperAdmin(
        token: String,
        adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody>

    //    Set club admin
    suspend fun setTeamAdmin(
        token: String,
        clubAdminSetRequestBody: ClubAdminSetRequestBody
    ): Response<UserResponseBody>

    //    Set club admin
    suspend fun setContentAdmin(
        token: String,
        adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody>

}