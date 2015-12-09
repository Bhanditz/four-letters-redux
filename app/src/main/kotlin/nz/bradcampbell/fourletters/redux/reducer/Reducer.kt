package nz.bradcampbell.fourletters.redux.reducer

interface Reducer<A, S> {
    fun call(action: A, state: S) : S;
}
