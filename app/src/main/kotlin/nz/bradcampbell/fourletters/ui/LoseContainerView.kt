package nz.bradcampbell.fourletters.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.core.action.ActionCreator
import nz.bradcampbell.fourletters.core.state.AppState
import nz.bradcampbell.fourletters.ui.Renderable
import javax.inject.Inject

class LoseContainerView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    var possibleAnswersView: TextView? = null
    var loseMessageView: TextView? = null

    var loseMessageTemplate: String? = null

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)

        loseMessageTemplate = context?.getString(R.string.lose_message)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        possibleAnswersView = findViewById(R.id.possibleWords) as TextView
        loseMessageView = findViewById(R.id.loseMessage) as TextView

        findViewById(R.id.playAgain).setOnClickListener {
            actionCreator.playAgain()
        }

        findViewById(R.id.menu).setOnClickListener {
            actionCreator.back()
        }
    }

    override fun render(appState: AppState) {
        loseMessageView?.text = loseMessageTemplate?.format(appState.gameState?.score)
        possibleAnswersView?.text = appState.gameState?.possibleAnswers?.joinToString()
    }
}