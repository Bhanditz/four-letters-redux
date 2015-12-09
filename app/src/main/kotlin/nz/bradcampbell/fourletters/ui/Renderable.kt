package nz.bradcampbell.fourletters.ui

import android.view.ViewGroup
import nz.bradcampbell.fourletters.redux.state.State

interface Renderable {
    fun render(state: State)
}

fun ViewGroup.dispatchRender(state: State) {
    for (i in 0..childCount - 1) {
        val v = getChildAt(i);
        if (v is Renderable) {
            v.render(state)
        }
        if (v is ViewGroup) {
            v.dispatchRender(state)
        }
    }
}
