package nz.bradcampbell.fourletters.redux.reducer

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.state.GameState
import nz.bradcampbell.fourletters.redux.state.Letter
import nz.bradcampbell.fourletters.redux.state.State

class GameReducer: Reducer<Action, State> {

    override fun call(action: Action, state: State): State {
        return when(action) {
            is Action.InitGame -> state.copy(gameState = GameState(
                answer = emptyList(),
                leftLetter = action.word.letters[0],
                topLetter = action.word.letters[1],
                rightLetter = action.word.letters[2],
                bottomLetter = action.word.letters[3],
                possibleAnswers = action.word.possibleAnswers,
                finishTime = action.finishTime
            ))
            is Action.NextGame -> state.copy(gameState = state.gameState!!.copy(
                    answer = emptyList(),
                    leftLetter = action.word.letters[0],
                    topLetter = action.word.letters[1],
                    rightLetter = action.word.letters[2],
                    bottomLetter = action.word.letters[3],
                    possibleAnswers = action.word.possibleAnswers,
                    finishTime = state.gameState.finishTime + action.bonusTime,
                    score = state.gameState.score + action.points
                ))
            is Action.LeftPressed -> letterPressed(state, state.gameState!!.leftLetter)
            is Action.TopPressed -> letterPressed(state, state.gameState!!.topLetter)
            is Action.RightPressed -> letterPressed(state, state.gameState!!.rightLetter)
            is Action.BottomPressed -> letterPressed(state, state.gameState!!.bottomLetter)
            is Action.ResetGame -> state.copy(gameState = state.gameState!!.copy(answer = emptyList()))
            is Action.Back -> state.copy(gameState = null)
            else -> state
        }
    }

    private fun letterPressed(state: State, letter: Letter): State {
        val answer = state.gameState!!.answer
        val isUsed = answer.contains(letter)
        val newAnswer = if (isUsed) answer.filter { it != letter } else answer + letter
        return state.copy(gameState = state.gameState.copy(newAnswer))
    }
}
