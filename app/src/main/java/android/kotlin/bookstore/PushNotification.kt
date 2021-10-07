package android.kotlin.bookstore

data class PushNotification(
    val data: NotificationData,
    val to: String
)