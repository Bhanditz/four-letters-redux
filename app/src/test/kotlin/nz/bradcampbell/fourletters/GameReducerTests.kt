package nz.bradcampbell.fourletters

import nz.bradcampbell.fourletters.core.*
import nz.bradcampbell.fourletters.core.reducers.GameReducer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GameReducerTests {

    @Test
    public fun testInitGame() {
        val gameReducer = GameReducer()
        var appState = AppState()
        assertNull(appState.gameState)
        val testWord = Word("test".toListOfLetters(), listOf("test"))
        val initGameAction = Action.InitGame(testWord, ActionCreator.GAME_DURATION)
        appState = gameReducer.call(initGameAction, appState)
        assertNotNull(appState.gameState)
        assertEquals(appState.gameState?.leftLetter?.letter, 't')
        assertEquals(appState.gameState?.topLetter?.letter, 'e')
        assertEquals(appState.gameState?.rightLetter?.letter, 's')
        assertEquals(appState.gameState?.bottomLetter?.letter, 't')
        assertEquals(appState.gameState?.score, 0)
        assertEquals(appState.gameState?.answer, emptyList())
        assertEquals(appState.gameState?.finishTime, ActionCreator.GAME_DURATION)
        assertEquals(appState.gameState?.possibleAnswers, listOf("test"))
    }
}
