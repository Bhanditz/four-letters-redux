package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.Page
import nz.bradcampbell.fourletters.redux.state.PaginationState
import nz.bradcampbell.fourletters.redux.state.State

class PaginateReducer: Reducer<Action, State> {
    override fun call(action: Action, state: State): State {
        return when (action) {
            is Action.Navigate -> {
                var newHistory = state.paginationState.history
                if (action.addToBackStack) {
                    newHistory += state.paginationState.currentPage
                }
                state.copy(PaginationState(action.page, newHistory))
            }
            is Action.Back -> {
                val newPage: Page = state.paginationState.history.last();
                val newHistory = state.paginationState.history.subList(0, state.paginationState.history.size - 1)
                state.copy(PaginationState(newPage, newHistory))
            }
            else -> state
        }
    }
}
