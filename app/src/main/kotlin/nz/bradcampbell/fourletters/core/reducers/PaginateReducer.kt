package nz.bradcampbell.fourletters.core.reducers

import nz.bradcampbell.fourletters.core.Action
import nz.bradcampbell.fourletters.core.Page
import nz.bradcampbell.fourletters.core.PaginationState
import nz.bradcampbell.fourletters.core.AppState

class PaginateReducer: Reducer<Action, AppState> {
    override fun call(action: Action, appState: AppState): AppState {
        return when (action) {
            is Action.Navigate -> {
                var newHistory = appState.paginationState.history
                if (action.addToBackStack) {
                    newHistory += appState.paginationState.currentPage
                }
                appState.copy(PaginationState(action.page, newHistory))
            }
            is Action.Back -> {
                val newPage: Page = appState.paginationState.history.last();
                val newHistory = appState.paginationState.history.subList(0, appState.paginationState.history.size - 1)
                appState.copy(PaginationState(newPage, newHistory))
            }
            else -> appState
        }
    }
}
