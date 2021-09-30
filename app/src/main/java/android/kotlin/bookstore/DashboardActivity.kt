package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.model.*
import android.kotlin.bookstore.service.RetrofitClient
import android.kotlin.bookstore.service.RetrofitPengguna
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private val api by lazy { RetrofitPengguna().getService() }
    val userAdapter = UserAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        btnTambahUser.setOnClickListener{
            val intent = Intent(this, TambahUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getUserData()
    }

    private fun getUserData(){
        api.getUserList("get_pengguna").enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                if (response!!.isSuccessful){
                    response.body()?.let { tampilUser(it) }
                    Toast.makeText(this@DashboardActivity, "Daftar User", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@DashboardActivity, "Gagal mendapatkan daftar user", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                Toast.makeText(this@DashboardActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun tampilUser(dataUser : UserResponse){
        val result = dataUser.data
        userAdapter.setData(result as List<UserItem>)
    }
}