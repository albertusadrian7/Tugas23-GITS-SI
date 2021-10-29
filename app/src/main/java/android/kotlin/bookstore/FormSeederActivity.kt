package android.kotlin.bookstore

import android.app.*
import android.content.ContentValues
import android.content.Intent
import android.kotlin.bookstore.model.NotificationResponse
import android.kotlin.bookstore.service.RetrofitClient
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_form_seeder.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

const val TOPIC = "/topics/novel"
var TOKEN = ""
class FormSeederActivity : AppCompatActivity() {
    // Atribut untuk Alarm Notifikasi
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_seeder)
        createNotificationChannel()
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get FCM token
            val token = task.result
            token?.let { Log.d(ContentValues.TAG, it) }
            // Isi token device saat ini
            TOKEN = token.toString()
            Toast.makeText(baseContext, "Token saat ini: $token", Toast.LENGTH_LONG).show()
            Log.d(ContentValues.TAG, "Token saat ini: $token")
        })
        btnKirimNotifikasi.setOnClickListener {
            val title = titleMessage.text.toString()
            val message = bodyMessage.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message), TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }
        btnPickAlarm.setOnClickListener {
            // Memilih waktu
            pickAlarm()
        }
        btnSetAlarm.setOnClickListener {
            // Set Alarm Notifikasi
            setAlarm()
        }
    }

    private fun pickAlarm() {
        calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY,hour)
            calendar.set(Calendar.MINUTE, minute)
            jam.text = SimpleDateFormat("HH:mm").format(calendar.time)
        }
        TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show()
    }

    private fun sendNotification(notification: PushNotification) {
        RetrofitClient.instanceFirebase.postNotification(
            TOKEN,
            notification.data.title,
            notification.data.message,
            "sendPushNotification"
        ).enqueue(object : Callback<NotificationResponse>{
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response!!.isSuccessful){
                    if (response.body()?.success == 1){
                        Toast.makeText(this@FormSeederActivity, "Berhasil mengirim notifikasi!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@FormSeederActivity, "Gagal mengirim notifikasi!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Toast.makeText(this@FormSeederActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nama: CharSequence = "notifikasi_alarm"
            val description = "Channel untuk Notifikasi Alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notification_channel", nama, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent
        )
        Toast.makeText(this,"Alarm set successfuly",Toast.LENGTH_SHORT).show()
    }

}