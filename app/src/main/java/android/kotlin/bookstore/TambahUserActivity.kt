package android.kotlin.bookstore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.kotlin.bookstore.model.DefaultResponse
import android.kotlin.bookstore.model.UserResponse
import android.kotlin.bookstore.service.RetrofitClient
import android.kotlin.bookstore.service.RetrofitImage
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Attendees.query
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_tambah_buku.*
import kotlinx.android.synthetic.main.activity_tambah_user.*
import kotlinx.android.synthetic.main.activity_tambah_user.btnTambahUser
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI.create
import java.util.jar.Manifest
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody

class TambahUserActivity : AppCompatActivity() {
    private var pathFoto: String? = ""
    private var bitmap: Bitmap? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_user)
        btnUploadGambar.setOnClickListener {
            pilihGambar()
        }
//        btnTambahUser.setOnClickListener {
//            Toast.makeText(this, "Halo",Toast.LENGTH_LONG).show()
//
//        }
    }

    private fun pilihGambar(){
        Intent(Intent.ACTION_PICK).also{
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER ->{
                    imageUri = data?.data
                    userViewImg.setImageURI(imageUri)
                    btnTambahUser.setOnClickListener {
                        Toast.makeText(this, "Halo",Toast.LENGTH_LONG).show()
                        uploadImage(imageUri!!)
                    }
                }
            }
        }
    }

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString())
            return ""
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    private fun uploadImage(contentURI: Uri) {
        val filePath = getRealPathFromURI(this, contentURI)
        val file = File(filePath)
        val mFile = RequestBody.create("multipart".toMediaTypeOrNull(), file) //membungkus file ke dalam request body
        val body: MultipartBody.Part = createFormData("file_gambar", file.getName(), mFile)
        RetrofitImage().getService().uploadGambar(
            body,"upload_gambar"
        ).enqueue(object: Callback<DefaultResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<DefaultResponse>?,
                response: Response<DefaultResponse>?
            ) {
                if (response!!.isSuccessful){
                    if (response.body()?.status == 1){
                        val toast = Toast.makeText(this@TambahUserActivity, "Foto berhasil ditambahkan!", Toast.LENGTH_LONG)
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(this@TambahUserActivity, "Foto gagal ditambahkan!", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                val toast = Toast.makeText(this@TambahUserActivity, "Tidak ada respon $t", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }

}