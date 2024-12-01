package br.com.ecoalert.report.ui

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PhotosAdapter(private val photos: List<Bitmap>) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(150, 150)
        }
        return PhotoViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.imageView.setImageBitmap(photos[position])
    }

    override fun getItemCount(): Int = photos.size
}
