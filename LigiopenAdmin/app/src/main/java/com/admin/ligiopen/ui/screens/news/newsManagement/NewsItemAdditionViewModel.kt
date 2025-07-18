package com.admin.ligiopen.ui.screens.news.newsManagement

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.news.NewsItemAdditionRequestBody
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

class NewsItemAdditionViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(NewsItemAdditionUiData())
    val uiState: StateFlow<NewsItemAdditionUiData> = _uiState.asStateFlow()

    private var selectedClubs = mutableStateListOf<Int>()

    private val newsId: String? = savedStateHandle[NewsItemAdditionScreenDestination.newsId]

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

    fun changeParagraph(paragraph: String) {
        _uiState.update {
            it.copy(
                paragraph = paragraph
            )
        }
        enableButton()
    }

    fun changePhoto(photo: Uri) {
        _uiState.update {
            it.copy(
                photo = photo
            )
        }
        enableButton()
    }

    fun removePhoto() {
        _uiState.update {
            it.copy(
                photo = null
            )
        }
        enableButton()
    }


    fun publishNewsItem(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val newsItemAdditionRequestBody = NewsItemAdditionRequestBody(
                    title = uiState.value.title,
                    subtitle = uiState.value.subtitle,
                    paragraph = uiState.value.paragraph,
                    newsId = newsId!!.toInt(),
                )

                var photo: MultipartBody.Part? = null

                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uiState.value.photo!!, "r", null)
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

                    val mimeType = context.contentResolver.getType(uiState.value.photo!!)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                    val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
                    photo = imagePart
                }

                val response = apiRepository.publishNewsItem(
                    token = uiState.value.userAccount.token,
                    newsItemAdditionRequestBody = newsItemAdditionRequestBody,
                    photo = photo!!,
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
                buttonEnabled = uiState.value.title.isNotEmpty() && uiState.value.subtitle.isNotEmpty() && uiState.value.paragraph.isNotEmpty() && uiState.value.photo != null
            )
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
    }
}