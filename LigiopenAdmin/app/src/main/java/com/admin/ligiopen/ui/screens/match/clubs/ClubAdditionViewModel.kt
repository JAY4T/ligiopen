package com.admin.ligiopen.ui.screens.match.clubs

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.club.ClubAdditionRequest
import com.admin.ligiopen.data.network.repository.ApiRepository
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.time.LocalDate

class ClubAdditionViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ClubAdditionUiData())
    val uiState: StateFlow<ClubAdditionUiData> = _uiState.asStateFlow()


    fun onChangeClubName(name: String) {
        _uiState.update {
            it.copy(
                clubName = name
            )
        }
    }

    fun onChangeClubDesc(desc: String) {
        _uiState.update {
            it.copy(
                clubDescription = desc
            )
        }
    }

    fun onChangeCountry(country: String) {
        _uiState.update {
            it.copy(
                country = country
            )
        }
    }

    fun onChangeCounty(county: String) {
        _uiState.update {
            it.copy(
                county = county
            )
        }
    }

    fun onChangeTown(town: String) {
        _uiState.update {
            it.copy(
                town = town
            )
        }
    }

    fun onUploadClubLogo(logo: Uri) {
        _uiState.update {
            it.copy(
                clubLogo = logo
            )
        }
    }

    fun onRemoveClubLogo() {
        _uiState.update {
            it.copy(
                clubLogo = null
            )
        }
    }

    fun changeDate(date: LocalDate) {
        _uiState.update {
            it.copy(
                startedOn = date
            )
        }
    }

    fun onAddClub(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val clubAdditionRequest = ClubAdditionRequest(
                   name = uiState.value.clubName,
                   description = uiState.value.clubDescription,
                   startedOn = uiState.value.startedOn.toString(),
                   country = uiState.value.country,
                   county = uiState.value.county,
                   town = uiState.value.town
               )

                var logo: MultipartBody.Part? = null
                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uiState.value.clubLogo!!, "r", null)
                parcelFileDescriptor?.let { pfd ->
                    val inputStream = FileInputStream(pfd.fileDescriptor)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int
                    while(inputStream.read(buffer).also { length = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, length)
                    }
                    val byteArray = byteArrayOutputStream.toByteArray()

                    //Get the MIME type of the file

                    val mimeType = context.contentResolver.getType(uiState.value.clubLogo!!)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                    val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
                    logo = imagePart
                }

                val response = apiRepository.addClub(
                    token = uiState.value.userAccount.token,
                    clubAdditionRequest = clubAdditionRequest,
                    logo = logo!!
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }
                    Log.e("addClub", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }
                Log.e("addClub", "e: $e")
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect {users ->
                    _uiState.update {
                        it.copy(
                            userAccount = userAccountDt.takeIf { users.isEmpty() } ?: users[0]
                        )
                    }
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL,
            )
        }
    }

    init {
        loadUserData()
    }
}