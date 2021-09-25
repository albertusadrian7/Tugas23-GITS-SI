package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.model.BukuResponse
import android.kotlin.bookstore.model.DataItem
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.BukuApiInterface
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_edit_buku.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val bukuAdapter = BukuAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerBuku.layoutManager = LinearLayoutManager(this)
        getBukuData()
        btnTambah.setOnClickListener{
            val intent = Intent(this, TambahBukuActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getBukuData(){
        RetrofitClient.instance.getBukuList("get_buku").enqueue(object: Callback<BukuResponse> {
            override fun onResponse(call: Call<BukuResponse>?, response: Response<BukuResponse>?) {
                recyclerBuku.adapter = BukuAdapter(response?.body()?.data as ArrayList<DataItem>)
                if (response!!.isSuccessful){
                    response.body()?.let { tampilBuku(it) }
                    val toast = Toast.makeText(this@MainActivity, "Daftar Buku", Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    val toast = Toast.makeText(this@MainActivity, "Gagal memberikan response", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            override fun onFailure(call: Call<BukuResponse>?, t: Throwable?) {
                val toast = Toast.makeText(this@MainActivity, "Tidak ada respon $t", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }

    private fun tampilBuku(dataBuku : BukuResponse){
        val result = dataBuku.data
        bukuAdapter.setData(result as List<DataItem>)
    }


}