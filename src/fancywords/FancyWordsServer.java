package fancywords;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class FancyWordsServer implements AutoCloseable {

    private static FancyWordsServer instance;
    FancyWordsDatabase fancyWordsDatabase;

    private FancyWordsServer() throws SQLException {
        this.fancyWordsDatabase = new FancyWordsDatabase();
    }

    public static FancyWordsServer getInstanceAndStart() throws SQLException {
        if (instance == null)
            instance = new FancyWordsServer();
        return instance;
    }

    public void close() throws SQLException {
        fancyWordsDatabase.close();
        instance = null;
    }

    public List<BriefUserWordEntry> requestAllEntries() throws SQLException {
        return fancyWordsDatabase.queryAllEntries();
    }

    public boolean addNewUserWordEntry(String word, String definition) throws SQLException {
        return fancyWordsDatabase.insertUserWordEntry(new UserWordEntry(word, definition, Double.MIN_VALUE, System.currentTimeMillis(), System.currentTimeMillis(), 0, 0, 0));
    }


    public boolean updateUserWordEntry(String wordToBeUpdated, String definition) throws SQLException {
        fancyWordsDatabase.updateTimeEdited(wordToBeUpdated, System.currentTimeMillis());
        return fancyWordsDatabase.updateDefinition(wordToBeUpdated, definition);
    }

    /**
     * @param word
     * @return
     */
    public UserWordEntry requestUserWordEntry(String word) throws SQLException {
        if (fancyWordsDatabase.updateCountViewedByOne(word))
            return fancyWordsDatabase.queryWordEntryByWord(word);
        else
            return null;
    }

    /**
     * @param word
     * @return
     */
    public boolean deleteUserWordEntry(String word) throws SQLException {
        return fancyWordsDatabase.deleteUserWordEntry(word);
    }

    /**
     * @param examType
     * @return
     */
    public Exam requestExam(byte examType) throws Exam.InvalidExamTypeException, SQLException {
        switch (examType) {
            case Exam.EXAM_TYPE_WORD:
                return fancyWordsDatabase.queryWordExam();
            case Exam.EXAM_TYPE_DEFINITION:
                return fancyWordsDatabase.queryDefinitionExam();
            default:
                throw new Exam.InvalidExamTypeException();
        }
    }

    /**
     * @param correct
     * @return
     */
    public boolean updateWordExamFinished(String word, boolean correct) throws SQLException {
        return fancyWordsDatabase.updateCountTested(word, correct) &&
                (!correct || fancyWordsDatabase.updateMasteryByOne(word, System.currentTimeMillis()));
    }
}