package fancywords;

/**
 * 
 */
public class DefinitionExam implements Exam {
    private String word;
    private String definition;
    private String[] wordChoices;
    private byte correctChoice;

    public DefinitionExam(String word, String definition, String[] wordChoices, byte correctChoice) {
        this.word = word;
        this.definition = definition;
        this.wordChoices = wordChoices;
        this.correctChoice = correctChoice;
    }

    public byte getExamType() {
        return EXAM_TYPE_DEFINITION;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String[] getWordChoices() {
        return wordChoices;
    }

    /**
     * @return
     */
    public byte getCorrectChoice() {
        return correctChoice;
    }
}