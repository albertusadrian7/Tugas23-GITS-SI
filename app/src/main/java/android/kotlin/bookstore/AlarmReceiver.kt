package android.kotlin.bookstore

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val builder = NotificationCompat.Builder(context!!,"notification_channel")
            .setSmallIcon(R.drawable.novel)
            .setContentTitle("Admin Book Store")
            .setContentText("Yuk cek update buku terbaru di aplikasi Book Store!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(100,builder.build())
    }
}