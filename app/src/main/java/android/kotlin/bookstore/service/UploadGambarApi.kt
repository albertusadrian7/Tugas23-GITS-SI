package android.kotlin.bookstore.service

import android.kotlin.bookstore.model.UploadGambarResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadGambarApi {
    @Multipart
    @POST("uploadGambarApi.php?function=upload_gambar")
    fun uploadGambar(
        @Part file: MultipartBody.Part
    ): Call<UploadGambarResponse>
}