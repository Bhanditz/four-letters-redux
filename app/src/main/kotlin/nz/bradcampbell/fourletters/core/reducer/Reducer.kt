package nz.bradcampbell.fourletters.core.reducer

interface Reducer<A, S> {
    fun call(action: A, appState: S) : S;
}
