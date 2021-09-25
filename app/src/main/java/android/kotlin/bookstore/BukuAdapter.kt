package android.kotlin.bookstore

import android.content.Intent
import android.kotlin.bookstore.model.DataItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BukuAdapter(private val listBuku: ArrayList<DataItem>): RecyclerView.Adapter<BukuAdapter.ViewHolder>(){
    fun setData(data : List<DataItem>){
        listBuku.clear()
        listBuku.addAll(data)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var judulBuku: TextView = itemView.findViewById(R.id.judulBuku)
        var penulis: TextView = itemView.findViewById(R.id.penulis)
        var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBuku)
        var harga: TextView = itemView.findViewById(R.id.harga)
        var pilihBuku: CardView = itemView.findViewById(R.id.card_buku)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_buku, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listBuku[position]
        val rating = data.rating
        holder.judulBuku.text = data.judul.toString()
        holder.penulis.text = data.penulis.toString()
        holder.harga.text = data.harga.toString()
        if (rating != null) {
            holder.ratingBar.rating = rating.toFloat()
        }
        holder.pilihBuku.setOnClickListener{
            val intent = Intent(holder.itemView.context, EditBukuActivity::class.java)
            intent.putExtra("idBuku", data.id)
            intent.putExtra("judulBuku", data.judul)
            intent.putExtra("penulis", data.penulis)
            intent.putExtra("rating", data.rating)
            intent.putExtra("harga", data.harga)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listBuku.size

}