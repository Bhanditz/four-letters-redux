package nz.bradcampbell.fourletters.core

import nz.bradcampbell.fourletters.core.reducers.RootReducer
import rx.Observable
import rx.lang.kotlin.BehaviourSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Store @Inject constructor(val reducer: RootReducer) {
    var state = AppState()
    val subject = BehaviourSubject<AppState>()

    init {
        subject.onNext(state)
    }

    fun asObservable(): Observable<AppState> {
        return subject.asObservable();
    }

    fun dispatch(action: Action) {
        state = reducer.call(action, state)
        subject.onNext(state)
    }
}
