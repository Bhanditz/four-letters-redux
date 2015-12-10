package nz.bradcampbell.fourletters.ui.renderables

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.data.Clock
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.redux.state.State
import nz.bradcampbell.fourletters.ui.Renderable
import javax.inject.Inject

class TimeRemainingView(context: Context?, attrs: AttributeSet?) : ProgressBar(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator
    @Inject lateinit var clock: Clock

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

    override fun render(state: State) {
        animation?.removeAllListeners()
        animation?.cancel()

        val gameState = state.gameState!!
        val finishTime = gameState.finishTime
        val currentTime = clock.millis()
        val from = finishTime - currentTime

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
