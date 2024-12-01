package br.com.ecoalert.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object BrowserUtils {
    fun openWebsite(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}
