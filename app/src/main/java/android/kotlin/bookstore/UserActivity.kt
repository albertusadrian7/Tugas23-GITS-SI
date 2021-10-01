package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.adapter.BukuAdapter
import android.kotlin.bookstore.adapter.UserAdapter
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.model.UserItem
import android.kotlin.bookstore.model.UserResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instanceUser }
    val userAdapter = UserAdapter(arrayListOf(),object : UserAdapter.OnAdapterListener{
        override fun onDeleteUser(idUser: String) {
            api.deleteUser(idUser,"delete_pengguna")
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response!!.isSuccessful){
                            if (response.body()?.status == 1){
                                val toast = Toast.makeText(this@UserActivity, "Berhasil delete user!", Toast.LENGTH_SHORT)
                                toast.show()
                                getUserData()
                            }
                        } else {
                            val toast = Toast.makeText(this@UserActivity, "Gagal delete user", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        val toast = Toast.makeText(this@UserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                })
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        recyclerUser.layoutManager = LinearLayoutManager(this)
        recyclerUser.adapter = userAdapter
        onStart()
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
                    Toast.makeText(this@UserActivity, "Daftar User", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@UserActivity, "Gagal mendapatkan daftar user", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                Toast.makeText(this@UserActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun tampilUser(dataUser : UserResponse){
        val result = dataUser.data
        userAdapter.setData(result as List<UserItem>)
    }
}