package br.com.ecoalert.report.ui

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.ecoalert.components.LoadingDialog
import br.com.ecoalert.databinding.ActivityReportBinding
import br.com.ecoalert.report.viewmodel.ReportViewModel

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.geographicCoordinate.observe(this) { binding.geographicInput.setText(it) }
        viewModel.errorMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setupListeners() {
        binding.getLocationButton.setOnClickListener { getLocation() }
    }

    private fun getLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        loadingDialog.show("Capturing geolocation...")

        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val coordinate = "${location.latitude}, ${location.longitude}"
                    viewModel.setGeographicCoordinate(coordinate)
                    loadingDialog.dismiss()
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@ReportActivity,
                        "Unable to fetch location. GPS is disabled.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            null
        )
    }
}
