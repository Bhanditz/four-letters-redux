package nz.bradcampbell.fourletters.data

import nz.bradcampbell.fourletters.redux.state.Letter
import nz.bradcampbell.fourletters.redux.state.Position
import rx.Observable

interface WordRepository {
    fun getRandomWord(): Observable<Word>
}

fun String.shuffle(random: Random): String {
    var oldStr = this;
    var newStr = "";
    while (oldStr.length != 0) {
        val i = random.next(oldStr.length)
        newStr += oldStr[i]
        oldStr = oldStr.removeRange(i, i + 1)
    }
    return newStr;
}

fun String.sort(): String {
    val sortedLetters = toCharArray()
    sortedLetters.sort()
    return String(sortedLetters)
}

public data class Word(val letters: List<Letter>, val possibleAnswers: List<String>)

public fun String.toListOfLetters(): List<Letter> {
    return Array(4, { Letter(Position.from(it), this[it]) }).toList()
}
