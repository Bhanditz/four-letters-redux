package nz.bradcampbell.fourletters.core

import nz.bradcampbell.fourletters.R

data class AppState(val paginationState: PaginationState = PaginationState(),
                 val gameState: GameState? = null)

data class PaginationState(val currentPage: Page = Page(R.layout.menu),
                      val history: List<Page> = listOf<Page>())

data class GameState(val answer: List<Letter> = emptyList(),
                val leftLetter: Letter,
                val topLetter: Letter,
                val rightLetter: Letter,
                val bottomLetter: Letter,
                val possibleAnswers: List<String>,
                val score: Int = 0,
                val timeRemaining: Int = 15000)
