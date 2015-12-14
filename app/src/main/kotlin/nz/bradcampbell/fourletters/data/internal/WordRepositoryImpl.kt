package nz.bradcampbell.fourletters.data.internal

import nz.bradcampbell.fourletters.data.*
import nz.bradcampbell.fourletters.data.Random
import rx.Observable
import java.util.*

class WordRepositoryImpl(val wordService: WordService, val random: Random) : WordRepository {
    var wordsCache: List<String> = emptyList()

    override public fun getRandomWord(): Observable<Word> {
        return Observable.concat(wordsCache(), wordService.getAllWords())
            .first { it.size > 0 }
            .doOnNext { wordsCache = it; }
            .map { it[random.next(it.size)] }
            .map { it.shuffle(random) }
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
}
