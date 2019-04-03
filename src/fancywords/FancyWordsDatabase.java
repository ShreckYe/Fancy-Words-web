package fancywords;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FancyWordsDatabase implements AutoCloseable {

    final static int RECOMMENDED_SIZE = 2 * Runtime.getRuntime().availableProcessors() + 1;
    private final static String DATABASE_NAME = "fancy_words_server", ALLOW_MULTI_QUERIES_TRUE = "allowMultiQueries=true";
    private final static String URL = "jdbc:mariadb://localhost:3306/" + DATABASE_NAME + "?" + ALLOW_MULTI_QUERIES_TRUE,
            USERNAME = "fancywordsserver",
            PASSWORD = "xhqycqyys";
    HikariDataSource hikariDataSource;

    public FancyWordsDatabase() throws SQLException {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        //hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);

        hikariDataSource.setMaximumPoolSize(RECOMMENDED_SIZE);

        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", 256);
        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariDataSource.addDataSourceProperty("cachePrepStmts", true);
        hikariDataSource.addDataSourceProperty("useServerPrepStmts", true);
    }

    public void close() {
        hikariDataSource.close();
    }

    public boolean authenticateUser(String username, byte[] encryptedPassword) {
        // TODO implement here
        return false;
    }

    public List<BriefUserWordEntry> queryAllEntries() throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT word, definition, log_absolute_mastery FROM user_word_entry ORDER BY word ASC;");

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<BriefUserWordEntry> entries = new ArrayList<>();
            for (int i = 0; resultSet.next(); i++) {
                entries.add(new BriefUserWordEntry(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3)));
            }

            connection.close();

            return entries;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean insertUserWordEntry(UserWordEntry userWordEntry) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_word_entry VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, userWordEntry.getWord());
            preparedStatement.setString(2, userWordEntry.getDefinition());
            preparedStatement.setDouble(3, userWordEntry.getLogAbsoluteMastery());
            preparedStatement.setLong(4, userWordEntry.getTimeCreated());
            preparedStatement.setLong(5, userWordEntry.getTimeEdited());
            preparedStatement.setInt(6, userWordEntry.getCountViewed());
            preparedStatement.setInt(7, userWordEntry.getCountTested());
            preparedStatement.setInt(8, userWordEntry.getCountTestedCorrect());
            long updateCount;
            try {
                updateCount = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062)
                    return false;
                else throw e;
            }

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateDefinition(String wordToBeUpdated, String definition) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_word_entry SET definition = ? WHERE word = ?;");
            preparedStatement.setString(1, definition);
            preparedStatement.setString(2, wordToBeUpdated);

            long updateCount = preparedStatement.executeUpdate();

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateMasteryByOne(String wordToBeUpdated, long time) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement selectPreparedStatement = connection.prepareStatement("SELECT log_absolute_mastery FROM user_word_entry WHERE word = ?;");
            selectPreparedStatement.setString(1, wordToBeUpdated);
            ResultSet resultSet = selectPreparedStatement.executeQuery();
            double logAbsoluteMastery;
            if (resultSet.next())
                logAbsoluteMastery = resultSet.getDouble(1);
            else
                return false;

            PreparedStatement updatePreparedStatement = connection.prepareStatement("UPDATE user_word_entry SET log_absolute_mastery = ? WHERE word = ?;");
            updatePreparedStatement.setDouble(1, Utils.increaseLogAbsoluteMasteryByOneByTime(logAbsoluteMastery, time));
            updatePreparedStatement.setString(2, wordToBeUpdated);

            long updateCount = updatePreparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateTimeEdited(String wordToBeUpdated, long timeEdited) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_word_entry SET time_edited = ? WHERE word = ?;");
            preparedStatement.setLong(1, timeEdited);
            preparedStatement.setString(2, wordToBeUpdated);

            long updateCount = preparedStatement.executeUpdate();

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * This method automatically increase the number of times this word has been viewed by ONE.
     *
     * @param wordToBeUpdated
     * @return
     * @throws SQLException
     */
    public boolean updateCountViewedByOne(String wordToBeUpdated) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_word_entry SET count_viewed = count_viewed + 1 WHERE word = ?;");
            preparedStatement.setString(1, wordToBeUpdated);

            long updateCount = preparedStatement.executeUpdate();

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateCountTested(String wordToBeUpdated, boolean correct) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(correct ?
                    "UPDATE user_word_entry SET count_tested = count_tested + 1 AND count_tested_correct = count_tested_correct + 1 WHERE word = ?;" :
                    "UPDATE user_word_entry SET count_tested = count_tested + 1 WHERE word = ?;");
            preparedStatement.setString(1, wordToBeUpdated);

            long updateCount = preparedStatement.executeUpdate();

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }


    public boolean deleteUserWordEntry(String word) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_word_entry WHERE word = ?;");
            preparedStatement.setString(1, word);

            long updateCount = preparedStatement.executeUpdate();

            connection.close();

            return updateCount > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public UserWordEntry queryWordEntryByWord(String word) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT word, definition, log_absolute_mastery, time_created, time_edited, count_viewed, count_tested, count_tested_correct FROM user_word_entry WHERE word = ?;");
            preparedStatement.setString(1, word);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            UserWordEntry userWordEntry = new UserWordEntry(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getLong(4),
                    resultSet.getLong(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8));

            connection.close();

            return userWordEntry;
        } catch (SQLException e) {
            throw e;
        }
    }

    public WordExam queryWordExam() throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement selectWordPreparedStatement = connection.prepareStatement("SELECT word, definition FROM user_word_entry ORDER BY log_absolute_mastery ASC LIMIT 1;");

            ResultSet resultSet = selectWordPreparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            String word = resultSet.getString(1),
                    definition = resultSet.getString(2);

            PreparedStatement selectChoicesPreparedStatement = connection.prepareStatement("SELECT definition FROM user_word_entry WHERE word != ?  ORDER BY rand() LIMIT 3;");
            selectChoicesPreparedStatement.setString(1, word);
            resultSet = selectChoicesPreparedStatement.executeQuery();
            String definitionChoices[] = new String[4];
            byte correctChoice = Utils.randomIn4();
            if (!resultSet.next()) return null;
            for (int i = 0, j = 0; i < 4; i++)
                if (i == correctChoice)
                    definitionChoices[i] = definition;
                else {
                    definitionChoices[i] = resultSet.getString(1);
                    j++;
                    if (!resultSet.next() && j < 3)
                        return null;
                }

            connection.close();

            return new WordExam(word, definitionChoices, correctChoice);
        } catch (SQLException e) {
            throw e;
        }
    }

    public DefinitionExam queryDefinitionExam() throws SQLException {
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement selectWordPreparedStatement = connection.prepareStatement("SELECT word, definition FROM user_word_entry ORDER BY log_absolute_mastery ASC LIMIT 1;");

            ResultSet resultSet = selectWordPreparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            String word = resultSet.getString(1),
                    definition = resultSet.getString(2);

            PreparedStatement selectChoicesPreparedStatement = connection.prepareStatement("SELECT word FROM user_word_entry WHERE word != ? ORDER BY rand() LIMIT 3;");
            selectChoicesPreparedStatement.setString(1, word);
            resultSet = selectChoicesPreparedStatement.executeQuery();
            String wordChoices[] = new String[4];
            byte correctChoice = Utils.randomIn4();
            if (!resultSet.next()) return null;
            for (int i = 0, j = 0; i < 4; i++)
                if (i == correctChoice)
                    wordChoices[i] = word;
                else {
                    wordChoices[i] = resultSet.getString(1);
                    j++;
                    if (!resultSet.next() && j < 3)
                        return null;
                }

            connection.close();

            return new DefinitionExam(word, definition, wordChoices, correctChoice);
        } catch (SQLException e) {
            throw e;
        }
    }
}