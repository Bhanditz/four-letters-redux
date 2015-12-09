package nz.bradcampbell.fourletters.redux.store.internal

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.reducer.RootReducer
import nz.bradcampbell.fourletters.redux.state.State
import nz.bradcampbell.fourletters.redux.store.Store
import rx.Observable
import rx.lang.kotlin.BehaviourSubject

class StoreImpl(val reducer: RootReducer) : Store {
    var state = State()
    val subject = BehaviourSubject<State>()

    init {
        subject.onNext(state)
    }

    override fun state() = state

    override fun asObservable(): Observable<State> {
        return subject.asObservable();
    }

    override fun dispatch(action: Action) {
        state = reducer.call(action, state)
        subject.onNext(state)
    }
}
