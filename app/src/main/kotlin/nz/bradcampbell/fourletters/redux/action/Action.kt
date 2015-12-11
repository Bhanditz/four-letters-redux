package nz.bradcampbell.fourletters.redux.action

import nz.bradcampbell.fourletters.data.Word
import nz.bradcampbell.fourletters.redux.state.Page

sealed class Action {
    class InitGame(val word: Word, val finishTime: Long) : Action()
    class NextGame(val word: Word, val points: Int, val bonusTime: Long): Action()
    object LoadWordError : Action()
    object DismissLoadWordError : Action()
    object LeftPressed : Action()
    object TopPressed : Action()
    object RightPressed : Action()
    object BottomPressed : Action()
    object Back : Action()
    object ResetGame: Action()
    class Navigate(val page: Page, val addToBackStack: Boolean = true) : Action()
}
