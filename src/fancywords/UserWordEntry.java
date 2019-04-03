package fancywords;

/**
 *
 */
public class UserWordEntry extends BriefUserWordEntry {
    public UserWordEntry(String word,
                         String definition,
                         double logAbsoluteMastery,
                         long timeCreated,
                         long timeEdited,
                         int countViewed,
                         int countTested,
                         int countTestedCorrect) {
        super(word, definition, logAbsoluteMastery);
        this.timeCreated = timeCreated;
        this.timeEdited = timeEdited;
        this.countViewed = countViewed;
        this.countTested = countTested;
        this.countTestedCorrect = countTestedCorrect;
    }

    private long timeCreated;
    private long timeEdited;
    private int countViewed;
    private int countTested;
    private int countTestedCorrect;

    public long getTimeCreated() {
        return timeCreated;
    }

    public long getTimeEdited() {
        return timeEdited;
    }

    public int getCountViewed() {
        return countViewed;
    }

    public int getCountTested() {
        return countTested;
    }

    public int getCountTestedCorrect() {
        return countTestedCorrect;
    }
}