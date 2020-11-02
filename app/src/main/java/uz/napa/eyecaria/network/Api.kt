package uz.napa.eyecaria.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import uz.napa.eyecaria.model.EyeResponse

interface Api {

    @POST("upload/")
    @Multipart
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Response<EyeResponse>


}