package android.kotlin.bookstore.service

import android.kotlin.bookstore.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImage {
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit() : Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getService(): UploadGambarApi = getRetrofit().create(UploadGambarApi::class.java)
}