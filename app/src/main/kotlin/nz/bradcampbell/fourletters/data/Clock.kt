package nz.bradcampbell.fourletters.data

public interface Clock {
    fun millis() = System.currentTimeMillis()
}