package br.com.ecoalert.utils

import androidx.recyclerview.widget.GridLayoutManager
import br.com.ecoalert.ui.home.MenuGridAdapter.MenuViewHolder

object RecyclerViewMarginGridUtils {
    fun adjustMargins(holder: MenuViewHolder, marginSize: Int) {
        val layoutParams = holder.itemView.layoutParams as GridLayoutManager.LayoutParams
        layoutParams.setMargins(marginSize, marginSize, marginSize, marginSize)
        holder.itemView.layoutParams = layoutParams
    }
}