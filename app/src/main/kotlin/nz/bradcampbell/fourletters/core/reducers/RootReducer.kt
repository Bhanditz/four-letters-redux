package nz.bradcampbell.fourletters.core.reducers

import nz.bradcampbell.fourletters.core.Action
import nz.bradcampbell.fourletters.core.AppState
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
