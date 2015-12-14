package nz.bradcampbell.fourletters.tests

import nz.bradcampbell.fourletters.data.Random
import nz.bradcampbell.fourletters.data.shuffle
import nz.bradcampbell.fourletters.data.sort
import nz.bradcampbell.fourletters.data.toListOfLetters
import nz.bradcampbell.fourletters.redux.state.Letter
import nz.bradcampbell.fourletters.redux.state.Position
import org.junit.Test
import kotlin.test.assertEquals

class WordUtilsTests {

    @Test public fun testStringSortEmpty() {
        val testString = ""
        assertEquals(testString.sort(), "")
    }

    @Test public fun testStringSort() {
        val testString = "test"
        assertEquals(testString.sort(), "estt")
    }

    @Test public fun testToListOfLetters() {
        val testString = "test"
        val expectedResult = listOf(Letter(Position.LEFT, 't'), Letter(Position.TOP, 'e'), Letter(Position.RIGHT, 's'),
            Letter(Position.BOTTOM, 't'))
        assertEquals(testString.toListOfLetters(), expectedResult)
    }

    @Test public fun testShuffleBackwards() {
        val notRandom = object : Random {
            override fun next(cap: Int): Int {
                return cap - 1
            }
        }
        val testString = "test"
        assertEquals(testString.shuffle(notRandom), "tset")
    }
}
