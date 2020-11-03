package uz.napa.eyecaria

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object{
        lateinit var appInstance:App
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}