package android.kotlin.bookstore.adapter

import android.content.Intent
import android.kotlin.bookstore.EditBukuActivity
import android.kotlin.bookstore.R
import android.kotlin.bookstore.model.DataItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class BukuAdapter(
    val listBuku: ArrayList<DataItem>,
    val listener: OnAdapterListener
): RecyclerView.Adapter<BukuAdapter.ViewHolder>(){
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
        var btnDelete: Button = itemView.findViewById(R.id.btnDelete)
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
            intent.putExtra("idBuku", data.id.toString())
            intent.putExtra("judulBuku", data.judul.toString())
            intent.putExtra("penulis", data.penulis.toString())
            intent.putExtra("rating", data.rating.toString())
            intent.putExtra("harga", data.harga.toString())
            holder.itemView.context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            listener.onDeleteBuku(data.id.toString())
        }
    }

    override fun getItemCount(): Int = listBuku.size

    interface OnAdapterListener {
        fun onDeleteBuku(idBuku: String)
    }
}