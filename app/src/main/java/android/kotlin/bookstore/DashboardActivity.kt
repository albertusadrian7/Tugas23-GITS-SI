package android.kotlin.bookstore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlin.bookstore.Constants.Companion.BASE_URL
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.model.UserResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class DashboardActivity : AppCompatActivity() {
    val PERFS_NAME = "LOGIN"
    val KEY_USERID = "key.userid"
    val KEY_USERNAME = "key.username"
    val KEY_NAMA = "key.nama"
    val KEY_ALAMAT = "key.alamat"
    val KEY_EMAIL = "key.email"
    val KEY_GAMBAR = "key.gambar"
    val IMAGE_BASE = BASE_URL + "user_img/"
    private lateinit var userId: String
    private lateinit var deviceId: String
    private val api by lazy { RetrofitClient.instanceUser }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sharedPreferences = getSharedPreferences(PERFS_NAME, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(KEY_USERID, "").toString()
        var username = sharedPreferences.getString(KEY_USERNAME, "")
        var nama = sharedPreferences.getString(KEY_NAMA, "")
        var alamat = sharedPreferences.getString(KEY_ALAMAT, "")
        var email = sharedPreferences.getString(KEY_EMAIL,"")
        var gambar = sharedPreferences.getString(KEY_GAMBAR,"")
        var pathGambar = IMAGE_BASE + gambar
        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        namaUser.text = nama
        vw_username.text = username
        vw_alamat.text = alamat
        vw_email.text = email
        Toast.makeText(this,"Halaman Dashboard",Toast.LENGTH_SHORT).show()
        Glide.with(this).load(pathGambar).into(vw_imgUser)
        updatePenggunaHardware()
        logout.setOnClickListener {
            logout()
        }
        daftarBuku.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        daftarUser.setOnClickListener {
            val intent = Intent(this,UserActivity::class.java)
            startActivity(intent)
        }
        btnNotif.setOnClickListener {
            val intent = Intent(this,FormSeederActivity::class.java)
            startActivity(intent)
        }

    }

    // Menambahkan device id/hardware id ke dalam tabel pengguna
    private fun updatePenggunaHardware(){
        api.updateUserHardware(userId,deviceId,"update_pengguna_hardware")
            .enqueue(object: Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.body()?.status ==1){
                        Toast.makeText(this@DashboardActivity,"ID Device Anda: $deviceId",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity,"Gagal memperbarui id device Anda!",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity,"Tidak ada response: $t",Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun logout(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}