package android.kotlin.bookstore.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(

	@field:SerializedName("data")
	val data: List<UserItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class UserItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
) : Parcelable
