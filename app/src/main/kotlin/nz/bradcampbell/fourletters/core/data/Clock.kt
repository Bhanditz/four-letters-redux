package nz.bradcampbell.fourletters.core.data

public interface Clock {
    fun millis() = System.currentTimeMillis()
}