package nz.bradcampbell.fourletters.core

import android.view.ViewGroup

interface Renderable {
    fun render(appState: AppState)
}

fun ViewGroup.dispatchRender(appState: AppState) {
    for (i in 0..childCount - 1) {
        val v = getChildAt(i);
        if (v is Renderable) {
            v.render(appState)
        }
        if (v is ViewGroup) {
            v.dispatchRender(appState)
        }
    }
}
