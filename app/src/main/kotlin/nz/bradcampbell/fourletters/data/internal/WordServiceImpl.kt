package nz.bradcampbell.fourletters.data.internal

import android.app.Application
import nz.bradcampbell.fourletters.data.WordService
import rx.Observable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class WordServiceImpl(val app: Application) : WordService {
    override fun getAllWords(): Observable<List<String>> {
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
            Observable.just(words.toList())
        }
    }
}
