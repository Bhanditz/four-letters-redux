package nz.bradcampbell.fourletters.core.data

import android.app.Application
import nz.bradcampbell.fourletters.core.state.Letter
import nz.bradcampbell.fourletters.core.state.Position
import rx.Observable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

val random = Random()

@Singleton
class WordRepository @Inject constructor(val app: Application) {
    var wordsCache: List<String> = emptyList()

    public fun getRandomWord(): Observable<Word> {
        return Observable.concat(wordsCache(), readAllWords())
            .first { it.size > 0 }
            .map { it[random.nextInt(it.size)] }
            .map { it.shuffle() }
            .flatMap {
                val str = Observable.just(it)
                val words = getAllPossibleWords(it)
                Observable.zip(str, words, { s, w -> Word(s.toListOfLetters(), w) })
            }
    }

    private fun getAllPossibleWords(letters: String): Observable<List<String>> {
        val sortedLetters = letters.sort()
        val result = ArrayList<String>()
        for (word in wordsCache) {
            if (sortedLetters == word.sort()) {
                result.add(word)
            }
        }
        return Observable.just(result.toList())
    }

    private fun wordsCache(): Observable<List<String>> {
        return Observable.just(wordsCache);
    }

    private fun readAllWords(): Observable<List<String>> {
        return Observable.defer {
            val words = ArrayList<String>()
            val input = BufferedReader(InputStreamReader(app.assets.open("data.txt"), "UTF-8"));
            try {
                input.forEachLine { line ->
                    line.split(" ").forEach { words.add(it) }
                }
            } finally {
                input.close()
            }
            wordsCache = words.toList();
            Observable.just(wordsCache)
        }
    }
}

fun String.shuffle(): String {
    var oldStr = this;
    var newStr = "";
    while (oldStr.length != 0) {
        val i = random.nextInt(oldStr.length)
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
