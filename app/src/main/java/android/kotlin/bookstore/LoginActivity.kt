package android.kotlin.bookstore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlin.bookstore.model.UserResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.card_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val api by lazy { RetrofitClient.instanceUser }
    var checkLogin = false
    val KEY_USERID = "key.userid"
    val KEY_USERNAME = "key.username"
    val KEY_NAMA = "key.nama"
    val KEY_ALAMAT = "key.alamat"
    val KEY_EMAIL = "key.email"
    val KEY_GAMBAR = "key.gambar"
    val KEY_LOGIN = "key.login"
    val PERFS_NAME = "LOGIN"
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var deviceId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        // Init Biometric
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this,executor, object:BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Jika auth error, maka hentikan task
                tvStatusAuth.text = "Authentication error: $errString"
                Toast.makeText(this@LoginActivity,"Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Jika auth berhasil/sukses, maka cek apakah fingerprint/device id tersebut sudah terdaftar dalam database atau belum
                loginByBiometric()
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Auth failed, maka tampilkan pesan auth gagal/failed
                tvStatusAuth.text = "Auth failed!"
                Toast.makeText(this@LoginActivity,"Authentication failed!", Toast.LENGTH_SHORT).show()
            }
        })

        // Set properti title dan deskripsi dialog auth
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login Admin Book Store")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
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
        btnFingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    // Login biasa menggunakan username dan password
    private fun login(){
        pesan()
    }
    private fun pesan(){
        val editor = sharedPreferences.edit()
        val username: String = input_username.text.toString()
        val password: String = input_password.text.toString()
        when{
            username == "" ->{
                var pesan = Toast.makeText(applicationContext,"Masukkan username", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            password == "" ->{
                var pesan = Toast.makeText(applicationContext,"Masukkan password", Toast.LENGTH_LONG)
                pesan.setGravity(Gravity.TOP, 0, 140)
                pesan.show()
            }
            else -> {
                api.loginPengguna(username,password).enqueue(object : Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                        if (response!!.isSuccessful){
                            if (response.body()?.status == 1){
                                Toast.makeText(this@LoginActivity,"Login berhasil!", Toast.LENGTH_SHORT).show()
                                sendSharedPreferences(editor, response)
                            } else if (response.body()?.status == 0){
                                var pesan = Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG)
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
    // Login menggunakan fingerprint/biometric
    private fun loginByBiometric() {
        val editor = sharedPreferences.edit()
        api.loginByDevice(deviceId, "get_pengguna_hardware_id").enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                if (response!!.isSuccessful){
                    if (response.body()?.status == 1){
                        tvStatusAuth.text = "Auth Succeed..."
                        Toast.makeText(this@LoginActivity,"Login biometric berhasil!", Toast.LENGTH_SHORT).show()
                        sendSharedPreferences(editor, response)
                    } else if (response.body()?.status == 0){
                        tvStatusAuth.text = "Fingerprint (ID Device) Anda belum terdaftar! Silakan login terlebih dahulu!"
                        var pesan = Toast.makeText(applicationContext, "Gagal login dengan biometric!", Toast.LENGTH_LONG)
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
    private fun checkLoginByBiometric(checkSuccess: Boolean = false): Boolean {
        return checkSuccess
    }
    private fun sendSharedPreferences(editor: SharedPreferences.Editor, response: Response<UserResponse>){
        editor.putString(KEY_USERID,
            response.body()!!.data!![0]?.idUser
        )
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
    }
}