package nz.bradcampbell.fourletters

import dagger.Component
import nz.bradcampbell.fourletters.ui.*
import nz.bradcampbell.fourletters.ui.renderables.GameContainerView
import nz.bradcampbell.fourletters.ui.renderables.LoseContainerView
import nz.bradcampbell.fourletters.ui.renderables.MenuContainerView
import nz.bradcampbell.fourletters.ui.renderables.TimeRemainingView
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