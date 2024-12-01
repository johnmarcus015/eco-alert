package br.com.ecoalert.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object PhoneUtils {
    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }
}