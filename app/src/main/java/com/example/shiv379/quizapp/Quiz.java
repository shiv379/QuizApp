package com.example.shiv379.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {

    final ArrayList<Question> mQuestions = new ArrayList<Question>();
    private int mQuestionIndex = 0;
    private ArrayList<Integer> mQuestionViews = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestions.add(new Question("What is your name?")); //Text input, no correct answer
        mQuestions.add(new Question("What programming language is this course teaching you?","Java"));
        mQuestions.add(new Question("What is the capital of Australia?",new String[]{"Sydney","Perth","Perranporth","Canberra"},3));
        mQuestions.add(new Question("What colours do you mix to make purple?",new String[]{"Red","Blue","Green","Yellow","Black","Gold"},new Integer[]{0,1}));

        Button submitButton = (Button) findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                checkAnswer();
            }
        });

        doQuestion(mQuestions.get(mQuestionIndex));
    }

    private void doQuestion(Question question){
        int mAnswerViewId;
        removeOldViews();
        LinearLayout rootView = (LinearLayout) findViewById(R.id.question_root);
        TextView questionTextView = (TextView) findViewById(R.id.question_text);
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
                findViewById(R.id.button).requestFocus();
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

    private void nextQuestion(){
        if (mQuestionIndex < mQuestions.size()-1) {
            mQuestionIndex++;
            doQuestion(mQuestions.get(mQuestionIndex));
        }
    }

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
                EditText editText = (EditText) findViewById(mQuestionViews.get(0));
                if (editText.getText().toString() != question.getAnswers().get(0)){
                    correct = false;
                }
                break;
            case Question.QUESTION_TYPE_RADIO:
                for (int i : mQuestionViews) {
                    if (findViewById(i) instanceof RadioButton){
                        RadioButton  rb  = (RadioButton) findViewById(i);
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
                        CheckBox cb = (CheckBox) findViewById(i);
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
        } else {
            Toast.makeText(this, getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
        }
        nextQuestion();
    }

    private void removeOldViews(){
        for (int rId : mQuestionViews) {
            View toDel = findViewById(rId);
            ((ViewGroup) toDel.getParent()).removeView(toDel);
        }
        mQuestionViews.clear();
    }
}
