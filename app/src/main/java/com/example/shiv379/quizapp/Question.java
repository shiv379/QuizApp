package com.example.shiv379.quizapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Toby on 22/07/2017.
 */

public class Question {

    public static final int QUESTION_TYPE_TEXT = 1;
    public static final int QUESTION_TYPE_MULTIPLE = 2;
    public static final int QUESTION_TYPE_RADIO = 3;
    private int mQuestionType;
    private int mImageResource;
    private String mQuestionText;
    private ArrayList<String> mAnswers = new ArrayList<String>();
    private ArrayList<Integer> mCorrectAnswers = new ArrayList<Integer>();

    //Multiple choice, no image
    public Question(String questionText, String[] answers, Integer[] correctAnswers) {
        mQuestionType = QUESTION_TYPE_MULTIPLE;
        mQuestionText = questionText;
        mAnswers = new ArrayList<String>(Arrays.asList(answers));
        mCorrectAnswers= new ArrayList<Integer>(Arrays.asList(correctAnswers));
    }

    //Radio choice, no image
    public Question(String questionText, String[] answers, Integer correctAnswers) {
        mQuestionType = QUESTION_TYPE_RADIO;
        mQuestionText = questionText;
        mAnswers = new ArrayList<String>(Arrays.asList(answers));
        mCorrectAnswers= new ArrayList<Integer>(Arrays.asList(correctAnswers));
    }

    //Text question, no image, no correct answer
    public Question(String questionText) {
        mQuestionType = QUESTION_TYPE_TEXT;
        mQuestionText = questionText;
    }

    //Text question, no image, correct answer
    public Question(String questionText, String answers) {
        mQuestionType = QUESTION_TYPE_TEXT;
        mQuestionText = questionText;
        mAnswers.add(answers);
        mCorrectAnswers.add(0);
    }

    public int getQuestionType() {
        return mQuestionType;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public ArrayList<String> getAnswers() {
        return mAnswers;
    }

    public ArrayList<Integer> getCorrectAnswers() {
        return mCorrectAnswers;
    }

    public Integer getAnswerIndexByString(String answerString) {
        for (int i = 0; i < mAnswers.size(); i++){
            if (answerString == mAnswers.get(i)){
                return i;
            }
        }
        return null;
    }
}
