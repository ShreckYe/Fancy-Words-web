package fancywords;

/**
 * Created by Huiqi Xue on 2017/6/3.
 */
public class BriefUserWordEntry extends WordEntry {

    private double logAbsoluteMastery;

    public BriefUserWordEntry(String word, String definition, double logAbsoluteMastery) {
        super(word, definition);
        this.logAbsoluteMastery = logAbsoluteMastery;
    }


    public double getLogAbsoluteMastery() {
        return logAbsoluteMastery;
    }

    public double getMastery(long time) {
        return Utils.getMastery(logAbsoluteMastery, time);
    }
}
