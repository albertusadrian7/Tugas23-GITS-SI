package android.kotlin.bookstore

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlin.bookstore.model.UserResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.card_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val api by lazy { RetrofitClient.instanceUser }
    var checkLogin = false
    val KEY_USERNAME = "key.username"
    val KEY_NAMA = "key.nama"
    val KEY_ALAMAT = "key.alamat"
    val KEY_EMAIL = "key.email"
    val KEY_GAMBAR = "key.gambar"
    val KEY_LOGIN = "key.login"
    val PERFS_NAME = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(PERFS_NAME, Context.MODE_PRIVATE)
        checkLogin = sharedPreferences.getBoolean(KEY_LOGIN,false)
        if (checkLogin){
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        signin_button.setOnClickListener {
            login()
        }
        register_button.setOnClickListener {
            val intent = Intent(this, TambahUserActivity::class.java)
            startActivity(intent)
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get FCM token
            val token = task.result
            token?.let { Log.d(TAG, it) }
            Toast.makeText(baseContext, "Token saat ini: $token", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Token saat ini: $token")
        })
    }

    private fun login(){
        pesan()
    }


    private fun pesan(){
        val editor = sharedPreferences.edit()
        val username: String = input_username.text.toString()
        val password: String = input_password.text.toString()
        when{
            username == "" ->{
                val pesan = Toast.makeText(applicationContext,"Masukkan username", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            password == "" ->{
                val pesan = Toast.makeText(applicationContext,"Masukkan password", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            else -> {
                api.loginPengguna(username,password).enqueue(object : Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                        if (response!!.isSuccessful){
                            if (response.body()?.status == 1){
                                val pesan = Toast.makeText(applicationContext, "Login berhasil!", Toast.LENGTH_LONG)
                                pesan.setGravity(Gravity.TOP,0,140)
                                pesan.show()
                                editor.putString(KEY_USERNAME,
                                    response.body()!!.data!![0]?.username
                                )
                                editor.putString(KEY_NAMA,
                                    response.body()!!.data!![0]?.nama
                                )
                                editor.putString(KEY_EMAIL,
                                    response.body()!!.data!![0]?.email
                                )
                                editor.putString(KEY_ALAMAT,
                                    response.body()!!.data!![0]?.alamat
                                )
                                editor.putString(KEY_GAMBAR,
                                    response.body()!!.data!![0]?.gambar
                                )
                                editor.putBoolean(KEY_LOGIN,true)
                                editor.apply()
                                val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (response.body()?.status == 0){
                                val pesan = Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG)
                                pesan.setGravity(Gravity.TOP,0,140)
                                pesan.show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Respon gagal", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                        Toast.makeText(this@LoginActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

}