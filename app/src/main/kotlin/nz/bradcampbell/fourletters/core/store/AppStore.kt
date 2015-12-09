package nz.bradcampbell.fourletters.core.store

import nz.bradcampbell.fourletters.core.action.Action
import nz.bradcampbell.fourletters.core.reducer.RootReducer
import nz.bradcampbell.fourletters.core.state.AppState
import rx.Observable
import rx.lang.kotlin.BehaviourSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStore @Inject constructor(val reducer: RootReducer) : Store<Action, AppState> {
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
