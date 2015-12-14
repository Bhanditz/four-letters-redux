package nz.bradcampbell.fourletters.data

interface Random {
    fun next(cap: Int): Int

    object REAL : Random {
        val random = java.util.Random()
        override fun next(cap: Int) = random.nextInt(cap)
    }
}
