package uz.napa.eyecaria.repository

import okhttp3.MultipartBody
import retrofit2.http.Part
import uz.napa.eyecaria.network.Api

class Repository
constructor(
    private val api: Api
) {
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ) = api.uploadImage(image)
}