package android.kotlin.bookstore.service

import android.kotlin.bookstore.model.NotificationResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NotificationAPI {
//    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @FormUrlEncoded
    @POST("sendPushNotification.php")
    fun postNotification(
        @Field("fcm_token") fcm_token: String,
        @Field("title") title: String,
        @Field("message") message: String,
        @Query("function") function: String
    ): Call<NotificationResponse>
}