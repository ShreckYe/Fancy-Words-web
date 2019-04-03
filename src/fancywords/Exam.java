package fancywords;

import java.util.*;

/**
 *
 */
public interface Exam {
    byte EXAM_TYPE_WORD = 1, EXAM_TYPE_DEFINITION = 2;

    String getWord();

    byte getExamType();

    byte getCorrectChoice();

    //public long getTime();

    class InvalidExamTypeException extends Exception {

    }
}