package nz.bradcampbell.fourletters.core.reducer

import nz.bradcampbell.fourletters.core.action.Action
import nz.bradcampbell.fourletters.core.state.AppState
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
