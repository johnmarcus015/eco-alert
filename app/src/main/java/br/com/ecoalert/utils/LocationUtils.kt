package br.com.ecoalert.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationUtils {

    fun requestLocationPermisssion(activity: AppCompatActivity) {
        if (ContextCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
    }

    fun getCurrentCoordinates(
        activity: AppCompatActivity,
        onLocationChanged: (location: Location) -> Unit,
        onProviderDisabled: () -> Unit
    ) {
        val locationManager =
            activity.baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    onLocationChanged(location)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {
                    onProviderDisabled()
                }
            },
            null
        )
    }

    suspend fun getAddressFromCoordinate(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    val address = addresses[0]
                    "${address.getAddressLine(0)}"
                } else {
                    "Address not found"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Unable to get address: ${e.localizedMessage}"
            }
        }
    }

    fun formatLocation(location: Location): String {
        return "${location.latitude}, ${location.longitude}"
    }
}
