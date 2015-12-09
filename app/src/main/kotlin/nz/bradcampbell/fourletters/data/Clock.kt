package nz.bradcampbell.fourletters.data

public interface Clock {
    fun millis() : Long

    object REAL : Clock {
        override fun millis() = System.currentTimeMillis()
    }
}