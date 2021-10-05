package android.kotlin.bookstore

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "android.kotlin.bookstore"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    // Token: cqc8a3cpRgKyCYyVgeLnR6:APA91bF2O8t-Lg24p5nACkcxXjMzI3rJpjbwiV2xMG22g5I0N7TudEpsVgnmrbbJCud0hp3W3iko5dVMs35H8MGLmGu1_M80E2ibaQ3VhOorCwDFm9jMPxr7c6SsNcRIhfixLAU6HsrV

    override fun onNewToken(token: String) {
        Log.d("TAG", "The token refreshed: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("android.kotlin.bookstore", R.layout.notification)
        remoteView.setTextViewText(R.id.notifTitle, title)
        remoteView.setTextViewText(R.id.notifMessage, message)
        return remoteView
    }

    // Menampilkan notifikasi
    private fun generateNotification(title: String, message: String){
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)
        // channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.novel)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(title,message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
             notificationManager.createNotificationChannel(notificationChannel)
             notificationManager.notify(0,builder.build())
        }
        notificationManager.notify(0,builder.build())
    }

}
