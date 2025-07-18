package com.jabulani.ligiopen.ui.inapp.clubs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.model.UserAccount
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.model.club.ClubBookmarkRequestBody
import com.jabulani.ligiopen.data.network.model.club.ClubDivisionDt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubsScreenUiData())
    val uiState: StateFlow<ClubsScreenUiData> = _uiState.asStateFlow()

    fun changeClubName(name: String) {
        _uiState.update {
            it.copy(
                clubSearchText = name
            )
        }
        if(name.isEmpty()) {
            getClubs()
        }
    }

    fun changeClubDivision(divisionDt: ClubDivisionDt?) {
        _uiState.update {
            it.copy(
                selectedDivision = divisionDt
            )
        }
        if(divisionDt == null) {
            getClubs()
        }
//
    }

    fun changeFavorite() {
        _uiState.update {
            it.copy(
                favorite = !uiState.value.favorite
            )
        }

        getClubs()
    }

    fun onApplyFilters() {
        getClubs()
    }




    fun bookmarkClub(clubId: Int) {

        viewModelScope.launch {

            val bookmarkRequestBody = ClubBookmarkRequestBody(
                userId = uiState.value.userAccount.id,
                clubId = clubId
            )

            try {
                val response = apiRepository.bookmarkClub(
                    token = uiState.value.userAccount.token,
                    clubBookmarkRequestBody = bookmarkRequestBody
                )

                if(response.isSuccessful) {
                    Log.i("bookmarkClub", "SUCCESS!!")
//                    _uiState.update {
//                        it.copy(
//                            divisions = response.body()?.data!!,
//                        )
//                    }
                } else {

                    val errorBodyString = response.errorBody()?.string()
                    Log.e("bookmarkClub", "ResponseErr: $errorBodyString")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("bookmarkClub", "Exception: $e")

            }
        }
    }



    private fun getClubs() {
//        _uiState.update {
//            it.copy(
//                loadingStatus = LoadingStatus.LOADING
//            )
//        }
        viewModelScope.launch {
            try {
               val response = apiRepository.getClubs(
                   token = uiState.value.userAccount.token,
                   clubName = uiState.value.clubSearchText,
                   divisionId = uiState.value.selectedDivision?.id,
                   favorite = uiState.value.favorite,
                   userId = uiState.value.userAccount.id
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubs = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    val errorBodyString = response.errorBody()?.string()
                    Log.e("getClubs", "ResponseErr: $errorBodyString")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getClubs", "Exception: $e")

            }
        }
    }

    private fun getDivisions() {

        viewModelScope.launch {
            try {
                val response = apiRepository.getAllLeagues(
                    token = uiState.value.userAccount.token,
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            divisions = response.body()?.data!!,
                        )
                    }
                } else {

                    val errorBodyString = response.errorBody()?.string()
                    Log.e("getLeagues", "ResponseErr: $errorBodyString")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getLeagues", "Exception: $e")

            }
        }
    }

    fun getClubsScreenUiData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getClubs()
            getDivisions()
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect { users ->
                    _uiState.update {
                        it.copy(
                            userAccount = if(users.isNotEmpty()) users[0] else UserAccount(0, "", "", "", "", "", "")
                        )
                    }
                }
            }
        }
    }

    init {
        getUser()
        getClubsScreenUiData()
    }
}