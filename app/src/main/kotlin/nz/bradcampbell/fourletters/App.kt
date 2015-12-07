package nz.bradcampbell.fourletters

import android.app.Application

class App : Application() {
    lateinit private var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getAppComponent() : AppComponent = appComponent
}