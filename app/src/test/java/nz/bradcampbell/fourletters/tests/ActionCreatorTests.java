package nz.bradcampbell.fourletters.tests;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static nz.bradcampbell.fourletters.data.WordRepositoryKt.toListOfLetters;
import static nz.bradcampbell.fourletters.redux.state.Position.BOTTOM;
import static nz.bradcampbell.fourletters.redux.state.Position.RIGHT;
import static nz.bradcampbell.fourletters.redux.state.Position.TOP;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nz.bradcampbell.fourletters.BuildConfig;
import nz.bradcampbell.fourletters.R;
import nz.bradcampbell.fourletters.RxJavaResetRule;
import nz.bradcampbell.fourletters.data.Clock;
import nz.bradcampbell.fourletters.data.Word;
import nz.bradcampbell.fourletters.data.WordRepository;
import nz.bradcampbell.fourletters.redux.action.Action;
import nz.bradcampbell.fourletters.redux.action.ActionCreator;
import nz.bradcampbell.fourletters.redux.state.GameState;
import nz.bradcampbell.fourletters.redux.state.Letter;
import nz.bradcampbell.fourletters.redux.state.MenuState;
import nz.bradcampbell.fourletters.redux.state.Page;
import nz.bradcampbell.fourletters.redux.state.PaginationState;
import nz.bradcampbell.fourletters.redux.state.State;
import nz.bradcampbell.fourletters.redux.store.Store;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import rx.Observable;

import java.util.Collections;
import java.util.List;

/**
 * These tests have to be in Java. Mockito doesn't work well with Kotlin.
 */
@Config(constants = BuildConfig.class, sdk = 16)
@RunWith(RobolectricGradleTestRunner.class)
public class ActionCreatorTests {
    @Rule public final RxJavaResetRule rxJavaResetRule = new RxJavaResetRule();

    private final Word testWord = new Word(toListOfLetters("test"), singletonList("test"));

    private Store mockStore;
    private Clock mockClock;
    private WordRepository mockRepository;

    private ActionCreator actionCreator;

    @Before public void setup() {
        mockClock = mock(Clock.class);
        when(mockClock.millis()).thenReturn(0L);

        mockRepository = mock(WordRepository.class);
        when(mockRepository.getRandomWord()).thenReturn(Observable.just(testWord));

        //noinspection unchecked
        mockStore = mock(Store.class);

        actionCreator = new ActionCreator(mockStore, mockRepository, mockClock);
    }

    @Test public void testInitiateGame() {
        actionCreator.initiateGame();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.Navigate firstAction = (Action.Navigate) capturedActions.get(0);
        assertEquals(firstAction.getPage().getLayoutId(), R.layout.loading);
        assertEquals(firstAction.getAddToBackStack(), true);

        Action.InitGame secondAction = (Action.InitGame) capturedActions.get(1);
        assertEquals(secondAction.getWord(), testWord);
        assertEquals(secondAction.getFinishTime(), ActionCreator.GAME_DURATION);

        Action.Navigate thirdAction = (Action.Navigate) capturedActions.get(2);
        assertEquals(thirdAction.getPage().getLayoutId(), R.layout.game);
        assertEquals(thirdAction.getAddToBackStack(), false);
    }

    @Test public void testPlayAgain() {
        actionCreator.playAgain();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(4)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.Back.INSTANCE);

        Action.Navigate secondAction = (Action.Navigate) capturedActions.get(1);
        assertEquals(secondAction.getPage().getLayoutId(), R.layout.loading);
        assertEquals(secondAction.getAddToBackStack(), true);

        Action.InitGame thirdAction = (Action.InitGame) capturedActions.get(2);
        assertEquals(thirdAction.getWord(), testWord);
        assertEquals(thirdAction.getFinishTime(), ActionCreator.GAME_DURATION);

        Action.Navigate fourthAction = (Action.Navigate) capturedActions.get(3);
        assertEquals(fourthAction.getPage().getLayoutId(), R.layout.game);
        assertEquals(fourthAction.getAddToBackStack(), false);
    }

    @Test public void testGameOver() {
        actionCreator.gameOver();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(1)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.Navigate firstAction = (Action.Navigate) capturedActions.get(0);
        assertEquals(firstAction.getPage().getLayoutId(), R.layout.lose);
        assertEquals(firstAction.getAddToBackStack(), false);
    }

    @Test public void testBack() {
        actionCreator.back();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(1)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.Back.INSTANCE);
    }

    @Test public void testLeftLetterPressedWithEmptyAnswer() {
        when(mockStore.state()).thenReturn(createState("test", Collections.<Letter>emptyList(),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();
        verifySingleActionDispatched(Action.LeftPressed.INSTANCE);
    }

    @Test public void testLeftLetterPressedWithOneItemInAnswer() {
        when(mockStore.state()).thenReturn(createState("test", singletonList(new Letter(TOP, 'e')),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();
        verifySingleActionDispatched(Action.LeftPressed.INSTANCE);
    }

    @Test public void testLeftLetterPressedWithTwoItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's')),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();
        verifySingleActionDispatched(Action.LeftPressed.INSTANCE);
    }

    @Test public void testLeftLetterPressedWithThreeItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's'), new Letter(BOTTOM, 't')),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();
        verifySingleActionDispatched(Action.LeftPressed.INSTANCE);
    }

    @Test public void testLeftLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.LeftPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.NextGame);
    }

    @Test public void testLeftLetterPressedWithIncorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("estt"),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.LeftPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.ResetGame);
    }

    @Test public void testLeftLetterPressedWithCorrectAnswerAndErrorOccurs() {
        when(mockRepository.getRandomWord()).thenReturn(Observable.<Word>error(new Exception()));
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));

        actionCreator.leftLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.LeftPressed.INSTANCE);
        assertEquals(capturedActions.get(1), Action.Back.INSTANCE);
        assertEquals(capturedActions.get(2), Action.LoadWordError.INSTANCE);
    }

    @Test public void testBonusTimeFullForLeftLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.leftLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), ActionCreator.TIME_BONUS);
    }

    @Test public void testBonusTimeCappedForLeftLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, ActionCreator.GAME_DURATION));
        when(mockClock.millis()).thenReturn(1L);
        actionCreator.leftLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), 1L);
    }

    @Test public void testTopLetterPressedWithEmptyAnswer() {
        when(mockStore.state()).thenReturn(createState("test", Collections.<Letter>emptyList(),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();
        verifySingleActionDispatched(Action.TopPressed.INSTANCE);
    }

    @Test public void testTopLetterPressedWithOneItemInAnswer() {
        when(mockStore.state()).thenReturn(createState("test", singletonList(new Letter(TOP, 'e')),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();
        verifySingleActionDispatched(Action.TopPressed.INSTANCE);
    }

    @Test public void testTopLetterPressedWithTwoItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's')),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();
        verifySingleActionDispatched(Action.TopPressed.INSTANCE);
    }

    @Test public void testTopLetterPressedWithThreeItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's'), new Letter(BOTTOM, 't')),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();
        verifySingleActionDispatched(Action.TopPressed.INSTANCE);
    }

    @Test public void testTopLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.TopPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.NextGame);
    }

    @Test public void testTopLetterPressedWithIncorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("estt"),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.TopPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.ResetGame);
    }

    @Test public void testTopLetterPressedWithCorrectAnswerAndErrorOccurs() {
        when(mockRepository.getRandomWord()).thenReturn(Observable.<Word>error(new Exception()));
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));

        actionCreator.topLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.TopPressed.INSTANCE);
        assertEquals(capturedActions.get(1), Action.Back.INSTANCE);
        assertEquals(capturedActions.get(2), Action.LoadWordError.INSTANCE);
    }

    @Test public void testBonusTimeFullForTopLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.topLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), ActionCreator.TIME_BONUS);
    }

    @Test public void testBonusTimeCappedForTopLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, ActionCreator.GAME_DURATION));
        when(mockClock.millis()).thenReturn(1L);
        actionCreator.topLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), 1L);
    }

    @Test public void testRightLetterPressedWithEmptyAnswer() {
        when(mockStore.state()).thenReturn(createState("test", Collections.<Letter>emptyList(),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();
        verifySingleActionDispatched(Action.RightPressed.INSTANCE);
    }

    @Test public void testRightLetterPressedWithOneItemInAnswer() {
        when(mockStore.state()).thenReturn(createState("test", singletonList(new Letter(TOP, 'e')),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();
        verifySingleActionDispatched(Action.RightPressed.INSTANCE);
    }

    @Test public void testRightLetterPressedWithTwoItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's')),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();
        verifySingleActionDispatched(Action.RightPressed.INSTANCE);
    }

    @Test public void testRightLetterPressedWithThreeItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's'), new Letter(BOTTOM, 't')),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();
        verifySingleActionDispatched(Action.RightPressed.INSTANCE);
    }

    @Test public void testRightLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.RightPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.NextGame);
    }

    @Test public void testRightLetterPressedWithIncorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("estt"),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.RightPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.ResetGame);
    }

    @Test public void testRightLetterPressedWithCorrectAnswerAndErrorOccurs() {
        when(mockRepository.getRandomWord()).thenReturn(Observable.<Word>error(new Exception()));
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));

        actionCreator.rightLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.RightPressed.INSTANCE);
        assertEquals(capturedActions.get(1), Action.Back.INSTANCE);
        assertEquals(capturedActions.get(2), Action.LoadWordError.INSTANCE);
    }

    @Test public void testBonusTimeFullForRightLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.rightLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), ActionCreator.TIME_BONUS);
    }

    @Test public void testBonusTimeCappedForRightLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, ActionCreator.GAME_DURATION));
        when(mockClock.millis()).thenReturn(1L);
        actionCreator.rightLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), 1L);
    }

    @Test public void testBottomLetterPressedWithEmptyAnswer() {
        when(mockStore.state()).thenReturn(createState("test", Collections.<Letter>emptyList(),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();
        verifySingleActionDispatched(Action.BottomPressed.INSTANCE);
    }

    @Test public void testBottomLetterPressedWithOneItemInAnswer() {
        when(mockStore.state()).thenReturn(createState("test", singletonList(new Letter(TOP, 'e')),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();
        verifySingleActionDispatched(Action.BottomPressed.INSTANCE);
    }

    @Test public void testBottomLetterPressedWithTwoItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's')),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();
        verifySingleActionDispatched(Action.BottomPressed.INSTANCE);
    }

    @Test public void testBottomLetterPressedWithThreeItemsInAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                asList(new Letter(TOP, 'e'), new Letter(RIGHT, 's'), new Letter(BOTTOM, 't')),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();
        verifySingleActionDispatched(Action.BottomPressed.INSTANCE);
    }

    @Test public void testBottomLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.BottomPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.NextGame);
    }

    @Test public void testBottomLetterPressedWithIncorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("estt"),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.BottomPressed.INSTANCE);
        assertTrue(capturedActions.get(1) instanceof Action.ResetGame);
    }

    @Test public void testBottomLetterPressedWithCorrectAnswerAndErrorOccurs() {
        when(mockRepository.getRandomWord()).thenReturn(Observable.<Word>error(new Exception()));
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));

        actionCreator.bottomLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.BottomPressed.INSTANCE);
        assertEquals(capturedActions.get(1), Action.Back.INSTANCE);
        assertEquals(capturedActions.get(2), Action.LoadWordError.INSTANCE);
    }

    @Test public void testBonusTimeFullForBottomLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, 1L));
        actionCreator.bottomLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), ActionCreator.TIME_BONUS);
    }

    @Test public void testBonusTimeCappedForBottomLetterPressedWithCorrectAnswer() {
        when(mockStore.state()).thenReturn(createState("test",
                toListOfLetters("test"),
                singletonList("test"), 0, ActionCreator.GAME_DURATION));
        when(mockClock.millis()).thenReturn(1L);
        actionCreator.bottomLetterPressed();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(2)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.NextGame nextGameAction = (Action.NextGame) capturedActions.get(1);
        assertEquals(nextGameAction.getBonusTime(), 1L);
    }

    @Test public void testInitiateGameError() {
        when(mockRepository.getRandomWord()).thenReturn(Observable.<Word>error(new Exception()));

        actionCreator.initiateGame();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(3)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        Action.Navigate firstAction = (Action.Navigate) capturedActions.get(0);
        assertEquals(firstAction.getPage().getLayoutId(), R.layout.loading);
        assertEquals(firstAction.getAddToBackStack(), true);

        assertEquals(capturedActions.get(1), Action.Back.INSTANCE);
        assertEquals(capturedActions.get(2), Action.LoadWordError.INSTANCE);
    }

    @Test public void testDismissWordLoadError() {
        actionCreator.dismissWordLoadError();

        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(1)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), Action.DismissLoadWordError.INSTANCE);
    }

    private void verifySingleActionDispatched(Action action) {
        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(mockStore, times(1)).dispatch(actionCaptor.capture());

        List<Action> capturedActions = actionCaptor.getAllValues();

        assertEquals(capturedActions.get(0), action);
    }

    private State createState(String word, List<Letter> answer, List<String> possibleAnswers, int score,
            long finishTime) {
        List<Letter> wordLetters = toListOfLetters(word);
        return new State(
                new PaginationState(new Page(R.layout.game), singletonList(new Page(R.layout.menu))),
                new GameState(answer, wordLetters.get(0), wordLetters.get(1), wordLetters.get(2), wordLetters.get(3),
                              possibleAnswers, score, finishTime),
                new MenuState(false));
    }
}
