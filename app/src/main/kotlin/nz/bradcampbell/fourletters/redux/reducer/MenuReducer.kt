package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.MenuState
import nz.bradcampbell.fourletters.redux.state.State

class MenuReducer: Reducer<Action, State> {
    override fun call(action: Action, state: State): State {
        return when(action) {
            is Action.LoadWordError -> state.copy(menuState = MenuState(true))
            is Action.DismissLoadWordError -> state.copy(menuState = MenuState(false))
            else -> state
        }
    }
}
