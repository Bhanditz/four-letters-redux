package nz.bradcampbell.fourletters.core.reducers

import nz.bradcampbell.fourletters.core.Action
import nz.bradcampbell.fourletters.core.GameState
import nz.bradcampbell.fourletters.core.Letter
import nz.bradcampbell.fourletters.core.AppState

class GameReducer: Reducer<Action, AppState> {

    override fun call(action: Action, appState: AppState): AppState {
        return when(action) {
            is Action.InitGame -> appState.copy(gameState = GameState(
                    answer = emptyList(),
                    leftLetter = action.word.letters[0],
                    topLetter = action.word.letters[1],
                    rightLetter = action.word.letters[2],
                    bottomLetter = action.word.letters[3],
                    possibleAnswers = action.word.possibleAnswers
                ))
            is Action.NextGame -> appState.copy(gameState = appState.gameState!!.copy(
                    answer = emptyList(),
                    leftLetter = action.word.letters[0],
                    topLetter = action.word.letters[1],
                    rightLetter = action.word.letters[2],
                    bottomLetter = action.word.letters[3],
                    possibleAnswers = action.word.possibleAnswers,
                    timeRemaining = appState.gameState.timeRemaining + action.bonusTime
                ))
            is Action.LeftPressed -> letterPressed(appState, appState.gameState!!.leftLetter)
            is Action.TopPressed -> letterPressed(appState, appState.gameState!!.topLetter)
            is Action.RightPressed -> letterPressed(appState, appState.gameState!!.rightLetter)
            is Action.BottomPressed -> letterPressed(appState, appState.gameState!!.bottomLetter)
            is Action.BumpScore -> appState.copy(gameState = appState.gameState!!.copy(score = appState.gameState.score + 1))
            is Action.ResetGame -> appState.copy(gameState = appState.gameState!!.copy(answer = emptyList()))
            is Action.Tick -> appState.copy(gameState = appState.gameState?.copy(timeRemaining = Math.max(appState.gameState.timeRemaining - 16, 0)))
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
