package nz.bradcampbell.fourletters.core.store

import rx.Observable

public interface Store<Action, State> {
    fun state(): State
    fun asObservable(): Observable<State>
    fun dispatch(action: Action)
}