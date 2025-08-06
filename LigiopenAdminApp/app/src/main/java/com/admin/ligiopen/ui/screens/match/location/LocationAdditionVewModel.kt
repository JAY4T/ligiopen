package com.admin.ligiopen.ui.screens.match.location

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin.ligiopen.data.network.enums.LoadingStatus
import com.admin.ligiopen.data.network.models.match.location.LocationCreationRequest
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

class LocationAdditionVewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LocationAdditionUiData())
    val uiState: StateFlow<LocationAdditionUiData> = _uiState.asStateFlow()

    private val photos = mutableStateListOf<Uri>()

    fun changeVenueName(name: String) {
        _uiState.update {
            it.copy(
                venueName = name
            )
        }
        enableButton()
    }

    fun updateCounty(county: String) {
        _uiState.update {
            it.copy(
                county = county
            )
        }
        enableButton()
    }

    fun updateTown(town: String) {
        _uiState.update {
            it.copy(
                town = town
            )
        }
        enableButton()
    }

    fun uploadPhoto(photo: Uri) {
        photos.add(photo)
        _uiState.update {
            it.copy(
                photos = photos
            )
        }
        enableButton()
    }

    fun removePhoto(index: Int) {
        photos.removeAt(index)
        _uiState.update {
            it.copy(
                photos = photos
            )
        }
        enableButton()
    }

    fun addLocation(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
                val locationCreationRequest = LocationCreationRequest(
                    venueName = uiState.value.venueName,
                    country = uiState.value.country,
                    county = uiState.value.county,
                    town = uiState.value.town,
                )
                val response = apiRepository.createMatchLocation(
                    token = uiState.value.userAccount.token,
                    locationCreationRequest = locationCreationRequest
                )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            locationId = response.body()?.data?.locationId
                        )
                    }
                    uploadLocationPhotos(context)
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("createLocation", "response $response")
                }


            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("createLocation", "e $e")
            }
        }
    }

    private fun uploadLocationPhotos(context: Context) {
        val imageParts = ArrayList<MultipartBody.Part>()
        _uiState.value.photos.forEach { uri ->
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
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

                val mimeType = context.contentResolver.getType(uri)
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                val imagePart = MultipartBody.Part.createFormData("file", "upload.$extension", requestFile)
                imageParts.add(imagePart)
            }
        }

        viewModelScope.launch {
            try {
               val response = apiRepository.uploadMatchLocationPhotos(
                   token = uiState.value.userAccount.token,
                   locationId = uiState.value.locationId!!,
                   files = imageParts
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

                    Log.e("matchLocationPhotos", "response: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("matchLocationPhotos", "e: $e")

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

    private fun enableButton() {
        _uiState.update {
            it.copy(
                buttonEnabled = uiState.value.venueName.isNotEmpty() &&
                uiState.value.country.isNotEmpty() &&
                uiState.value.county.isNotEmpty() &&
                uiState.value.town.isNotEmpty() &&
                uiState.value.photos.isNotEmpty()
            )
        }
    }

    init {
        loadUserData()
    }
}