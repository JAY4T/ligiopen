package com.admin.ligiopen.ui.screens.news.newsManagement

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsAdditionRequestBody
import com.admin.ligiopen.data.network.repository.ApiRepository
import com.admin.ligiopen.data.room.db.userAccountDt
import com.admin.ligiopen.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

class NewsAdditionViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(NewsAdditionUiData())
    val uiState: StateFlow<NewsAdditionUiData> = _uiState.asStateFlow()

    private var selectedClubs = mutableStateListOf<Int>()

    fun changeTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title
            )
        }
        enableButton()
    }

    fun changeSubTitle(subtitle: String) {
        _uiState.update {
            it.copy(
                subtitle = subtitle
            )
        }
        enableButton()
    }

    fun changePhoto(photo: Uri) {
        _uiState.update {
            it.copy(
                coverPhoto = photo
            )
        }
        enableButton()
    }

    fun removePhoto() {
        _uiState.update {
            it.copy(
                coverPhoto = null
            )
        }
        enableButton()
    }

    fun selectClub(clubId: Int) {
        selectedClubs.add(clubId)

        _uiState.update {
            it.copy(
                selectedClubIds = selectedClubs
            )
        }
        enableButton()
    }

    fun removeClub(clubId: Int) {
        selectedClubs.removeIf { it == clubId }

        _uiState.update {
            it.copy(
                selectedClubIds = selectedClubs
            )
        }
        enableButton()
    }

    private fun getClubs() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getClubs(
                    token = uiState.value.userAccount.token,
                    clubName = null,
                    divisionId = null,
                    userId = uiState.value.userAccount.id,
                    favorite = null,
                    status = null
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            clubs = response.body()?.data!!
                        )
                    }
                } else {
                    Log.e("loadClubsResponse", "response: $response")
                }

            } catch (e: Exception) {
                Log.e("loadClubsResponse", "e: $e")

            }
        }
    }

    fun publishNews(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val newsAdditionRequestBody = NewsAdditionRequestBody(
                    title = uiState.value.title,
                    subTitle = uiState.value.subtitle,
                    teamsInvolved = uiState.value.selectedClubIds,
                )

                var coverPhoto: MultipartBody.Part? = null

                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uiState.value.coverPhoto!!, "r", null)
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

                    val mimeType = context.contentResolver.getType(uiState.value.coverPhoto!!)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                    val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
                    coverPhoto = imagePart
                }

                val response = apiRepository.publishNews(
                    token = uiState.value.userAccount.token,
                    newsAdditionRequestBody = newsAdditionRequestBody,
                    coverPhoto = coverPhoto!!
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            publishedNews = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("publishNewsResponse", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("publishNewsResponse", "e: $e")

            }
        }
    }

    fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.title.isNotEmpty() && uiState.value.subtitle.isNotEmpty() && uiState.value.coverPhoto != null
            )
        }
    }

    private fun loadStartupData() {
        viewModelScope.launch {
            while (uiState.value.userAccount.id == 0) {
                delay(1000)
            }
            getClubs()
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
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    init {
        loadUserData()
        loadStartupData()
    }
}