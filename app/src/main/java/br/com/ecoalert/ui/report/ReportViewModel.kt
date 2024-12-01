package br.com.ecoalert.ui.report

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {

    private val _geographicCoordinate = MutableLiveData<String>()
    val geographicCoordinate: LiveData<String> = _geographicCoordinate

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _referencePoints = MutableLiveData<String>()
    val referencePoints: LiveData<String> = _referencePoints

    private val _observation = MutableLiveData<String>()
    val observation: LiveData<String> = _observation

    private val _photos = MutableLiveData<MutableList<Bitmap>>(mutableListOf())
    val photos: LiveData<MutableList<Bitmap>> = _photos

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setGeographicCoordinate(value: String) {
        _geographicCoordinate.value = value
    }

    fun setAddress(value: String) {
        _address.value = value
    }

    fun setReferencePoints(value: String) {
        _referencePoints.value = value
    }

    fun setObservation(value: String) {
        _observation.value = value
    }

    fun addPhoto(photo: Bitmap) {
        val updatedPhotos = _photos.value ?: mutableListOf()
        updatedPhotos.add(photo)
        _photos.value = updatedPhotos
    }

    fun validateData(): Boolean {
        if (geographicCoordinate.value.isNullOrEmpty()) {
            _errorMessage.value = "Coordenada geográfica é obrigatória"
            return false
        }
        if (address.value.isNullOrEmpty()) {
            _errorMessage.value = "Endereço é obrigatório"
            return false
        }
        if (referencePoints.value.isNullOrEmpty()) {
            _errorMessage.value = "Ponto de referência é obrigatório"
            return false
        }
        if (observation.value.isNullOrEmpty()) {
            _errorMessage.value = "Observação é obrigatório"
            return false
        }
        if (photos.value.isNullOrEmpty()) {
            _errorMessage.value = "Ao menos uma foto é obrigatória"
            return false
        }
        _errorMessage.value = null
        return true
    }

    fun sendEmail(): Intent? {
        if (!validateData()) return null
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("defesacivil@betim.mg.gov.br"))
            putExtra(Intent.EXTRA_SUBJECT, "Denúncia de Queimada")
            putExtra(
                Intent.EXTRA_TEXT,
                """
                Coordenada geográfica: ${geographicCoordinate.value}
                Endereço: ${address.value}
                Ponto de referência: ${referencePoints.value}
                Observação: ${observation.value}
                """.trimIndent()
            )
        }
        return emailIntent
    }
}
