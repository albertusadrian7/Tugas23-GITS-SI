package android.kotlin.bookstore.service

import android.kotlin.bookstore.model.BukuResponse
import android.kotlin.bookstore.model.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface BukuApiInterface {
    @GET("bukuApi.php")
    fun getBukuList(
        @Query("function") function: String
    ): Call<BukuResponse>

    @FormUrlEncoded
    @POST("bukuApi.php")
    fun insertBuku(
        @Field("id") id: String,
        @Field("judul") judulBuku: String,
        @Field("penulis") penulis: String,
        @Field("rating") rating: String,
        @Field("harga") harga: String,
        @Query("function") function: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("bukuApi.php")
    fun updateBuku(
        @Field("id") id: String,
        @Field("judul") judulBuku: String,
        @Field("penulis") penulis: String,
        @Field("rating") rating: String,
        @Field("harga") harga: String,
        @Query("function") function: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("bukuApi.php")
    fun deleteBuku(
        @Field("id") id: String,
        @Query("function") function: String
    ): Call<DefaultResponse>
}