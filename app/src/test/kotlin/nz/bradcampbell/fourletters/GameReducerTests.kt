package nz.bradcampbell.fourletters

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.data.Word
import nz.bradcampbell.fourletters.data.toListOfLetters
import nz.bradcampbell.fourletters.redux.reducer.GameReducer
import nz.bradcampbell.fourletters.redux.state.AppState
import nz.bradcampbell.fourletters.redux.state.GameState
import nz.bradcampbell.fourletters.redux.state.Letter
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GameReducerTests {

    val testLetters = "test".toListOfLetters()
    val left = testLetters[0]
    val top = testLetters[1]
    val right = testLetters[2]
    val bottom = testLetters[3]
    val testPossibleWords = listOf("test")

    @Test public fun testInitGame() {
        val gameReducer = GameReducer()
        var appState = AppState()
        assertNull(appState.gameState)
        val testWord = Word(testLetters, testPossibleWords)
        val initGameAction = Action.InitGame(testWord, ActionCreator.GAME_DURATION)
        appState = gameReducer.call(initGameAction, appState)
        assertNotNull(appState.gameState)
        assertEquals(appState.gameState?.leftLetter?.letter, left.letter)
        assertEquals(appState.gameState?.leftLetter?.position, left.position)
        assertEquals(appState.gameState?.topLetter?.letter, top.letter)
        assertEquals(appState.gameState?.topLetter?.position, top.position)
        assertEquals(appState.gameState?.rightLetter?.letter, right.letter)
        assertEquals(appState.gameState?.rightLetter?.position, right.position)
        assertEquals(appState.gameState?.bottomLetter?.letter, bottom.letter)
        assertEquals(appState.gameState?.bottomLetter?.position, bottom.position)
        assertEquals(appState.gameState?.score, 0)
        assertEquals(appState.gameState?.answer, emptyList())
        assertEquals(appState.gameState?.finishTime, ActionCreator.GAME_DURATION)
        assertEquals(appState.gameState?.possibleAnswers, testPossibleWords)
    }

    private fun testAnswerForAction(action: Action, initialAnswer: List<Letter> = emptyList(),
                                  expectedAnswer: List<Letter> = emptyList()) {
        val gameReducer = GameReducer()
        var appState = AppState(gameState = GameState(
            answer = initialAnswer,
            leftLetter = left,
            topLetter = top,
            rightLetter = right,
            bottomLetter = bottom,
            possibleAnswers = testPossibleWords,
            finishTime = 1
        ))
        appState = gameReducer.call(action, appState)
        assertEquals(appState.gameState?.answer, expectedAnswer)
    }

    @Test public fun testLeftPressedWithEmptyAnswer() {
        testAnswerForAction(Action.LeftPressed, expectedAnswer = listOf(left))
    }

    @Test public fun testLeftPressedWithLeftContainedInAnswer() {
        testAnswerForAction(Action.LeftPressed, listOf(left))
    }

    @Test public fun testTopPressedWithEmptyAnswer() {
        testAnswerForAction(Action.TopPressed, expectedAnswer = listOf(top))
    }

    @Test public fun testTopPressedWithTopContainedInAnswer() {
        testAnswerForAction(Action.TopPressed, listOf(top))
    }

    @Test public fun testRightPressedWithEmptyAnswer() {
        testAnswerForAction(Action.RightPressed, expectedAnswer = listOf(right))
    }

    @Test public fun testRightPressedWithRightContainedInAnswer() {
        testAnswerForAction(Action.RightPressed, listOf(right))
    }

    @Test public fun testBottomPressedWithEmptyAnswer() {
        testAnswerForAction(Action.BottomPressed, expectedAnswer = listOf(bottom))
    }

    @Test public fun testBottomPressedWithBottomContainedInAnswer() {
        testAnswerForAction(Action.BottomPressed, listOf(bottom))
    }

    @Test public fun testLeftLetterRemovedFromEnd() {
        testAnswerForAction(Action.LeftPressed, listOf(top, left), listOf(top))
    }

    @Test public fun testLeftLetterRemovedFromStart() {
        testAnswerForAction(Action.LeftPressed, listOf(left, top), listOf(top))
    }

    @Test public fun testLeftLetterRemovedFromMiddle() {
        testAnswerForAction(Action.LeftPressed, listOf(right, left, top), listOf(right, top))
    }

    @Test public fun testTopLetterRemovedFromEnd() {
        testAnswerForAction(Action.TopPressed, listOf(left, top), listOf(left))
    }

    @Test public fun testTopLetterRemovedFromStart() {
        testAnswerForAction(Action.TopPressed, listOf(top, left), listOf(left))
    }

    @Test public fun testTopLetterRemovedFromMiddle() {
        testAnswerForAction(Action.TopPressed, listOf(right, top, left), listOf(right, left))
    }

    @Test public fun testRightLetterRemovedFromEnd() {
        testAnswerForAction(Action.RightPressed, listOf(left, right), listOf(left))
    }

    @Test public fun testRightLetterRemovedFromStart() {
        testAnswerForAction(Action.RightPressed, listOf(right, left), listOf(left))
    }

    @Test public fun testRightLetterRemovedFromMiddle() {
        testAnswerForAction(Action.RightPressed, listOf(top, right, left), listOf(top, left))
    }

    @Test public fun testBottomLetterRemovedFromEnd() {
        testAnswerForAction(Action.BottomPressed, listOf(left, bottom), listOf(left))
    }

    @Test public fun testBottomLetterRemovedFromStart() {
        testAnswerForAction(Action.BottomPressed, listOf(bottom, left), listOf(left))
    }

    @Test public fun testBottomLetterRemovedFromMiddle() {
        testAnswerForAction(Action.BottomPressed, listOf(top, bottom, left), listOf(top, left))
    }

    @Test public fun testResetActionEmpty() {
        testAnswerForAction(Action.ResetGame)
    }

    @Test public fun testResetActionOne() {
        testAnswerForAction(Action.ResetGame, listOf(left))
    }

    @Test public fun testResetActionTwo() {
        testAnswerForAction(Action.ResetGame, listOf(left, top))
    }

    @Test public fun testResetActionThree() {
        testAnswerForAction(Action.ResetGame, listOf(left, top, right))
    }

    @Test public fun testResetActionFour() {
        testAnswerForAction(Action.ResetGame, listOf(left, top, right, bottom))
    }

    @Test public fun testBackClearsGameState() {
        val gameReducer = GameReducer()
        var appState = AppState(gameState = GameState(
            leftLetter = left,
            topLetter = top,
            rightLetter = right,
            bottomLetter = bottom,
            possibleAnswers = testPossibleWords,
            finishTime = 1
        ))
        val action = Action.Back
        appState = gameReducer.call(action, appState)
        assertNull(appState.gameState)
    }
}
