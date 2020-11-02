package uz.napa.eyecaria.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import uz.napa.eyecaria.network.RetrofitInstance

class Repository {
    private val api by lazy { RetrofitInstance.api }

    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ) = api.uploadImage(image)
}