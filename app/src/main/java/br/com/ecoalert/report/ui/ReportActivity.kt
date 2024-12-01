package br.com.ecoalert.report.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.ecoalert.databinding.ActivityReportBinding
import br.com.ecoalert.report.viewmodel.ReportViewModel

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding // Binding for the layout
    private val viewModel: ReportViewModel by viewModels()

    private val imageCaptureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { viewModel.addPhoto(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.geographicCoordinate.observe(this) { binding.geographicInput.setText(it) }
        viewModel.errorMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
        viewModel.photos.observe(this) { photos ->
            val adapter = PhotosAdapter(photos)
            binding.photoRecyclerView.adapter = adapter
        }
    }

    private fun setupListeners() {
        binding.getLocationButton.setOnClickListener {
            getLocation()
        }

        binding.addPhotoButton.setOnClickListener {
            imageCaptureLauncher.launch(null)
        }

        binding.sendButton.setOnClickListener {
            val emailIntent = viewModel.sendEmail()
            emailIntent?.let {
                startActivity(Intent.createChooser(it, "Send Email"))
            }
        }
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

        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
            val coordinate = "${location.latitude}, ${location.longitude}"
            viewModel.setGeographicCoordinate(coordinate)
        } ?: run {
            Toast.makeText(this, "Unable to fetch location. Please try again.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
