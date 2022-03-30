package com.mdr.food.presentation

import android.app.Application
import com.mdr.food.di.KOIN_MODULES
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(KOIN_MODULES)
        }
    }
}