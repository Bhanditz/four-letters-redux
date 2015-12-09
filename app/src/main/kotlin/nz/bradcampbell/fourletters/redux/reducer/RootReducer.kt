package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.AppState
import javax.inject.Inject

class RootReducer @Inject constructor() : Reducer<Action, AppState> {
    val paginateReducer = PaginateReducer()
    val gameReducer = GameReducer()

    override fun call(action: Action, appState: AppState): AppState {
        var result = paginateReducer.call(action, appState)
        result = gameReducer.call(action, result)
        return result;
    }
}
