package fancywords

/**
 * Created by Huiqi Xue on 2017/6/3.
 */
open class BriefUserWordEntry(word: String, definition: String, val logAbsoluteMastery: Double) :
    WordEntry(word, definition) {
    fun getMastery(time: Long) = getMastery(logAbsoluteMastery, time)
}
