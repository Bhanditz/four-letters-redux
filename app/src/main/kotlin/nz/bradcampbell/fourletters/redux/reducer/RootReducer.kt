package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.State
import javax.inject.Inject

class RootReducer @Inject constructor() : Reducer<Action, State> {
    val paginateReducer = PaginateReducer()
    val gameReducer = GameReducer()
    val menuReducer = MenuReducer()

    override fun call(action: Action, state: State): State {
        var result = paginateReducer.call(action, state)
        result = gameReducer.call(action, result)
        result = menuReducer.call(action, result)
        return result;
    }
}
