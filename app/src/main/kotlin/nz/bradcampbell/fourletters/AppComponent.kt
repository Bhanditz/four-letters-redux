package nz.bradcampbell.fourletters

import dagger.Component
import nz.bradcampbell.fourletters.ui.GameContainerView
import nz.bradcampbell.fourletters.ui.LoseContainerView
import nz.bradcampbell.fourletters.ui.MainActivity
import nz.bradcampbell.fourletters.ui.MenuContainerView
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
    fun inject(activity: MainActivity)
    fun inject(menu: MenuContainerView)
    fun inject(game: GameContainerView)
    fun inject(lose: LoseContainerView)
}