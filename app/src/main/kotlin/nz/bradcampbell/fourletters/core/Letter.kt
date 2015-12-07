package nz.bradcampbell.fourletters.core

public enum class Position(val index: Int) {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    BOTTOM(3);

    companion object {
        public fun from(index: Int): Position {
            for (position in values()) {
                if (position.index == index) {
                    return position
                }
            }
            return LEFT
        }
    }
}

public data class Letter(val position: Position, val letter: Char)
