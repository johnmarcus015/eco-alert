package br.com.ecoalert.components

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import br.com.ecoalert.R
import br.com.ecoalert.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) {

    private val dialog: Dialog = Dialog(context).apply {
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show(message: String) {
        dialog.findViewById<TextView>(R.id.loadingMessage)?.text = message
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }
}
