package nz.bradcampbell.fourletters.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.core.ActionCreator
import nz.bradcampbell.fourletters.core.Renderable
import nz.bradcampbell.fourletters.core.AppState
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameContainerView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    var answerView: TextView? = null
    var score: TextView? = null
    var left: TextView? = null
    var top: TextView? = null
    var right: TextView? = null
    var bottom: TextView? = null
    var timeRemaining: TextView? = null

    private var sub: Subscription = Subscriptions.empty()

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        sub = Observable.interval(16, TimeUnit.MILLISECONDS)
            .onBackpressureDrop()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                actionCreator.tick()
            }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        sub.unsubscribe()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        answerView = findViewById(R.id.answer) as TextView
        score = findViewById(R.id.score) as TextView

        left = findViewById(R.id.leftLetter) as TextView
        top = findViewById(R.id.topLetter) as TextView
        right = findViewById(R.id.rightLetter) as TextView
        bottom = findViewById(R.id.bottomLetter) as TextView

        timeRemaining = findViewById(R.id.timeRemaining) as TextView

        left!!.setOnClickListener {
            actionCreator.leftLetterPressed()
        }
        top!!.setOnClickListener {
            actionCreator.topLetterPressed()
        }
        right!!.setOnClickListener {
            actionCreator.rightLetterPressed()
        }
        bottom!!.setOnClickListener {
            actionCreator.bottomLetterPressed()
        }
    }

    override fun render(appState: AppState) {
        answerView?.setTextIfNeeded(appState.gameState?.answer?.map { it.letter }.toString())
        left?.setTextIfNeeded(appState.gameState?.leftLetter?.letter.toString())
        top?.setTextIfNeeded(appState.gameState?.topLetter?.letter.toString())
        right?.setTextIfNeeded(appState.gameState?.rightLetter?.letter.toString())
        bottom?.setTextIfNeeded(appState.gameState?.bottomLetter?.letter.toString())
        score?.setTextIfNeeded(appState.gameState?.score.toString())
        timeRemaining?.setTextIfNeeded(appState.gameState?.timeRemaining.toString())
    }
}

fun TextView.setTextIfNeeded(text: String) {
    if (this.text != text) {
        this.text = text
    }
}
