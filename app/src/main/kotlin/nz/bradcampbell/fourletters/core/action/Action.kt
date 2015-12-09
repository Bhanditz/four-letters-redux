package nz.bradcampbell.fourletters.core.action

import nz.bradcampbell.fourletters.core.data.Word
import nz.bradcampbell.fourletters.core.state.Page

sealed class Action {
    class InitGame(val word: Word, val finishTime: Long) : Action()
    class NextGame(val word: Word, val points: Int, val bonusTime: Long): Action()
    object LeftPressed : Action()
    object TopPressed : Action()
    object RightPressed : Action()
    object BottomPressed : Action()
    object Back : Action()
    object ResetGame: Action()
    class Navigate(val page: Page, val addToBackStack: Boolean = true) : Action()
}
