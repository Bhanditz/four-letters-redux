package nz.bradcampbell.fourletters.core

import nz.bradcampbell.fourletters.R
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class ActionCreator @Inject constructor(val store: Store, val wordRepository: WordRepository) {
    private var initGameSubscription: Subscription = Subscriptions.empty()
    private var checkWinSubscription: Subscription = Subscriptions.empty()

    public fun initiateGame() {
        val startTime = System.currentTimeMillis()
        store.dispatch(Action.Navigate(Page(R.layout.loading)))
        initGameSubscription = wordRepository.getRandomWord()
            // Show loading for at least 500ms
            .delay(500 - (System.currentTimeMillis() - startTime), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                store.dispatch(Action.InitGame( it ))
                store.dispatch(Action.Navigate(Page(R.layout.game), false))
            }
    }

    public fun playAgain() {
        store.dispatch(Action.Back)
        initiateGame()
    }

    public fun tick() {
        store.dispatch(Action.Tick)
        checkLose()
    }

    public fun back() {
        initGameSubscription.unsubscribe()
        checkWinSubscription.unsubscribe()
        store.dispatch(Action.Back)
    }

    public fun leftLetterPressed() {
        letterPressed(Action.LeftPressed)
    }

    public fun topLetterPressed() {
        letterPressed(Action.TopPressed)
    }

    public fun rightLetterPressed() {
        letterPressed(Action.RightPressed)
    }

    public fun bottomLetterPressed() {
        letterPressed(Action.BottomPressed)
    }

    private fun letterPressed(action: Action) {
        store.dispatch(action)
        checkWin()
    }

    private fun checkWin() {
        val gameState = store.state.gameState!!
        val answer = gameState.answer
        if (answer.size == 4) {
            val stringAnswer = answer.map { it.letter }.joinToString("")
            if (gameState.possibleAnswers.contains(stringAnswer)) {
                store.dispatch(Action.BumpScore)
                checkWinSubscription = wordRepository.getRandomWord()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        store.dispatch(Action.NextGame( it, 5000 ))
                    }
            } else {
                store.dispatch(Action.ResetGame)
            }
        }
    }

    private fun checkLose() {
        var gameState = store.state.gameState
        gameState?.let {
            if (gameState.timeRemaining <= 0) {
                store.dispatch(Action.Navigate(Page(R.layout.lose), false))
            }
        }
    }
}