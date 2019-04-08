package fancywords

import fancywords.database.FancyWordsDatabase
import java.sql.SQLException

class FancyWordsServer @Throws(SQLException::class)
private constructor() : AutoCloseable {
    val fancyWordsDatabase: FancyWordsDatabase = FancyWordsDatabase()

    @Throws(SQLException::class)
    override fun close() =
        fancyWordsDatabase.close()

    @Throws(SQLException::class)
    fun requestAllEntries(): List<BriefUserWordEntry> {
        return fancyWordsDatabase.queryAllEntries()
    }

    @Throws(SQLException::class)
    fun addNewUserWordEntry(word: String, definition: String): Boolean {
        return fancyWordsDatabase.insertUserWordEntry(
            UserWordEntry(
                word,
                definition,
                java.lang.Double.MIN_VALUE,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                0,
                0,
                0
            )
        )
    }


    @Throws(SQLException::class)
    fun updateUserWordEntry(wordToBeUpdated: String, definition: String): Boolean {
        fancyWordsDatabase.updateTimeEdited(wordToBeUpdated, System.currentTimeMillis())
        return fancyWordsDatabase.updateDefinition(wordToBeUpdated, definition)
    }

    /**
     * @param word
     * @return
     */
    @Throws(SQLException::class)
    fun requestUserWordEntry(word: String): UserWordEntry? {
        return if (fancyWordsDatabase.updateCountViewedByOne(word))
            fancyWordsDatabase.queryWordEntryByWord(word)
        else
            null
    }

    /**
     * @param word
     * @return
     */
    @Throws(SQLException::class)
    fun deleteUserWordEntry(word: String): Boolean {
        return fancyWordsDatabase.deleteUserWordEntry(word)
    }

    /**
     * @param examType
     * @return
     */
    @Throws(Exam.InvalidExamTypeException::class, SQLException::class)
    fun requestExam(examType: Byte): Exam? {
        when (examType) {
            EXAM_TYPE_WORD -> return fancyWordsDatabase.queryWordExam()
            EXAM_TYPE_DEFINITION -> return fancyWordsDatabase.queryDefinitionExam()
            else -> throw Exam.InvalidExamTypeException()
        }
    }

    /**
     * @param correct
     * @return
     */
    @Throws(SQLException::class)
    fun updateWordExamFinished(word: String, correct: Boolean): Boolean {
        return fancyWordsDatabase.updateCountTested(
            word,
            correct
        ) && (!correct || fancyWordsDatabase.updateMasteryByOne(word, System.currentTimeMillis()))
    }

    companion object {
        val INSTANCE: FancyWordsServer by lazy { FancyWordsServer() }
    }
}