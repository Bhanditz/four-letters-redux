package nz.bradcampbell.fourletters.core.reducers

interface Reducer<A, S> {
    fun call(action: A, appState: S) : S;
}
