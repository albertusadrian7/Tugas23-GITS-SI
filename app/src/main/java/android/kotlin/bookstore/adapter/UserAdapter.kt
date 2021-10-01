package android.kotlin.bookstore.adapter

import android.content.Intent
import android.kotlin.bookstore.EditUserActivity
import android.kotlin.bookstore.R
import android.kotlin.bookstore.model.UserItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter(
    val listUser: ArrayList<UserItem>,
    val listener: UserAdapter.OnAdapterListener
): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    fun setData(data : List<UserItem>){
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        val IMAGE_BASE = "http://192.168.43.84/bukurestapi/user_img/"
        val pathGambar = IMAGE_BASE + user.gambar
        holder.nama.text = user.nama.toString()
        holder.username.text = "@${user.username}"
        holder.email.text = user.email.toString()
        holder.alamat.text = user.alamat.toString()
        Glide.with(holder.itemView).load(pathGambar).apply(RequestOptions().override(75,75)).into(holder.gambarUser)
        holder.pilihUser.setOnClickListener{
            val intent = Intent(holder.itemView.context, EditUserActivity::class.java)
            intent.putExtra("idUser", user.idUser.toString())
            intent.putExtra("username", user.username.toString())
            intent.putExtra("password", user.password.toString())
            intent.putExtra("email", user.email.toString())
            intent.putExtra("nama", user.nama.toString())
            intent.putExtra("alamat", user.alamat.toString())
            intent.putExtra("namaGambar",user.gambar.toString())
            intent.putExtra("pathGambar", "$pathGambar")
            holder.itemView.context.startActivity(intent)
        }
        holder.btnDeleteUser.setOnClickListener {
            listener.onDeleteUser(user.idUser.toString())
        }
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nama: TextView = itemView.findViewById(R.id.nama)
        var username: TextView = itemView.findViewById(R.id.username)
        var alamat: TextView = itemView.findViewById(R.id.alamat)
        var email: TextView = itemView.findViewById(R.id.email)
        var gambarUser: ImageView = itemView.findViewById(R.id.gambarUser)
        var pilihUser: CardView = itemView.findViewById(R.id.card_user)
        var btnDeleteUser: Button = itemView.findViewById(R.id.btnDeleteUser)
    }

    interface OnAdapterListener {
        fun onDeleteUser(idUser: String)
    }

}