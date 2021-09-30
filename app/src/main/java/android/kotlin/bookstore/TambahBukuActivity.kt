package android.kotlin.bookstore

import android.kotlin.bookstore.model.DataItem
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.activity_tambah_buku.btnTambah
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahBukuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_buku)
        btnTambah.setOnClickListener {
            RetrofitClient.instance.insertBuku(
                inputId.text.toString().trim(),
                inputJudul.text.toString().trim(),
                inputPenulis.text.toString().trim(),
                inputRating.text.toString().trim(),
                inputHarga.text.toString().trim(),
                "insert_buku"
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response!!.isSuccessful){
                        if (response.body()?.status == 1){
                            inputId.setText("")
                            inputJudul.setText("")
                            inputPenulis.setText("")
                            inputRating.setText("")
                            inputHarga.setText("")
                            val toast = Toast.makeText(this@TambahBukuActivity, "Berhasil menambah buku!", Toast.LENGTH_SHORT)
                            toast.show()
                            finish()
                        }
                    } else {
                        val toast = Toast.makeText(this@TambahBukuActivity, "Gagal menambah buku", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    val toast = Toast.makeText(this@TambahBukuActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
        }
    }
}