package android.kotlin.bookstore.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BukuResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("penulis")
	val penulis: String? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null
) : Parcelable
