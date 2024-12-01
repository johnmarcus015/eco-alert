package br.com.ecoalert

import android.app.Application
import br.com.ecoalert.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}