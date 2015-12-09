package nz.bradcampbell.fourletters

import android.app.Application
import dagger.Module
import dagger.Provides
import nz.bradcampbell.fourletters.core.action.Action
import nz.bradcampbell.fourletters.core.data.Clock
import nz.bradcampbell.fourletters.core.data.WordRepository
import nz.bradcampbell.fourletters.core.data.WordRepositoryImpl
import nz.bradcampbell.fourletters.core.reducer.RootReducer
import nz.bradcampbell.fourletters.core.state.AppState
import nz.bradcampbell.fourletters.core.store.AppStore
import nz.bradcampbell.fourletters.core.store.Store
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
    fun provideStore(rootReducer: RootReducer): Store<Action, AppState> {
        return AppStore(rootReducer)
    }

    @Provides
    @Singleton
    fun provideWordRepository(application: Application): WordRepository {
        return WordRepositoryImpl(application)
    }
}
