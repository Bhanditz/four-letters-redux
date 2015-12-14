package nz.bradcampbell.fourletters.data

import rx.Observable

public interface WordService {
    fun getAllWords() : Observable<List<String>>
}