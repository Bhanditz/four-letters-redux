package nz.bradcampbell.fourletters.ui.renderables

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.redux.state.State
import nz.bradcampbell.fourletters.redux.state.Position
import nz.bradcampbell.fourletters.ui.Renderable
import javax.inject.Inject

class GameContainerView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    var answerView: TextView? = null
    var score: TextView? = null
    var left: TextView? = null
    var top: TextView? = null
    var right: TextView? = null
    var bottom: TextView? = null
    var timeRemaining: View? = null

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        answerView = findViewById(R.id.answer) as TextView
        score = findViewById(R.id.score) as TextView
        timeRemaining = findViewById(R.id.timeRemaining)

        val leftView = findViewById(R.id.leftLetter) as TextView
        leftView.setOnClickListener {
            actionCreator.leftLetterPressed()
        }
        left = leftView

        val topView = findViewById(R.id.topLetter) as TextView
        topView.setOnClickListener {
            actionCreator.topLetterPressed()
        }
        top = topView

        val rightView = findViewById(R.id.rightLetter) as TextView
        rightView.setOnClickListener {
            actionCreator.rightLetterPressed()
        }
        right = rightView

        val bottomView = findViewById(R.id.bottomLetter) as TextView
        bottomView.setOnClickListener {
            actionCreator.bottomLetterPressed()
        }
        bottom = bottomView
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0..childCount-1) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val w = r - l
        val h = b - t

        val answerView = this.answerView!!
        val scoreView = this.score!!
        val topView = this.top!!
        val topViewTopMargin = (topView.layoutParams as MarginLayoutParams).topMargin
        val leftView = this.left!!
        val rightView = this.right!!
        val bottomView = this.bottom!!
        val timeRemainingView = this.timeRemaining!!
        val bottomViewTopMargin = (timeRemainingView.layoutParams as MarginLayoutParams).topMargin

        val contentHeight = answerView.measuredHeight +
                scoreView.measuredHeight +
                topView.measuredHeight +
                topViewTopMargin +
                leftView.measuredHeight +
                bottomView.measuredHeight +
                timeRemainingView.measuredHeight +
                bottomViewTopMargin

        val answerViewTop = (h - contentHeight) / 2 - answerView.measuredHeight

        // Layout answer view at the top middle
        answerView.layout(w / 2 - answerView.measuredWidth / 2, answerViewTop,
            w / 2 + answerView.measuredWidth / 2, answerViewTop + answerView.measuredHeight)

        // Layout score below answer
        scoreView.layout(w / 2 - scoreView.measuredWidth / 2, answerView.bottom,
            w / 2 + scoreView.measuredWidth / 2, answerView.bottom + scoreView.measuredHeight)

        // Layout top button below score
        topView.layout(w / 2 - topView.measuredWidth / 2, scoreView.bottom + topViewTopMargin,
            w / 2 + topView.measuredWidth / 2, scoreView.bottom + topView.measuredHeight + topViewTopMargin)

        // Layout left below top on the left side
        leftView.layout(topView.left - leftView.measuredWidth, topView.bottom, topView.left,
            topView.bottom + leftView.measuredHeight)

        // Layout right below top on the right side
        rightView.layout(topView.right, topView.bottom, topView.right + rightView.measuredWidth,
            topView.bottom + rightView.measuredHeight)

        // Layout bottom view below the left/right views in the middle
        bottomView.layout(w / 2 - bottomView.measuredWidth / 2, rightView.bottom,
            w / 2 + bottomView.measuredWidth / 2, rightView.bottom + bottomView.measuredHeight)

        // Layout time remaining below the bottom button
        timeRemainingView.layout(w / 2 - timeRemainingView.measuredWidth / 2, bottomView.bottom + bottomViewTopMargin,
            w / 2 + timeRemainingView.measuredWidth / 2, bottomView.bottom + timeRemainingView.measuredHeight + bottomViewTopMargin)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }

    override fun render(state: State) {
        answerView?.setTextIfNeeded(state.gameState?.answer?.map { it.letter }?.joinToString(" "))
        left?.setTextIfNeeded(state.gameState?.leftLetter?.letter.toString())
        left?.alpha = getAlphaForLetter(state, Position.LEFT)
        top?.setTextIfNeeded(state.gameState?.topLetter?.letter.toString())
        top?.alpha = getAlphaForLetter(state, Position.TOP)
        right?.setTextIfNeeded(state.gameState?.rightLetter?.letter.toString())
        right?.alpha = getAlphaForLetter(state, Position.RIGHT)
        bottom?.setTextIfNeeded(state.gameState?.bottomLetter?.letter.toString())
        bottom?.alpha = getAlphaForLetter(state, Position.BOTTOM)
        score?.setTextIfNeeded("Score: ${state.gameState?.score.toString()}")
    }

    private fun getAlphaForLetter(state: State, position: Position): Float {
        return if (state.gameState?.answer?.filter { it.position == position }?.size == 0) 1f else 0.25f
    }
}

fun TextView.setTextIfNeeded(text: String?) {
    if (this.text != text) {
        this.text = text
    }
}
