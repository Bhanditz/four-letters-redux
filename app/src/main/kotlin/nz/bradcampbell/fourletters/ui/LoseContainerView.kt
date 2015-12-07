package nz.bradcampbell.fourletters.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.core.ActionCreator
import nz.bradcampbell.fourletters.core.AppState
import nz.bradcampbell.fourletters.core.Renderable
import javax.inject.Inject

class LoseContainerView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    var possibleAnswers: TextView? = null

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        possibleAnswers = findViewById(R.id.possibleWords) as TextView

        findViewById(R.id.playAgain).setOnClickListener {
            actionCreator.playAgain()
        }

        findViewById(R.id.menu).setOnClickListener {
            actionCreator.back()
        }
    }

    override fun render(appState: AppState) {
        possibleAnswers?.text = appState.gameState?.possibleAnswers?.joinToString()
    }
}