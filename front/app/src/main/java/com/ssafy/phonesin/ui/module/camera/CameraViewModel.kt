package com.ssafy.phonesin.ui.module.camera

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssafy.phonesin.ApplicationClass.Companion.prefs
import com.ssafy.phonesin.model.Event
import com.ssafy.phonesin.model.PhotoResponse
import com.ssafy.phonesin.network.NetworkResponse
import com.ssafy.phonesin.repository.ytwok.Y2KRepository
import com.ssafy.phonesin.ui.util.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "CameraViewModel"

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: Y2KRepository,
    private val application: Application
) : BaseViewModel() {

    private val _msg = MutableLiveData<Event<String>>()
    val errorMsg: LiveData<Event<String>> = _msg

    private val _photoResponse = MutableLiveData<Event<PhotoResponse>>()
    val photoResponse: LiveData<Event<PhotoResponse>> = _photoResponse

    private val _printCount = MutableLiveData<Int>(getPrintCountFromPrefs())
    val printCount: LiveData<Int> = _printCount

    private val _photoPaths = MutableLiveData<List<String>>()
    val photoPaths: LiveData<List<String>> = _photoPaths

    private val _selectedFrameColor = MutableLiveData<Int>()
    val selectedFrameColor: LiveData<Int> = _selectedFrameColor

    private val _selectedImagePaths = MutableLiveData<List<String>>()
    val selectedImagePaths: LiveData<List<String>> = _selectedImagePaths


    fun setSelectedFrameColor(color: Int) {
        _selectedFrameColor.value = color
    }

    fun setSelectedImagePaths(paths: List<String>) {
        _selectedImagePaths.value = paths
    }

    fun updatePhotoPaths(paths: List<String>) {
        _photoPaths.value = paths
    }

    fun getPrintCountFromPrefs(): Int {
        return prefs.getInt("PRINT_COUNT", 0)
    }

    private fun savePrintCountToPrefs(count: Int) {
        with(prefs.edit()) {
            putInt("PRINT_COUNT", count)
            apply()
        }
    }

    fun increasePrintCount() {
        val newCount = (_printCount.value ?: 0) + 1
        _printCount.value = newCount
        savePrintCountToPrefs(newCount)
    }

    fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            val requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                imageFile
            )

            val body = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestFile
            )

            val response = repository.uploadImage(body)
            Log.d(TAG, "uploadImage: $response")

            val type = "y2k 사진"
            when (response) {
                is NetworkResponse.Success -> {
                    _photoResponse.postValue(Event(response.body))
                }

                is NetworkResponse.ApiError -> {
                    _msg.postValue(postValueEvent(0, type))
                }

                is NetworkResponse.NetworkError -> {
                    _msg.postValue(postValueEvent(1, type))
                }

                is NetworkResponse.UnknownError -> {
                    _msg.postValue(postValueEvent(2, type))
                }
            }
        }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val resolver = application.contentResolver
            val mimeType = resolver.getType(uri)
            val inputStream = resolver.openInputStream(uri)

            val requestFile =
                inputStream?.let { RequestBody.create(MediaType.parse(mimeType), it.readBytes()) }
            val body = requestFile?.let {
                MultipartBody.Part.createFormData(
                    "file",
                    getFileName(uri),
                    it
                )
            }

            val response = body?.let { repository.uploadImage(it) }
            Log.d(TAG, "uploadImage: $response")

            val type = "y2k 사진"
            when (response) {
                is NetworkResponse.Success -> {
                    _photoResponse.postValue(Event(response.body))
                }

                is NetworkResponse.ApiError -> {
                    _msg.postValue(postValueEvent(0, type))
                }

                is NetworkResponse.NetworkError -> {
                    _msg.postValue(postValueEvent(1, type))
                }

                is NetworkResponse.UnknownError -> {
                    _msg.postValue(postValueEvent(2, type))
                }
                else -> {
                    _msg.postValue(postValueEvent(2, type))
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor =
                application.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result ?: "unknown"
    }
}