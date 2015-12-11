package nz.bradcampbell.fourletters.ui.renderables

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.redux.state.State
import nz.bradcampbell.fourletters.ui.Renderable
import javax.inject.Inject

class MenuContainerView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    private var errorMessage: AlertDialog? = null

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById(R.id.playButton).setOnClickListener {
            actionCreator.initiateGame()
        }
    }

    override fun render(state: State) {
        val menuState = state.menuState;
        showErrorDialogIfNeeded(menuState.wordErrorDisplayed)
    }

    private fun showErrorDialogIfNeeded(loadWordError : Boolean) {
        if (loadWordError && errorMessage?.isShowing ?: true) {
            val alert = AlertDialog.Builder(context)
                .setMessage(R.string.error_loading_word)
                .setPositiveButton(android.R.string.ok, { d, i -> actionCreator.dismissWordLoadError() })
                .setOnCancelListener { actionCreator.dismissWordLoadError() }
                .create()
            alert.show()
            errorMessage = alert
        }
    }
}
