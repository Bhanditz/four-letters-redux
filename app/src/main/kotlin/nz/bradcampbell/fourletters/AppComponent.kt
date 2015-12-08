package nz.bradcampbell.fourletters

import dagger.Component
import nz.bradcampbell.fourletters.ui.*
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
    fun inject(activity: MainActivity)
    fun inject(menu: MenuContainerView)
    fun inject(game: GameContainerView)
    fun inject(lose: LoseContainerView)
    fun inject(timer: TimeRemainingView)
}