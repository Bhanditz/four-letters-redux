package nz.bradcampbell.fourletters.redux.state

import android.os.Parcel
import android.support.annotation.LayoutRes
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.paperparcel.DefaultAdapter
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable
import nz.bradcampbell.paperparcel.TypeAdapter

@PaperParcel
data class State(
  val paginationState: PaginationState = PaginationState(),
  val gameState: GameState? = null,
  val menuState: MenuState = MenuState()
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(State::class.java)
  }
}

@PaperParcel
data class PaginationState(
  val currentPage: Page = Page(R.layout.menu),
  val history: List<Page> = listOf<Page>()
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(PaginationState::class.java)
  }
}

@PaperParcel
data class GameState(
  val answer: List<Letter> = emptyList(),
  val leftLetter: Letter,
  val topLetter: Letter,
  val rightLetter: Letter,
  val bottomLetter: Letter,
  val possibleAnswers: List<String>,
  val score: Int = 0,
  val finishTime: Long
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(GameState::class.java)
  }
}

@PaperParcel
data class MenuState(
  val wordErrorDisplayed: Boolean = false
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(MenuState::class.java)
  }
}

@PaperParcel
data class Page(
  @LayoutRes var layoutId: Int
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(Page::class.java)
  }
}

enum class Position(val index: Int) {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    BOTTOM(3);

    companion object {
        fun from(index: Int): Position {
            for (position in values()) {
                if (position.index == index) {
                    return position
                }
            }
            return LEFT
        }
    }
}

@PaperParcel
data class Letter(
  val position: Position,
  val letter: Char
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelable.Creator(Letter::class.java)
  }
}

@DefaultAdapter
class PositionTypeAdapter : TypeAdapter<Position> {
    override fun writeToParcel(value: Position, outParcel: Parcel, flags: Int) {
        outParcel.writeInt(value.index)
    }

    override fun readFromParcel(inParcel: Parcel): Position {
        return Position.from(inParcel.readInt())
    }
}
