package android.kotlin.bookstore

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.kotlin.bookstore.model.UploadGambarResponse
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.service.RetrofitClient
import android.kotlin.bookstore.service.RetrofitImage
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.activity_tambah_user.*
import kotlinx.android.synthetic.main.activity_tambah_user.btnTambahUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions

class TambahUserActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    private var imageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_user)
        btnUploadGambar.setOnClickListener {
            pilihGambar()
        }
        btnTambahUser.setOnClickListener {
            insertUserWithImage(imageUri)
        }
    }

    private fun pilihGambar(){
        if(EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
        }else{
            // Menampilkan permission request saat belum mendapat permission dari user
            EasyPermissions.requestPermissions(
                this,
                "This application need your permission to access photo gallery.",
                991,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER ->{
                    imageUri = data?.data
                    userViewImg.setImageURI(imageUri)
                }
            }
        }
    }

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    private fun getPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, filePathColumn, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e(TAG, "getPathFromURI Exception : ${e.toString()}")
            ""
        } finally {
            cursor?.close()
        }
    }

    // Function untuk upload gambar ke server dan insert pengguna ke database
    private fun insertUserWithImage(imageUri: Uri?) {
        // Jika pengguna belum upload gambar, maka aplikasi akan menampilkan pesan
        if (imageUri == null){
            imageName = "user.png"
            insertUser()
        } else {
            // Upload gambar ke server
            val filePath = getPathFromURI(this, imageUri)
            val file = File(filePath)
            val mFile = RequestBody.create("multipart".toMediaTypeOrNull(), file)
            imageName = file.name
            val body: MultipartBody.Part = createFormData("file", imageName, mFile)
            RetrofitImage().getService().uploadGambar(
                body
            ).enqueue(object: Callback<UploadGambarResponse> {
                override fun onResponse(
                    call: Call<UploadGambarResponse>?,
                    response: Response<UploadGambarResponse>?
                ) {
                    if (response!!.isSuccessful){
                        if (response.body()?.status == 1){
                            Toast.makeText(this@TambahUserActivity, "Berhasil upload foto!", Toast.LENGTH_SHORT).show()
                            insertUser()
                        }
                    } else {
                        Toast.makeText(this@TambahUserActivity, "Gagal upload foto!", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<UploadGambarResponse>, t: Throwable) {
                    Toast.makeText(this@TambahUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun insertUser(){
        // Insert pengguna ke dalam database
        RetrofitClient.instanceUser.insertPengguna(
            "",
            inputUsername.text.toString().trim(),
            inputPassword.text.toString().trim(),
            inputEmail.text.toString().trim(),
            inputNama.text.toString().trim(),
            inputAlamat.text.toString().trim(),
            imageName.trim(),
            "insert_pengguna"
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response!!.isSuccessful){
                    if (response.body()?.status == 1){
                        inputNama.setText("")
                        inputAlamat.setText("")
                        inputEmail.setText("")
                        inputUsername.setText("")
                        inputPassword.setText("")
                        Toast.makeText(this@TambahUserActivity, "Berhasil menambah user!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@TambahUserActivity, "Gagal menambah user!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@TambahUserActivity, "Tidak ada respon $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

}