package android.kotlin.bookstore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    val PERFS_NAME = "LOGIN"
    val KEY_USERNAME = "key.username"
    val KEY_NAMA = "key.nama"
    val KEY_ALAMAT = "key.alamat"
    val KEY_EMAIL = "key.email"
    val KEY_GAMBAR = "key.gambar"
    val IMAGE_BASE = RetrofitClient.BASE_URL + "user_img/"

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sharedPreferences = getSharedPreferences(PERFS_NAME, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(KEY_USERNAME, "")
        val nama = sharedPreferences.getString(KEY_NAMA, "")
        val alamat = sharedPreferences.getString(KEY_ALAMAT, "")
        val email = sharedPreferences.getString(KEY_EMAIL,"")
        val gambar = sharedPreferences.getString(KEY_GAMBAR,"")
        val pathGambar = IMAGE_BASE + gambar
        namaUser.setText(nama)
        vw_username.setText(username)
        vw_alamat.setText(alamat)
        vw_email.setText(email)
        Toast.makeText(this,"Berhasil Login!",Toast.LENGTH_SHORT).show()
        Toast.makeText(this,"Halaman Dashboard",Toast.LENGTH_SHORT).show()
        Glide.with(this).load(pathGambar).into(vw_imgUser)
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
    private fun logout(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}