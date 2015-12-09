package nz.bradcampbell.fourletters.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.core.ActionCreator
import nz.bradcampbell.fourletters.core.AppState
import nz.bradcampbell.fourletters.core.Renderable
import javax.inject.Inject

class TimeRemainingView(context: Context?, attrs: AttributeSet?) : ProgressBar(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    var animation: ValueAnimator? = null

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation?.removeAllListeners()
        animation?.cancel()
    }

    override fun render(appState: AppState) {
        animation?.removeAllListeners()
        animation?.cancel()

        val gameState = appState.gameState!!
        val finishTime = gameState.finishTime
        val currentTime = System.currentTimeMillis()
        val from = Math.min(finishTime - currentTime, ActionCreator.GAME_DURATION)

        max = ActionCreator.GAME_DURATION.toInt()

        val a = ValueAnimator.ofInt(from.toInt(), 0)
        a.interpolator = LinearInterpolator()
        a.setDuration(from)
        a.addUpdateListener {
            progress = it.animatedValue as Int
        }
        a.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                actionCreator.gameOver()
            }
        })

        a.start()
        animation = a
    }
}
