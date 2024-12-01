package br.com.ecoalert.report.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ecoalert.databinding.ItemPhotoBinding

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    private val photos = mutableListOf<Bitmap>()

    fun submitList(newPhotos: List<Bitmap>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Bitmap) {
            binding.photoImageView.setImageBitmap(photo)
        }
    }
}
