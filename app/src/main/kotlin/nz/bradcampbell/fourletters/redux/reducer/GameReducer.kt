package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.GameState
import nz.bradcampbell.fourletters.redux.state.Letter
import nz.bradcampbell.fourletters.redux.state.AppState

class GameReducer: Reducer<Action, AppState> {

    override fun call(action: Action, appState: AppState): AppState {
        return when(action) {
            is Action.InitGame -> appState.copy(gameState = GameState(
                answer = emptyList(),
                leftLetter = action.word.letters[0],
                topLetter = action.word.letters[1],
                rightLetter = action.word.letters[2],
                bottomLetter = action.word.letters[3],
                possibleAnswers = action.word.possibleAnswers,
                finishTime = action.finishTime
            ))
            is Action.NextGame -> appState.copy(gameState = appState.gameState!!.copy(
                    answer = emptyList(),
                    leftLetter = action.word.letters[0],
                    topLetter = action.word.letters[1],
                    rightLetter = action.word.letters[2],
                    bottomLetter = action.word.letters[3],
                    possibleAnswers = action.word.possibleAnswers,
                    finishTime = appState.gameState.finishTime + action.bonusTime,
                    score = appState.gameState.score + action.points
                ))
            is Action.LeftPressed -> letterPressed(appState, appState.gameState!!.leftLetter)
            is Action.TopPressed -> letterPressed(appState, appState.gameState!!.topLetter)
            is Action.RightPressed -> letterPressed(appState, appState.gameState!!.rightLetter)
            is Action.BottomPressed -> letterPressed(appState, appState.gameState!!.bottomLetter)
            is Action.ResetGame -> appState.copy(gameState = appState.gameState!!.copy(answer = emptyList()))
            is Action.Back -> appState.copy(gameState = null)
            else -> appState
        }
    }

    private fun letterPressed(appState: AppState, letter: Letter): AppState {
        val answer = appState.gameState!!.answer
        val isUsed = answer.contains(letter)
        val newAnswer = if (isUsed) answer.filter { it != letter } else answer + letter
        return appState.copy(gameState = appState.gameState.copy(newAnswer))
    }
}