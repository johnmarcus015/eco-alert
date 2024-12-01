package br.com.ecoalert.di

import br.com.ecoalert.report.viewmodel.ReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ReportViewModel() }
}