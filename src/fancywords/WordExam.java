package fancywords;

/**
 *
 */
public class WordExam implements Exam {
    /**
     *
     */
    private String questionWord;
    private String[] definitionChoices;
    private byte correctChoice;

    public WordExam(String questionWord, String[] definitionChoices, byte correctChoice) {
        this.questionWord = questionWord;
        this.definitionChoices = definitionChoices;
        this.correctChoice = correctChoice;
    }

    @Override
    public String getWord() {
        return questionWord;
    }

    public byte getExamType() {
        return EXAM_TYPE_WORD;
    }

    public String[] getDefinitionChoices() {
        return definitionChoices;
    }

    public byte getCorrectChoice() {
        return correctChoice;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }
}