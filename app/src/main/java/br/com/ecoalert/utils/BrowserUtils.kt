package br.com.ecoalert.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

object BrowserUtils {
    fun openWebsite(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            val activityInfo = intent.resolveActivity(context.packageManager)
            if (activityInfo != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No browser found to open the link: $url", Toast.LENGTH_SHORT).show()
                Log.e("BrowserUtils", "No activity found to handle ACTION_VIEW for $url")
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error opening link: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            Log.e("BrowserUtils", "Failed to open link $url", e)
        }
    }
}

