package nz.bradcampbell.fourletters.redux.store

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.reducer.RootReducer
import nz.bradcampbell.fourletters.redux.state.State
import rx.Observable
import rx.lang.kotlin.BehaviourSubject

public interface Store {
    fun state() : State
    fun load(state: State)
    fun asObservable() : Observable<State>
    fun dispatch(action: Action)

    object DEFAULT : Store {
        val reducer = RootReducer()
        var state = State()
        val subject = BehaviourSubject<State>()

        init {
            subject.onNext(state)
        }

        override fun load(state: State) {
            this.state = state
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
}