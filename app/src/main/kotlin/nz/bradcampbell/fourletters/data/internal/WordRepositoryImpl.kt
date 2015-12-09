package nz.bradcampbell.fourletters.data.internal

import android.app.Application
import nz.bradcampbell.fourletters.data.*
import rx.Observable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class WordRepositoryImpl(val app: Application) : WordRepository {
    var wordsCache: List<String> = emptyList()

    override public fun getRandomWord(): Observable<Word> {
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
