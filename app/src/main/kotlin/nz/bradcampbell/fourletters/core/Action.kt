package nz.bradcampbell.fourletters.core

sealed class Action {
    object Init : Action()
    class InitGame(val word: Word) : Action()
    class NextGame(val word: Word, val bonusTime: Int) : Action()
    object LeftPressed : Action()
    object TopPressed : Action()
    object RightPressed : Action()
    object BottomPressed : Action()
    object Back : Action()
    object BumpScore: Action()
    object ResetGame: Action()
    object Tick: Action();
    class Navigate(val page: Page, val addToBackStack: Boolean = true) : Action()
}
