package android.kotlin.bookstore

import android.content.ContentValues
import android.kotlin.bookstore.model.NotificationResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_form_seeder.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TOPIC = "/topics/novel"
var TOKEN = ""
class FormSeederActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_seeder)
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
}