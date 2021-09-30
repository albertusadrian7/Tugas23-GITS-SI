package android.kotlin.bookstore

import android.kotlin.bookstore.model.UserItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(val listUser: ArrayList<UserItem>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    fun setData(data : List<UserItem>){
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val data = listUser[position]
        val IMAGE_BASE = "http://192.168.43.84/bukurestapi/user_img/"
        holder.nama.text = data.nama.toString()
        holder.username.text = data.username.toString()
        holder.email.text = data.email.toString()
        holder.alamat.text = data.alamat.toString()
        Glide.with(holder.itemView).load(IMAGE_BASE + data.gambar).into(holder.gambarUser)
//        holder.pilihBuku.setOnClickListener{
//            val intent = Intent(holder.itemView.context, EditBukuActivity::class.java)
//            intent.putExtra("idBuku", data.id.toString())
//            intent.putExtra("judulBuku", data.judul.toString())
//            intent.putExtra("penulis", data.penulis.toString())
//            intent.putExtra("rating", data.rating.toString())
//            intent.putExtra("harga", data.harga.toString())
//            holder.itemView.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nama: TextView = itemView.findViewById(R.id.nama)
        var username: TextView = itemView.findViewById(R.id.username)
        var alamat: TextView = itemView.findViewById(R.id.alamat)
        var email: TextView = itemView.findViewById(R.id.email)
        var gambarUser: ImageView = itemView.findViewById(R.id.gambarUser)
//        var pilihUser: CardView = itemView.findViewById(R.id.card_user)
    }

}