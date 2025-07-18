package com.jabulani.ligiopen.data.network

import com.admin.ligiopen.data.network.models.match.location.MatchLocationResponseBody
import com.admin.ligiopen.data.network.models.match.location.MatchLocationsResponseBody
import com.jabulani.ligiopen.data.network.model.club.ClubBookmarkRequestBody
import com.jabulani.ligiopen.data.network.model.club.ClubBookmarkResponseBody
import com.jabulani.ligiopen.data.network.model.club.ClubDivisionsResponseBody
import com.jabulani.ligiopen.data.network.model.club.ClubResponseBody
import com.jabulani.ligiopen.data.network.model.club.ClubsResponseBody
import com.jabulani.ligiopen.data.network.model.club.PlayerResponseBody
import com.jabulani.ligiopen.data.network.model.match.commentary.FullMatchResponseBody
import com.jabulani.ligiopen.data.network.model.match.commentary.MatchCommentaryResponseBody
import com.jabulani.ligiopen.data.network.model.match.fixture.FixtureResponseBody
import com.jabulani.ligiopen.data.network.model.match.fixture.FixturesResponseBody
import com.jabulani.ligiopen.data.network.model.news.NewsResponseBody
import com.jabulani.ligiopen.data.network.model.news.SingleNewsResponseBody
import com.jabulani.ligiopen.data.network.model.user.UserLoginRequestBody
import com.jabulani.ligiopen.data.network.model.user.UserLoginResponseBody
import com.jabulani.ligiopen.data.network.model.user.UserRegistrationRequestBody
import com.jabulani.ligiopen.data.network.model.user.UserRegistrationResponseBody
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: ApiService): ApiRepository {
    override suspend fun createUserAccount(
        userRegistrationRequestBody: UserRegistrationRequestBody
    ): Response<UserRegistrationResponseBody> = apiService.createUserAccount(userRegistrationRequestBody = userRegistrationRequestBody)

    override suspend fun login(
        userLoginRequestBody: UserLoginRequestBody
    ): Response<UserLoginResponseBody> = apiService.login(userLoginRequestBody = userLoginRequestBody)

    override suspend fun getClubs(
        token: String,
        clubName: String?,
        divisionId: Int?,
        favorite: Boolean,
        userId: Int
    ): Response<ClubsResponseBody> =
        apiService.getClubs(
            token = "Bearer $token",
            clubName = clubName,
            divisionId = divisionId,
            favorite = favorite,
            userId = userId
        )

    override suspend fun getClubById(token: String, id: Int): Response<ClubResponseBody> =
        apiService.getClubById(
            token = "Bearer $token",
            id = id
        )

    override suspend fun getPlayerById(token: String, id: Int): Response<PlayerResponseBody> =
        apiService.getPlayerById(
            token = "Bearer $token",
            id = id
        )

    override suspend fun getMatchLocation(
        token: String,
        locationId: Int
    ): Response<MatchLocationResponseBody> =
        apiService.getMatchLocation(
            token = "Bearer $token",
            locationId = locationId
        )

    override suspend fun getMatchLocations(token: String): Response<MatchLocationsResponseBody> =
        apiService.getMatchLocations(
            token = "Bearer $token",
        )

    override suspend fun getMatchFixture(
        token: String,
        fixtureId: Int
    ): Response<FixtureResponseBody> =
        apiService.getMatchFixture(
            token = "Bearer $token",
            fixtureId = fixtureId
        )

    override suspend fun getMatchFixtures(
        token: String,
        status: String?,
        clubIds: List<Int>,
        matchDateTime: String?
    ): Response<FixturesResponseBody> =
        apiService.getMatchFixtures(
            token = "Bearer $token",
            status = status,
            clubIds = clubIds,
            matchDateTime = matchDateTime,
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

    override suspend fun getClub(token: String, clubId: Int): Response<ClubResponseBody> =
        apiService.getClub(
            token = "Bearer $token",
            clubId = clubId
        )

    override suspend fun getPlayer(token: String, playerId: Int): Response<PlayerResponseBody> =
        apiService.getPlayer(
            token = "Bearer $token",
            playerId = playerId
        )

    override suspend fun getAllNews(token: String, clubId: Int?): Response<NewsResponseBody> =
        apiService.getAllNews(
            token = "Bearer $token",
            clubId = clubId
        )

    override suspend fun getSingleNews(
        token: String,
        newsId: Int
    ): Response<SingleNewsResponseBody> =
        apiService.getSingleNews(
        token = "Bearer $token",
        newsId = newsId
    )

    override suspend fun getAllLeagues(token: String): Response<ClubDivisionsResponseBody> =
        apiService.getAllLeagues(
            token = "Bearer $token"
        )

    override suspend fun bookmarkClub(
        token: String,
        clubBookmarkRequestBody: ClubBookmarkRequestBody
    ): Response<ClubBookmarkResponseBody> =
        apiService.bookmarkClub(
            token = "Bearer $token",
            clubBookmarkRequestBody = clubBookmarkRequestBody
        )


}