package nz.bradcampbell.fourletters;

import static java.util.Collections.singletonList;
import static junit.framework.Assert.assertEquals;
import static nz.bradcampbell.fourletters.core.data.WordRepositoryKt.toListOfLetters;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nz.bradcampbell.fourletters.core.action.Action;
import nz.bradcampbell.fourletters.core.action.ActionCreator;
import nz.bradcampbell.fourletters.core.data.Clock;
import nz.bradcampbell.fourletters.core.data.Word;
import nz.bradcampbell.fourletters.core.data.WordRepository;
import nz.bradcampbell.fourletters.core.state.AppState;
import nz.bradcampbell.fourletters.core.store.Store;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import rx.Observable;

import java.util.List;

/**
 * These tests have to be in Java. Mockito doesn't work well with Kotlin.
 */
@Config(constants = BuildConfig.class, sdk = 16)
@RunWith(RobolectricGradleTestRunner.class)
public class ActionCreatorTests {
    @Rule public final RxJavaResetRule rxJavaResetRule = new RxJavaResetRule();

    private final Word testWord = new Word(toListOfLetters("test"), singletonList("test"));

    private Store<Action, AppState> mockStore;

    private ActionCreator actionCreator;

    @Before public void setup() {
        Clock mockClock = mock(Clock.class);
        when(mockClock.millis()).thenReturn(0L);

        WordRepository mockWordRepository = mock(WordRepository.class);
        when(mockWordRepository.getRandomWord()).thenReturn(Observable.just(testWord));

        //noinspection unchecked
        mockStore = mock(Store.class);

        actionCreator = new ActionCreator(mockStore, mockWordRepository, mockClock);
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
}
