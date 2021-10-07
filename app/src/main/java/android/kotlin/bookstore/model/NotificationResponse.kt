package android.kotlin.bookstore.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationResponse(

	@field:SerializedName("canonical_ids")
	val canonicalIds: Int? = null,

	@field:SerializedName("success")
	val success: Int? = null,

	@field:SerializedName("failure")
	val failure: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@field:SerializedName("multicast_id")
	val multicastId: Long? = null
) : Parcelable

@Parcelize
data class ResultsItem(

	@field:SerializedName("message_id")
	val messageId: String? = null
) : Parcelable
