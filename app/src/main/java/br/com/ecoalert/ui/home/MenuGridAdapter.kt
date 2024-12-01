package br.com.ecoalert.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ecoalert.databinding.ItemMenuBinding
import br.com.ecoalert.utils.RecyclerViewMarginGridUtils

data class MenuItem(val title: String, val iconRes: Int, val action: () -> Unit)

class MenuGridAdapter(private val items: List<MenuItem>) :
    RecyclerView.Adapter<MenuGridAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(items[position])
        RecyclerViewMarginGridUtils.adjustMargins(holder, MARGIN_SIZE)
    }

    override fun getItemCount(): Int = items.size

    inner class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            binding.menuIcon.setImageResource(item.iconRes)
            binding.menuTitle.text = item.title
            binding.root.setOnClickListener { item.action.invoke() }
        }
    }

    companion object {
        const val MARGIN_SIZE = 16
    }
}
