package nz.bradcampbell.fourletters.redux.store.internal

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.reducer.RootReducer
import nz.bradcampbell.fourletters.redux.state.AppState
import nz.bradcampbell.fourletters.redux.store.Store
import rx.Observable
import rx.lang.kotlin.BehaviourSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreImpl @Inject constructor(val reducer: RootReducer) : Store<Action, AppState> {
    var state = AppState()
    val subject = BehaviourSubject<AppState>()

    init {
        subject.onNext(state)
    }

    override fun state() = state

    override fun asObservable(): Observable<AppState> {
        return subject.asObservable();
    }

    override fun dispatch(action: Action) {
        state = reducer.call(action, state)
        subject.onNext(state)
    }
}
