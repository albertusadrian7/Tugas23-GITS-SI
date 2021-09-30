package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.adapter.BukuAdapter
import android.kotlin.bookstore.model.BukuResponse
import android.kotlin.bookstore.model.DataItem
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_buku.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnTambah
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.card_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance }
    val bukuAdapter = BukuAdapter(arrayListOf(), object : BukuAdapter.OnAdapterListener{
        override fun onDeleteBuku(idBuku: String) {
            api.deleteBuku(idBuku,"delete_buku")
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response!!.isSuccessful){
                            if (response.body()?.status == 1){
                                val toast = Toast.makeText(this@MainActivity, "Berhasil delete buku!", Toast.LENGTH_SHORT)
                                toast.show()
                                getBukuData()
                            }
                        } else {
                            val toast = Toast.makeText(this@MainActivity, "Gagal delete buku", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        val toast = Toast.makeText(this@MainActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                })
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerBuku.layoutManager = LinearLayoutManager(this)
        recyclerBuku.adapter = bukuAdapter
        onStart()
        btnTambah.setOnClickListener{
            val intent = Intent(this, TambahBukuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getBukuData()
    }

    private fun getBukuData(){
        api.getBukuList("get_buku").enqueue(object: Callback<BukuResponse> {
            override fun onResponse(call: Call<BukuResponse>?, response: Response<BukuResponse>?) {
                if (response!!.isSuccessful){
                    response.body()?.let { tampilBuku(it) }
                    Toast.makeText(this@MainActivity, "Daftar Buku", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Gagal mendapatkan daftar buku", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<BukuResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Tidak ada respon $t", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun tampilBuku(dataBuku : BukuResponse){
        val result = dataBuku.data
        bukuAdapter.setData(result as List<DataItem>)
    }

}