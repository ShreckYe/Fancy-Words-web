package fancywords;

import java.util.*;

/**
 *
 */
public class WordEntry {
    private String word;
    private String definition;

    public WordEntry(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    /**
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * @return
     */
    public String getDefinition() {
        return definition;
    }

}