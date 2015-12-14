package nz.bradcampbell.fourletters.tests;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static junit.framework.Assert.assertEquals;
import static nz.bradcampbell.fourletters.data.WordRepositoryKt.toListOfLetters;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

import nz.bradcampbell.fourletters.data.Word;
import nz.bradcampbell.fourletters.data.WordRepository;
import nz.bradcampbell.fourletters.data.WordService;
import nz.bradcampbell.fourletters.data.Random;
import nz.bradcampbell.fourletters.data.internal.WordRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * These tests have to be in Java. Mockito doesn't work well with Kotlin.
 */
public class WordRepositoryTests {

    private WordService mockWordService;
    private WordRepository wordRepository;

    @Before public void setup() {
        mockWordService = mock(WordService.class);

        // Always return 0
        Random mockRandom = mock(Random.class);
        when(mockRandom.next(anyInt())).thenReturn(0);

        wordRepository = new WordRepositoryImpl(mockWordService, mockRandom);
    }

    @Test public void testUsesCacheIfAvailable() {
        final AtomicInteger subscribeCount = new AtomicInteger();

        // Set up mocks
        List<String> mockWords = asList("cool", "bean", "test");
        when(mockWordService.getAllWords()).thenReturn(just(mockWords).map(
                new Func1<List<String>, List<? extends String>>() {
                    @Override public List<? extends String> call(List<String> strings) {
                        subscribeCount.getAndIncrement();
                        return strings;
                    }
                }));

        // Verify we haven't subscribed the service yet
        assertEquals(subscribeCount.get(), 0);

        TestSubscriber<Word> testSubscriber1 = new TestSubscriber<>();
        wordRepository.getRandomWord().subscribe(testSubscriber1);

        // Verify we have used the service once
        assertEquals(subscribeCount.get(), 1);

        TestSubscriber<Word> testSubscriber2 = new TestSubscriber<>();
        wordRepository.getRandomWord().subscribe(testSubscriber2);

        // Verify we still have used the service only once because it hit the cache
        assertEquals(subscribeCount.get(), 1);
    }

    @Test public void testWordCreation() {
        // Set up mocks
        List<String> mockWords = asList("brad", "bard", "test");
        when(mockWordService.getAllWords()).thenReturn(just(mockWords).map(
                new Func1<List<String>, List<? extends String>>() {
                    @Override public List<? extends String> call(List<String> strings) {
                        return strings;
                    }
                }));

        // Will always return the first word in the list because the "random" mock always returns 0
        Word expectedAnswer = new Word(toListOfLetters("brad"), asList("brad", "bard"));

        // Test that the Word is "brad" and has correctly found all the possible answers ("brad" and "bard") and
        // excluded everything else ("test")
        TestSubscriber<Word> testSubscriber1 = new TestSubscriber<>();
        wordRepository.getRandomWord().subscribe(testSubscriber1);
        testSubscriber1.assertNoErrors();
        testSubscriber1.assertReceivedOnNext(singletonList(expectedAnswer));
        testSubscriber1.assertCompleted();
    }
}
