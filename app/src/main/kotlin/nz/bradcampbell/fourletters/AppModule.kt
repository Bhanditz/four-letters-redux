package nz.bradcampbell.fourletters

import android.app.Application
import dagger.Module
import dagger.Provides
import nz.bradcampbell.fourletters.data.Clock
import nz.bradcampbell.fourletters.data.WordRepository
import nz.bradcampbell.fourletters.data.internal.WordRepositoryImpl
import nz.bradcampbell.fourletters.redux.reducer.RootReducer
import nz.bradcampbell.fourletters.redux.store.internal.StoreImpl
import nz.bradcampbell.fourletters.redux.store.Store
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideClock(): Clock {
        return object : Clock {}
    }

    @Provides
    @Singleton
    fun provideStore(rootReducer: RootReducer): Store {
        return StoreImpl(rootReducer)
    }

    @Provides
    @Singleton
    fun provideWordRepository(application: Application): WordRepository {
        return WordRepositoryImpl(application)
    }
}
