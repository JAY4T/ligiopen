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
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body userLoginRequestBody: UserLoginRequestBody
    ): Response<UserLoginResponseBody>

//    Create match location

    @POST("match-location")
    suspend fun createMatchLocation(
        @Header("Authorization") token: String,
        @Body locationCreation: LocationCreationRequest
    ): Response<MatchLocationResponseBody>

//    Update match location

    @PUT("match-location")
    suspend fun updateMatchLocation(
        @Header("Authorization") token: String,
        @Body locationUpdate: LocationUpdateRequest
    ): Response<MatchLocationResponseBody>

//    Upload match location photos

    @Multipart
    @PUT("match-location/files/{locationId}")
    suspend fun uploadMatchLocationPhotos(
        @Header("Authorization") token: String,
        @Path("locationId") locationId: Int,
        @Part files: List<MultipartBody.Part>,
    ): Response<MatchLocationResponseBody>

//    Remove match location file
    @PUT("match-location/file-removal/{locationId}/{fileId}")
    suspend fun removeMatchLocationFile(
        @Header("Authorization") token: String,
        @Path("locationId") locationId: Int,
        @Path("fileId") fileId: Int,
    ): Response<MatchLocationResponseBody>

//    Get match location

    @GET("match-location/{locationId}")
    suspend fun getMatchLocation(
        @Header("Authorization") token: String,
        @Path("locationId") locationId: Int,
    ): Response<MatchLocationResponseBody>

//    Get match locations

    @GET("match-location/all")
    suspend fun getMatchLocations(
        @Header("Authorization") token: String,
        @Query("venueName") venueName: String?,
        @Query("locationName") locationName: String?
    ): Response<MatchLocationsResponseBody>

//    Create match fixture
    @POST("match-fixture")
    suspend fun createMatchFixture(
        @Header("Authorization") token: String,
        @Body fixtureCreation: FixtureCreationRequest
    ): Response<FixtureResponseBody>

//    Update match fixture
    @PUT("match-fixture")
    suspend fun updateMatchFixture(
        @Header("Authorization") token: String,
        @Body fixtureUpdate: FixtureUpdateRequest
    ): Response<FixtureResponseBody>

//    Get match fixture
    @GET("match-fixture/{fixtureId}")
    suspend fun getMatchFixture(
        @Header("Authorization") token: String,
        @Path("fixtureId") fixtureId: Int,
    ): Response<FixtureResponseBody>

//    Get match fixtures
    @GET("match-fixture/all")
    suspend fun getMatchFixtures(
    @Header("Authorization") token: String,
    @Query("status") status: String?,
    ): Response<FixturesResponseBody>

//    Upload match commentary
    @POST("match-commentary")
    suspend fun uploadMatchCommentary(
        @Header("Authorization") token: String,
        @Body commentaryCreationRequest: CommentaryCreationRequest
    ): Response<MatchCommentaryResponseBody>

//    Update match commentary
    @PUT("match-commentary")
    suspend fun updateMatchCommentary(
        @Header("Authorization") token: String,
        @Body commentaryUpdateRequest: CommentaryUpdateRequest
    ): Response<MatchCommentaryResponseBody>

//    Upload commentary files
    @Multipart
    @PUT("match-event/files/{commentaryId}")
    suspend fun uploadMatchCommentaryFiles(
        @Header("Authorization") token: String,
        @Path("commentaryId") commentaryId: Int,
        @Part files: MutableList<MultipartBody.Part?>,
    ): Response<MatchCommentaryResponseBody>

//    Get match commentary
    @GET("match-commentary/{commentaryId}")
    suspend fun getMatchCommentary(
        @Header("Authorization") token: String,
        @Path("commentaryId") commentaryId: Int,
    ): Response<MatchCommentaryResponseBody>

//    Get full match details
    @GET("post-match-details/{postMatchAnalysisId}")
    suspend fun getFullMatchDetails(
        @Header("Authorization") token: String,
        @Path("postMatchAnalysisId") postMatchAnalysisId: Int,
    ): Response<FullMatchResponseBody>

//    Get clubs
    @GET("club")
    suspend fun getClubs(
        @Header("Authorization") token: String,
        @Query("clubName") clubName: String?,
        @Query("divisionId") divisionId: Int?,
        @Query("userId") userId: Int,
        @Query("favorite") favorite: Boolean?,
        @Query("status") status: String?
    ): Response<ClubsResponseBody>

//    Get club
    @GET("club/{clubId}")
    suspend fun getClub(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: Int,
    ): Response<ClubResponseBody>

//    Add club
    @Multipart
    @POST("club")
    suspend fun addClub(
        @Header("Authorization") token: String,
        @Part("data") clubAdditionRequest: ClubAdditionRequest,
        @Part logo: MultipartBody.Part
    ): Response<ClubResponseBody>

//    Update club logo
    @Multipart
    @PUT("club/logo/{clubId}")
    suspend fun changeClubLogo(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: Int,
        @Part clubLogo: MultipartBody.Part
    ): Response<ClubResponseBody>

//    Update club photo
    @Multipart
    @PUT("club/main-photo/{clubId}")
    suspend fun changeClubPhoto(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: Int,
        @Part photo: MultipartBody.Part
    ): Response<ClubResponseBody>

//    Get player
    @GET("player/{playerId}")
    suspend fun getPlayer(
        @Header("Authorization") token: String,
        @Path("playerId") playerId: Int,
    ): Response<PlayerResponseBody>

//    Update player details
    @PUT("player")
    suspend fun updatePlayerDetails(
        @Header("Authorization") token: String,
        @Body playerUpdateRequest: PlayerUpdateRequest
    ): Response<PlayerResponseBody>

//    Get all news
    @GET("news/all")
    suspend fun getAllNews(
        @Header("Authorization") token: String,
        @Query("status") status: String?
    ): Response<NewsResponseBody>

//    Get single news
    @GET("news/{newsId}")
    suspend fun getSingleNews(
        @Header("Authorization") token: String,
        @Path("newsId") newsId: Int,
    ): Response<SingleNewsResponseBody>

//    Publish news
    @Multipart
    @POST("news")
    suspend fun publishNews(
        @Header("Authorization") token: String,
        @Part("data") newsAdditionRequestBody: NewsAdditionRequestBody,
        @Part coverPhoto: MultipartBody.Part,
    ): Response<SingleNewsResponseBody>

//    Publish news item
    @Multipart
    @POST("news-item")
    suspend fun publishNewsItem(
    @Header("Authorization") token: String,
    @Part("data") newsItemAdditionRequestBody: NewsItemAdditionRequestBody,
    @Part photo: MultipartBody.Part,
    ) : Response<SingleNewsItemResponseBody>

    @PUT("club/status-update")
    suspend fun changeClubStatus(
        @Header("Authorization") token: String,
        @Body changeClubStatusRequestBody: ChangeClubStatusRequestBody
    ): Response<ChangeClubStatusResponseBody>

//    Change news status
    @PUT("news/status-update")
    suspend fun changeNewsStatus(
        @Header("Authorization") token: String,
        @Body newsStatusUpdateRequestBody: NewsStatusUpdateRequestBody
    ): Response<SingleNewsResponseBody>

    @PUT("club/details")
    suspend fun updateClubDetails(
        @Header("Authorization") token: String,
        @Body clubUpdateRequest: ClubUpdateRequest
    ): Response<ClubResponseBody>

//    Get user
    @GET("user/{userId}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<UserResponseBody>

//    Get users
    @GET("user")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("username") username: String?,
        @Query("role") role: String?
    ): Response<UsersResponseBody>

//    Set super admin
    @PUT("user/super-admin")
    suspend fun setSuperAdmin(
        @Header("Authorization") token: String,
        @Body adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody>

    //    Set club admin
    @PUT("user/team-admin")
    suspend fun setTeamAdmin(
        @Header("Authorization") token: String,
        @Body clubAdminSetRequestBody: ClubAdminSetRequestBody
    ): Response<UserResponseBody>

    //    Set club admin
    @PUT("user/content-admin")
    suspend fun setContentAdmin(
        @Header("Authorization") token: String,
        @Body adminSetRequestBody: AdminSetRequestBody
    ): Response<UserResponseBody>

}