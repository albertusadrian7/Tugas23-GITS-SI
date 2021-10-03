package android.kotlin.bookstore.service

import android.net.Uri
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import com.google.gson.GsonBuilder

import com.google.gson.Gson

object RetrofitClient {
    const val BASE_URL = "http://192.168.43.84/bukuRestApi/"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: BukuApiInterface by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(BukuApiInterface::class.java)
    }

    val instanceUser: UserApiInterface by lazy{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        retrofit.create(UserApiInterface::class.java)
    }
}