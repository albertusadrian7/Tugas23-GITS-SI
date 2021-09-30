package android.kotlin.bookstore

import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.RetrofitClient
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_buku.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditBukuActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_buku)
        inputEditId.setText(intent.getStringExtra("idBuku"))
        inputEditJudul.setText(intent.getStringExtra("judulBuku"))
        inputEditHarga.setText(intent.getStringExtra("penulis"))
        inputEditPenulis.setText(intent.getStringExtra("rating"))
        inputEditRating.setText(intent.getStringExtra("harga"))
        btnEdit.setOnClickListener {
            api.updateBuku(
                inputEditId.text.toString(),
                inputEditJudul.text.toString(),
                inputEditPenulis.text.toString(),
                inputEditRating.text.toString(),
                inputEditHarga.text.toString(),
                "update_buku"
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response!!.isSuccessful){
                        if (response.body()?.status == 1){
                            val toast = Toast.makeText(this@EditBukuActivity, "Berhasil edit buku!", Toast.LENGTH_SHORT)
                            toast.show()
                            finish()
                        }
                    } else {
                        val toast = Toast.makeText(this@EditBukuActivity, "Gagal edit buku", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    val toast = Toast.makeText(this@EditBukuActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
        }
    }
}