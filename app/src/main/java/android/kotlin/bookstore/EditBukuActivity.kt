package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.model.BukuResponse
import android.kotlin.bookstore.model.DataItem
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_buku.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.card_buku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditBukuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_buku)
        inputEditId.setText(intent.getStringExtra("idBuku"))
        inputEditJudul.setText(intent.getStringExtra("judulBuku"))
        inputEditHarga.setText(intent.getStringExtra("penulis"))
        inputEditPenulis.setText(intent.getStringExtra("rating"))
        inputEditRating.setText(intent.getStringExtra("harga"))
        btnEdit.setOnClickListener {
            btnEdit.setOnClickListener {
                RetrofitClient.instance.updateBuku(
                    inputId.text.toString().trim(),
                    inputJudul.text.toString().trim(),
                    inputPenulis.text.toString().trim(),
                    inputRating.text.toString().trim(),
                    inputHarga.text.toString().trim(),
                    "update_buku"
                ).enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()?.status == 1) {
                                Toast.makeText(
                                    this@EditBukuActivity,
                                    "Berhasil Edit Buku",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@EditBukuActivity,
                                    "Gagal edit buku",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(
                            this@EditBukuActivity,
                            "Tidak ada respon",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
    }

}