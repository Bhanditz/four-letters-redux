package nz.bradcampbell.fourletters.tests

import nz.bradcampbell.fourletters.redux.action.Action
import nz.bradcampbell.fourletters.redux.reducer.MenuReducer
import nz.bradcampbell.fourletters.redux.state.MenuState
import nz.bradcampbell.fourletters.redux.state.State
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MenuReducerTest {

    @Test public fun testWordLoadError() {
        val menuReducer = MenuReducer()
        var appState = State()
        val action = Action.LoadWordError
        assertFalse(appState.menuState.wordErrorDisplayed)
        appState = menuReducer.call(action, appState)
        assertTrue(appState.menuState.wordErrorDisplayed)
    }

    @Test public fun testDismissWordLoadError() {
        val menuReducer = MenuReducer()
        var appState = State(menuState = MenuState(true))
        val action = Action.DismissLoadWordError
        assertTrue(appState.menuState.wordErrorDisplayed)
        appState = menuReducer.call(action, appState)
        assertFalse(appState.menuState.wordErrorDisplayed)
    }
}
