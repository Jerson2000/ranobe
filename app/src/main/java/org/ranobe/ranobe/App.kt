package org.ranobe.ranobe

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.ranobe.ranobe.di.appModule

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(appModule)
        }
    }
}