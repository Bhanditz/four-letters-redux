package nz.bradcampbell.fourletters.redux.state

import android.os.Parcel
import android.support.annotation.LayoutRes
import nz.bradcampbell.fourletters.R
import paperparcel.RegisterAdapter
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import paperparcel.TypeAdapter

@PaperParcel
data class State(
    val paginationState: PaginationState = PaginationState(),
    val gameState: GameState? = null,
    val menuState: MenuState = MenuState()
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelState.CREATOR
  }
}

@PaperParcel
data class PaginationState(
    val currentPage: Page = Page(R.layout.menu),
    val history: List<Page> = listOf<Page>()
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelPaginationState.CREATOR
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
    @JvmField val CREATOR = PaperParcelGameState.CREATOR
  }
}

@PaperParcel
data class MenuState(
    val wordErrorDisplayed: Boolean = false
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelMenuState.CREATOR
  }
}

@PaperParcel
data class Page(
    @LayoutRes var layoutId: Int
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelPage.CREATOR
  }
}

@PaperParcel
data class Letter(
    val position: Position,
    val letter: Char
) : PaperParcelable {
  companion object {
    @JvmField val CREATOR = PaperParcelLetter.CREATOR
  }
}

enum class Position(val index: Int) {
  LEFT(0),
  TOP(1),
  RIGHT(2),
  BOTTOM(3);

  companion object {
    fun from(index: Int): Position {
      return values().firstOrNull { it.index == index } ?: LEFT
    }
  }
}

@RegisterAdapter
class PositionTypeAdapter : TypeAdapter<Position> {
  override fun writeToParcel(value: Position?, dest: Parcel, flags: Int) {
    dest.writeInt(value!!.index)
  }

  override fun readFromParcel(inParcel: Parcel): Position {
    return Position.from(inParcel.readInt())
  }
}
