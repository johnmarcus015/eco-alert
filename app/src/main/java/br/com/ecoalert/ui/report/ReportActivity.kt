package br.com.ecoalert.ui.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ecoalert.components.LoadingDialog
import br.com.ecoalert.databinding.ActivityReportBinding
import br.com.ecoalert.utils.LocationUtils
import br.com.ecoalert.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var photosAdapter: PhotosAdapter

    private val imageCaptureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { viewModel.addPhoto(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter()
        binding.photoRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoRecyclerView.adapter = photosAdapter
    }

    private fun setupObservers() {
        viewModel.photos.observe(this) { photos -> photosAdapter.submitList(photos) }
        viewModel.geographicCoordinate.observe(this) { binding.geographicInput.setText(it) }
        viewModel.address.observe(this) { binding.addressInput.setText(it) }
        viewModel.errorMessage.observe(this) { message -> message?.let { showToast(it) } }
    }

    private fun setupListeners() {
        binding.getLocationButton.setOnClickListener { getLocation() }
        binding.addPhotoButton.setOnClickListener { imageCaptureLauncher.launch(null) }
        binding.sendButton.setOnClickListener {

            viewModel.setAddress(binding.addressInput.text.toString())
            viewModel.setReferencePoints(binding.referenceInput.text.toString())
            viewModel.setObservation(binding.observationInput.text.toString())

            val emailIntent = viewModel.sendEmail()
            emailIntent?.let {
                startActivity(Intent.createChooser(it, "Send Email"))
            }
        }
    }

    private fun getLocation() {

        LocationUtils.requestLocationPermisssion(this)

        loadingDialog.show("Capturando localização")

        LocationUtils.getCurrentCoordinates(this,
            onLocationChanged = { location ->
                viewModel.setGeographicCoordinate(LocationUtils.formatLocation(location))
                lifecycleScope.launch {
                    val address = LocationUtils.getAddressFromCoordinate(
                        this@ReportActivity,
                        location.latitude,
                        location.longitude
                    )
                    withContext(Dispatchers.Main) {
                        viewModel.setAddress(address)
                    }
                }
                loadingDialog.dismiss()
            },
            onProviderDisabled = {
                loadingDialog.dismiss()
                showToast("Unable to fetch location. GPS is disabled.")
            }
        )
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ReportActivity::class.java))
        }
    }
}
