package nz.bradcampbell.fourletters.core.action

import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.core.data.WordRepository
import nz.bradcampbell.fourletters.core.state.Page
import nz.bradcampbell.fourletters.core.store.Store
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

    public companion object {
        public var GAME_DURATION = 20000L
        public var TIME_BONUS = 5000L
        public var POINTS_PER_WIN = 1
    }

    public fun initiateGame() {
        val startTime = System.currentTimeMillis()
        store.dispatch(Action.Navigate(Page(R.layout.loading)))
        initGameSubscription = wordRepository.getRandomWord()
            // Show loading for at least 500ms
            .delay(500 - (System.currentTimeMillis() - startTime), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // TODO: Pull out into clock interface for testability
                store.dispatch(Action.InitGame(it, System.currentTimeMillis() + GAME_DURATION))
                store.dispatch(Action.Navigate(Page(R.layout.game), false))
            }
    }

    public fun playAgain() {
        store.dispatch(Action.Back)
        initiateGame()
    }

    public fun gameOver() {
        store.dispatch(Action.Navigate(Page(R.layout.lose), false))
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
                // TODO: pre-load the next game while the user is playing the current level
                checkWinSubscription = wordRepository.getRandomWord()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        store.dispatch(Action.NextGame(it, POINTS_PER_WIN, TIME_BONUS))
                    }
            } else {
                store.dispatch(Action.ResetGame)
            }
        }
    }
}
