package nz.bradcampbell.fourletters

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Application {
        return application
    }
}
