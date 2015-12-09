package nz.bradcampbell.fourletters.redux.store

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.State
import rx.Observable

public interface Store {
    fun state() : State
    fun asObservable() : Observable<State>
    fun dispatch(action: Action)
}