package br.com.ecoalert.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import br.com.ecoalert.R
import br.com.ecoalert.databinding.ActivityHomeBinding
import br.com.ecoalert.ui.report.ReportActivity
import br.com.ecoalert.utils.BrowserUtils
import br.com.ecoalert.utils.PhoneUtils

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val menuItems = listOf(
            MenuItem("Realizar denúncia de queimada", R.drawable.ic_report, ::openReportOptions),
            MenuItem("Visualizar cartilha educativa", R.drawable.ic_book, ::openEducationalGuide),
            MenuItem(
                "Visualizar pontos de incêndio no mapa",
                R.drawable.ic_map,
                ::viewFirePointsOnMap
            ),
            MenuItem(
                "Cadastrar dados para notificações na Defesa Civil",
                R.drawable.ic_notification,
                ::registerNotificationData
            )
        )

        val adapter = MenuGridAdapter(menuItems)
        binding.menuGridRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.menuGridRecyclerView.adapter = adapter
    }

    private fun openReportOptions() {
        val options = arrayOf("Ligar para 199", "Ligar para (31) 3594-1201", "Enviar email")
        val actions = arrayOf(
            { PhoneUtils.dialPhoneNumber(this, "199") },
            { PhoneUtils.dialPhoneNumber(this, "(31) 3594-1201") },
            { sendEmail() }
        )

        // Show dialog to choose an option
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Opções de Denúncia")
            .setItems(options) { _, which -> actions[which].invoke() }
            .create()

        dialog.show()
    }

    private fun openEducationalGuide() {
        BrowserUtils.openWebsite(this, "https://www.gov.br/ibama/pt-br/acesso-a-informacao/perguntas-frequentes/incendios-florestais#participar-campanha")
    }

    private fun viewFirePointsOnMap() {
        BrowserUtils.openWebsite(this, "https://terrabrasilis.dpi.inpe.br/queimadas/bdqueimadas/")
    }

    private fun registerNotificationData() {
        BrowserUtils.openWebsite(this, "https://www.defesacivil.br/register")
    }

    private fun sendEmail() {
        ReportActivity.open(this)
    }
}
