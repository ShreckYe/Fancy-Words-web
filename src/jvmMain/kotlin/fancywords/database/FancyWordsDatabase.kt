package fancywords.database

import com.zaxxer.hikari.HikariDataSource
import fancywords.*
import java.sql.SQLException
import java.util.*

class FancyWordsDatabase @Throws(SQLException::class) constructor() : AutoCloseable {
    companion object {
        internal val RECOMMENDED_SIZE = 2 * Runtime.getRuntime().availableProcessors() + 1
        private val DATABASE_NAME = "fancy_words_server"
        private val ALLOW_MULTI_QUERIES_TRUE = "allowMultiQueries=true"
        private val URL = "jdbc:mariadb://localhost:3306/$DATABASE_NAME?$ALLOW_MULTI_QUERIES_TRUE"
        private val USERNAME = "fancywordsserver"
        private val PASSWORD = "xhqycqyys"
    }

    val hikariDataSource = HikariDataSource().apply {
        driverClassName = "org.mariadb.jdbc.Driver"
        //hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        jdbcUrl = URL
        username = USERNAME
        password = PASSWORD

        maximumPoolSize = RECOMMENDED_SIZE

        addDataSourceProperty("prepStmtCacheSize", 256)
        addDataSourceProperty("prepStmtCacheSqlLimit", 2048)
        addDataSourceProperty("cachePrepStmts", true)
        addDataSourceProperty("useServerPrepStmts", true)
    }

    override fun close() =
        hikariDataSource.close()

    fun authenticateUser(username: String, encryptedPassword: ByteArray): Boolean {
        // TODO implement here
        return false
    }

    @Throws(SQLException::class)
    fun queryAllEntries(): List<BriefUserWordEntry> {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("SELECT word, definition, log_absolute_mastery FROM user_word_entry ORDER BY word ASC;")

                val resultSet = preparedStatement.executeQuery()
                val entries = ArrayList<BriefUserWordEntry>()
                var i = 0
                while (resultSet.next()) {
                    entries.add(
                        BriefUserWordEntry(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3)
                        )
                    )
                    i++
                }

                connection.close()

                return entries
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun insertUserWordEntry(userWordEntry: UserWordEntry): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("INSERT INTO user_word_entry VALUES (?, ?, ?, ?, ?, ?, ?, ?);")
                preparedStatement.setString(1, userWordEntry.word)
                preparedStatement.setString(2, userWordEntry.definition)
                preparedStatement.setDouble(3, userWordEntry.logAbsoluteMastery)
                preparedStatement.setLong(4, userWordEntry.timeCreated)
                preparedStatement.setLong(5, userWordEntry.timeEdited)
                preparedStatement.setInt(6, userWordEntry.countViewed)
                preparedStatement.setInt(7, userWordEntry.countTested)
                preparedStatement.setInt(8, userWordEntry.countTestedCorrect)
                val updateCount: Long
                try {
                    updateCount = preparedStatement.executeUpdate().toLong()
                } catch (e: SQLException) {
                    return if (e.errorCode == 1062)
                        false
                    else
                        throw e
                }

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun updateDefinition(wordToBeUpdated: String, definition: String): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("UPDATE user_word_entry SET definition = ? WHERE word = ?;")
                preparedStatement.setString(1, definition)
                preparedStatement.setString(2, wordToBeUpdated)

                val updateCount = preparedStatement.executeUpdate().toLong()

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun updateMasteryByOne(wordToBeUpdated: String, time: Long): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                connection.autoCommit = false
                val selectPreparedStatement =
                    connection.prepareStatement("SELECT log_absolute_mastery FROM user_word_entry WHERE word = ?;")
                selectPreparedStatement.setString(1, wordToBeUpdated)
                val resultSet = selectPreparedStatement.executeQuery()
                val logAbsoluteMastery: Double
                if (resultSet.next())
                    logAbsoluteMastery = resultSet.getDouble(1)
                else
                    return false

                val updatePreparedStatement =
                    connection.prepareStatement("UPDATE user_word_entry SET log_absolute_mastery = ? WHERE word = ?;")
                updatePreparedStatement.setDouble(1, increaseLogAbsoluteMasteryByOneByTime(logAbsoluteMastery, time))
                updatePreparedStatement.setString(2, wordToBeUpdated)

                val updateCount = updatePreparedStatement.executeUpdate().toLong()

                connection.commit()
                connection.autoCommit = true

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun updateTimeEdited(wordToBeUpdated: String, timeEdited: Long): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("UPDATE user_word_entry SET time_edited = ? WHERE word = ?;")
                preparedStatement.setLong(1, timeEdited)
                preparedStatement.setString(2, wordToBeUpdated)

                val updateCount = preparedStatement.executeUpdate().toLong()

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    /**
     * This method automatically increase the number of times this word has been viewed by ONE.
     *
     * @param wordToBeUpdated
     * @return
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun updateCountViewedByOne(wordToBeUpdated: String): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("UPDATE user_word_entry SET count_viewed = count_viewed + 1 WHERE word = ?;")
                preparedStatement.setString(1, wordToBeUpdated)

                val updateCount = preparedStatement.executeUpdate().toLong()

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun updateCountTested(wordToBeUpdated: String, correct: Boolean): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement = connection.prepareStatement(
                    if (correct)
                        "UPDATE user_word_entry SET count_tested = count_tested + 1 AND count_tested_correct = count_tested_correct + 1 WHERE word = ?;"
                    else
                        "UPDATE user_word_entry SET count_tested = count_tested + 1 WHERE word = ?;"
                )
                preparedStatement.setString(1, wordToBeUpdated)

                val updateCount = preparedStatement.executeUpdate().toLong()

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }


    @Throws(SQLException::class)
    fun deleteUserWordEntry(word: String): Boolean {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement = connection.prepareStatement("DELETE FROM user_word_entry WHERE word = ?;")
                preparedStatement.setString(1, word)

                val updateCount = preparedStatement.executeUpdate().toLong()

                connection.close()

                return updateCount > 0
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun queryWordEntryByWord(word: String): UserWordEntry? {
        try {
            hikariDataSource.connection.use { connection ->
                val preparedStatement =
                    connection.prepareStatement("SELECT word, definition, log_absolute_mastery, time_created, time_edited, count_viewed, count_tested, count_tested_correct FROM user_word_entry WHERE word = ?;")
                preparedStatement.setString(1, word)

                val resultSet = preparedStatement.executeQuery()
                if (!resultSet.next()) return null
                val userWordEntry = UserWordEntry(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getLong(4),
                    resultSet.getLong(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8)
                )

                connection.close()

                return userWordEntry
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun queryWordExam(): WordExam? {
        try {
            hikariDataSource.connection.use { connection ->
                val selectWordPreparedStatement =
                    connection.prepareStatement("SELECT word, definition FROM user_word_entry ORDER BY log_absolute_mastery ASC LIMIT 1;")

                var resultSet = selectWordPreparedStatement.executeQuery()
                if (!resultSet.next()) return null
                val word = resultSet.getString(1)
                val definition = resultSet.getString(2)

                val selectChoicesPreparedStatement =
                    connection.prepareStatement("SELECT definition FROM user_word_entry WHERE word != ?  ORDER BY rand() LIMIT 3;")
                selectChoicesPreparedStatement.setString(1, word)
                resultSet = selectChoicesPreparedStatement.executeQuery()
                val definitionChoices = arrayOfNulls<String>(4)
                val correctChoice = randomIn4()
                if (!resultSet.next()) return null
                var i = 0
                var j = 0
                while (i < 4) {
                    if (i == correctChoice.toInt())
                        definitionChoices[i] = definition
                    else {
                        definitionChoices[i] = resultSet.getString(1)
                        j++
                        if (!resultSet.next() && j < 3)
                            return null
                    }
                    i++
                }

                connection.close()

                return WordExam(word, definitionChoices, correctChoice)
            }
        } catch (e: SQLException) {
            throw e
        }

    }

    @Throws(SQLException::class)
    fun queryDefinitionExam(): DefinitionExam? {
        try {
            hikariDataSource.connection.use { connection ->
                val selectWordPreparedStatement =
                    connection.prepareStatement("SELECT word, definition FROM user_word_entry ORDER BY log_absolute_mastery ASC LIMIT 1;")

                var resultSet = selectWordPreparedStatement.executeQuery()
                if (!resultSet.next()) return null
                val word = resultSet.getString(1)
                val definition = resultSet.getString(2)

                val selectChoicesPreparedStatement =
                    connection.prepareStatement("SELECT word FROM user_word_entry WHERE word != ? ORDER BY rand() LIMIT 3;")
                selectChoicesPreparedStatement.setString(1, word)
                resultSet = selectChoicesPreparedStatement.executeQuery()
                val wordChoices = arrayOfNulls<String>(4)
                val correctChoice = randomIn4()
                if (!resultSet.next()) return null
                var i = 0
                var j = 0
                while (i < 4) {
                    if (i == correctChoice.toInt())
                        wordChoices[i] = word
                    else {
                        wordChoices[i] = resultSet.getString(1)
                        j++
                        if (!resultSet.next() && j < 3)
                            return null
                    }
                    i++
                }

                connection.close()

                return DefinitionExam(word, definition, wordChoices, correctChoice)
            }
        } catch (e: SQLException) {
            throw e
        }

    }
}