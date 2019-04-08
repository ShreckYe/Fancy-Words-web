package fancywords

class UserWordEntry(
    word: String,
    definition: String,
    logAbsoluteMastery: Double,
    val timeCreated: Long,
    val timeEdited: Long,
    val countViewed: Int,
    val countTested: Int,
    val countTestedCorrect: Int
) : BriefUserWordEntry(word, definition, logAbsoluteMastery)