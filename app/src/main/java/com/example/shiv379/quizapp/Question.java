package com.example.shiv379.quizapp;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Toby on 22/07/2017.
 *
 * {@Question} represents a question of type text, multiple choice, or single choice from multiple
 *
 */

class Question {

    static final int QUESTION_TYPE_TEXT = 1;
    static final int QUESTION_TYPE_MULTIPLE = 2;
    static final int QUESTION_TYPE_RADIO = 3;
    private final int mQuestionType;
    private final String mQuestionText;
    private ArrayList<String> mAnswers = new ArrayList<>(); //List of possible answers
    private ArrayList<Integer> mCorrectAnswers = new ArrayList<>(); //list of index ids from mAnswers for correct answers

    /**
     *Multiple choice, no image
     * @param questionText is the text of the question
     * @param answers is the list of potential answers
     * @param correctAnswers is the list of index ids from the answers array indicating which are correct answers
     */
    Question(String questionText, String[] answers, Integer[] correctAnswers) {
        mQuestionType = QUESTION_TYPE_MULTIPLE;
        mQuestionText = questionText;
        mAnswers = new ArrayList<>(Arrays.asList(answers));
        mCorrectAnswers= new ArrayList<>(Arrays.asList(correctAnswers));
    }

    /**
     * Radio choice, no image
     * @param questionText is the text of the question
     * @param answers is the list of potential answers
     * @param correctAnswers is the index ids from the answers array indicating which is the correct answer
     */
    Question(String questionText, String[] answers, Integer correctAnswers) {
        mQuestionType = QUESTION_TYPE_RADIO;
        mQuestionText = questionText;
        mAnswers = new ArrayList<>(Arrays.asList(answers));
        mCorrectAnswers= new ArrayList<>(Arrays.asList(correctAnswers));
    }

    /**
     * Text question, no image, no correct answer
     * @param questionText is the text of the question
     */
    Question(String questionText) {
        mQuestionType = QUESTION_TYPE_TEXT;
        mQuestionText = questionText;
    }

    /**
     * Text question, no image, correct answer
     * @param questionText is the text of the question
     * @param answers is the correct answer
     */
    Question(String questionText, String answers) {
        mQuestionType = QUESTION_TYPE_TEXT;
        mQuestionText = questionText;
        mAnswers.add(answers);
        mCorrectAnswers.add(0);
    }

    int getQuestionType() {
        return mQuestionType;
    }

    String getQuestionText() {
        return mQuestionText;
    }

    ArrayList<String> getAnswers() {
        return mAnswers;
    }

    ArrayList<Integer> getCorrectAnswers() {
        return mCorrectAnswers;
    }

    Integer getAnswerIndexByString(String answerString) {
        for (int i = 0; i < mAnswers.size(); i++){
            if (answerString.toLowerCase().equals(mAnswers.get(i).toLowerCase())){
                return i;
            }
        }
        return null;
    }
}
