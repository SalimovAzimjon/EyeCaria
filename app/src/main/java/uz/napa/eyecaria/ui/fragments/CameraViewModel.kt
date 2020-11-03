package uz.napa.eyecaria.ui.fragments

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import uz.napa.eyecaria.model.EyeResponse
import uz.napa.eyecaria.repository.Repository
import uz.napa.eyecaria.util.Resource
import java.io.File

class CameraViewModel
@ViewModelInject
constructor(private val repository: Repository) : ViewModel() {
    private val _eyeResponse = MutableLiveData<Resource<EyeResponse>>()
    val eyeResponse: LiveData<Resource<EyeResponse>> = _eyeResponse

    fun uploadImage(file: File) = viewModelScope.launch {
        val reqFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body =
            MultipartBody.Part.createFormData("scanner", file.name, reqFile)
        try {
            _eyeResponse.postValue(Resource.Loading())
            val response = repository.uploadImage(body)
            if (response.isSuccessful) {
                response.body()?.let {
                    _eyeResponse.postValue(Resource.Success(it))
                }
            } else
                _eyeResponse.postValue(Resource.Error(response.message()))
        } catch (e: Exception) {
            _eyeResponse.postValue(Resource.Error(e.message.toString()))

        }

    }
}