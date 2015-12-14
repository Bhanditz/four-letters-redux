package nz.bradcampbell.fourletters

import android.app.Application
import dagger.Module
import dagger.Provides
import nz.bradcampbell.fourletters.data.Clock
import nz.bradcampbell.fourletters.data.WordRepository
import nz.bradcampbell.fourletters.data.WordService
import nz.bradcampbell.fourletters.data.Random
import nz.bradcampbell.fourletters.data.internal.WordRepositoryImpl
import nz.bradcampbell.fourletters.data.internal.WordServiceImpl
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
        return Clock.REAL
    }

    @Provides
    @Singleton
    fun provideStore(): Store {
        return Store.DEFAULT
    }

    @Provides
    @Singleton
    fun provideWordService(application: Application): WordService {
        return WordServiceImpl(application)
    }

    @Provides
    @Singleton
    fun provideWordRepository(wordService: WordService, random: Random): WordRepository {
        return WordRepositoryImpl(wordService, random)
    }

    @Provides
    @Singleton
    fun provideRandom(): Random {
        return Random.REAL;
    }
}
