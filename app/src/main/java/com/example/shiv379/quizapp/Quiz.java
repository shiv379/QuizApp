package com.example.shiv379.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Toby on 22/07/2017.
 *
 * Quiz app created for Udacity Android Developer nanodegree
 * Proof of concept using different UI elements. I got a bit carried away and
 * made it much more complex than the original brief!
 *
 * If developed further I will look into using fragments and adapters to allow
 * better styling of the UI elements.
 */

public class Quiz extends AppCompatActivity {

    private final ArrayList<Question> mQuestions = new ArrayList<>();
    private int mQuestionIndex = 0;
    private ArrayList<Integer> mQuestionViews = new ArrayList<>();
    private int mScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mQuestionIndex = savedInstanceState.getInt("question_index", 0);
            mScore = savedInstanceState.getInt("score",0);
        }

        mQuestions.add(new Question("What is your name?")); //Text input, no correct answer
        mQuestions.add(new Question("What programming language is this course teaching you?","Java"));
        mQuestions.add(new Question("What is the capital of Australia?",new String[]{"Sydney","Perth","Perranporth","Canberra"},3));
        mQuestions.add(new Question("What colours do you mix to make purple?",new String[]{"Red","Blue","Green","Yellow","Black","Gold"},new Integer[]{0,1}));

        Button submitButton = findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                checkAnswer();
            }
        });

        doQuestion(mQuestions.get(mQuestionIndex));
    }

    /**
     *
     * @param question presents the specified question on screen
     */
    private void doQuestion(Question question){
        int mAnswerViewId;
        removeOldViews();
        LinearLayout rootView = findViewById(R.id.question_root);
        TextView questionTextView = findViewById(R.id.question_text);
        questionTextView.setText(question.getQuestionText());

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        switch (question.getQuestionType()) {
            case Question.QUESTION_TYPE_TEXT:
                EditText uiItem = (EditText)getLayoutInflater().inflate(R.layout.ettemplate, null);
                mAnswerViewId = View.generateViewId();
                uiItem.setId(mAnswerViewId);
                rootView.addView(uiItem,params);
                mQuestionViews.add(mAnswerViewId);
                rootView.clearFocus();
                uiItem.requestFocus();
                break;
            case Question.QUESTION_TYPE_RADIO:
                RadioGroup radioGroup = new RadioGroup(this);
                mAnswerViewId = View.generateViewId();
                radioGroup.setId(mAnswerViewId);
                rootView.addView(radioGroup);
                mQuestionViews.add(mAnswerViewId);
                for (String answer : question.getAnswers()) {
                    RadioButton rButton = new RadioButton(this);
                    rButton.setText(answer);
                    mAnswerViewId = View.generateViewId();
                    rButton.setId(mAnswerViewId);
                    radioGroup.addView(rButton,params);
                    mQuestionViews.add(mQuestionViews.size()-1,mAnswerViewId);//inject before RadioGroup to ensure RadioButtons are deleted first
                }
                break;
            case Question.QUESTION_TYPE_MULTIPLE:
                for (String answer : question.getAnswers()) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(answer);
                    mAnswerViewId = View.generateViewId();
                    checkBox.setId(mAnswerViewId);
                    rootView.addView(checkBox,params);
                    mQuestionViews.add(mAnswerViewId);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Proceed to next question, or end the quiz if out of questions
     */
    private void nextQuestion(){
        if (mQuestionIndex < mQuestions.size()-1) {
            mQuestionIndex++;
            doQuestion(mQuestions.get(mQuestionIndex));
        } else {
            removeOldViews();
            Button btn = findViewById(R.id.button);
            ((ViewGroup) btn.getParent()).removeView(btn);
            TextView questionTextView = findViewById(R.id.question_text);
            questionTextView.setText(getString(R.string.score) + " " + mScore);
        }
    }

    /**
     * Checks if answer provided by the user is correct, provides Toast feedback
     */
    private void checkAnswer(){
        Question question = mQuestions.get(mQuestionIndex);
        ArrayList<Integer> correctAnswers = question.getCorrectAnswers();
        Boolean correct = true;
        //If there are no correct answers, just say thanks and move on
        if (correctAnswers.isEmpty()){
            Toast.makeText(this, getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
            nextQuestion();
            return;
        }

        switch (question.getQuestionType()) {
            case Question.QUESTION_TYPE_TEXT:
                EditText editText = findViewById(mQuestionViews.get(0));
                if (!(editText.getText().toString().toLowerCase().equals(question.getAnswers().get(0).toLowerCase()))){
                    correct = false;
                }
                break;
            case Question.QUESTION_TYPE_RADIO:
                for (int i : mQuestionViews) {
                    if (findViewById(i) instanceof RadioButton){
                        RadioButton  rb  = findViewById(i);
                        int j = question.getAnswerIndexByString((String) rb.getText());
                        if ((!correctAnswers.contains(j) && rb.isChecked()) || (correctAnswers.contains(j) && !rb.isChecked())){
                            correct = false;
                            break;
                        }
                    }
                }
                break;
            case Question.QUESTION_TYPE_MULTIPLE:
                for (int i : mQuestionViews) {
                    if (findViewById(i) instanceof CheckBox){
                        CheckBox cb = findViewById(i);
                        int j = question.getAnswerIndexByString((String) cb.getText());
                        if ((!correctAnswers.contains(j) && cb.isChecked()) || (correctAnswers.contains(j) && !cb.isChecked())){
                            correct = false;
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }

        if (correct){
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(this, getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
        }
        nextQuestion();
    }

    /**
     * Removes the UI elements from the previous question, by deleting everything with an id found in mQuestionViews
     */
    private void removeOldViews(){
        for (int rId : mQuestionViews) {
            View toDel = findViewById(rId);
            ((ViewGroup) toDel.getParent()).removeView(toDel);
        }
        mQuestionViews.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("question_index",mQuestionIndex);
        outState.putInt("score",mScore);
    }
}
