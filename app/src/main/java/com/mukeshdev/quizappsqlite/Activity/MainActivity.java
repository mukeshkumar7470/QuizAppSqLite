package com.mukeshdev.quizappsqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mukeshdev.quizappsqlite.CorrectDialog;
import com.mukeshdev.quizappsqlite.LoadingDialog;
import com.mukeshdev.quizappsqlite.PlayAudioForAnswers;
import com.mukeshdev.quizappsqlite.Models.Questions;
import com.mukeshdev.quizappsqlite.Utility.QuizDbHelper;
import com.mukeshdev.quizappsqlite.R;
import com.mukeshdev.quizappsqlite.TimerDialog;
import com.mukeshdev.quizappsqlite.WrongDialog;
import com.mukeshdev.quizappsqlite.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ArrayList<Questions> questionList;
    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestions;
    private boolean answerd;

    private final Handler handler = new Handler();

    private int correctAns = 0, wrongAns = 0;

    private TimerDialog timerDialog;
    private CorrectDialog correctDialog;
    private WrongDialog wrongDialog;
    private LoadingDialog loadingDialog;
    private PlayAudioForAnswers playAudioForAnswers;

    int FLAG = 0;

    int score =0;

    private int totalSizeofQuiz=0;

    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private long timeleftinMillis;

    private long backPressedTime;

    private String categoryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent intentCategory = getIntent();
        categoryValue = intentCategory.getStringExtra("Category");



        fetchDB();
        Log.i("BUG","onCreate() in MainActivity");




        timerDialog = new TimerDialog(this);
        correctDialog = new CorrectDialog(this);
        wrongDialog = new WrongDialog(this);
        loadingDialog = new LoadingDialog(this);
        playAudioForAnswers = new PlayAudioForAnswers(this);





    }

    public void fetchDB() {
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestionsWithCategory(categoryValue);  // calling the method with category
        startQuiz();

    }

    public void startQuiz() {

        questionTotalCount = questionList.size();
        Collections.shuffle(questionList);

        showQuestions();   // calling showQuestion() method

        binding.rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                switch (checkedId){

                    case R.id.rb_one:

                        binding.rbOne.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Green));
                        binding.rbTwo.setTextColor(Color.WHITE);
                        binding.rbThree.setTextColor(Color.WHITE);
                        binding.rbFour.setTextColor(Color.WHITE);



                        binding.rbOne.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_checked_background));
                        binding.rbTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbThree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbFour.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));

                        break;
                    case R.id.rb_two:
                        binding.rbTwo.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Green));

                        binding.rbOne.setTextColor(Color.WHITE);
                        binding.rbThree.setTextColor(Color.WHITE);
                        binding.rbFour.setTextColor(Color.WHITE);



                        binding.rbTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_checked_background));
                        binding.rbOne.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbThree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbFour.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));

                        break;

                    case R.id.rb_three:
                        binding.rbThree.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Green));
                        binding.rbTwo.setTextColor(Color.WHITE);
                        binding.rbOne.setTextColor(Color.WHITE);
                        binding.rbFour.setTextColor(Color.WHITE);


                        binding.rbThree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_checked_background));
                        binding.rbTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbFour.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbOne.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));

                        break;

                    case R.id.rb_four:
                        binding.rbFour.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Green));
                        binding.rbTwo.setTextColor(Color.WHITE);
                        binding.rbThree.setTextColor(Color.WHITE);
                        binding.rbOne.setTextColor(Color.WHITE);



                        binding.rbFour.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_checked_background));
                        binding.rbTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbThree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
                        binding.rbOne.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));

                        break;
                }

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answerd) {
                    if (binding.rbOne.isChecked() || binding.rbTwo.isChecked() || binding.rbThree.isChecked() || binding.rbFour.isChecked()) {

                        quizOperation();
                    } else {

                        Toast.makeText(MainActivity.this, "Please Select the Answer ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    public void showQuestions() {


        binding.rgOption.clearCheck();

        binding.rbOne.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
        binding.rbTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
        binding.rbThree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));
        binding.rbFour.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_radio_unchecked_background));

        binding.rbOne.setTextColor(Color.WHITE);
        binding.rbTwo.setTextColor(Color.WHITE);
        binding.rbThree.setTextColor(Color.WHITE);
        binding.rbFour.setTextColor(Color.WHITE);


        if (questionCounter < questionTotalCount) {
            currentQuestions = questionList.get(questionCounter);
            binding.txtContainQuestion.setText(currentQuestions.getQuestion());
            binding.rbOne.setText(currentQuestions.getOption1());
            binding.rbTwo.setText(currentQuestions.getOption2());
            binding.rbThree.setText(currentQuestions.getOption3());
            binding.rbFour.setText(currentQuestions.getOption4());

            questionCounter++;
            answerd = false;
            binding.btnNext.setText("Confirm and Next");

            binding.txtQuestion.setText("Questions: " + questionCounter + "/" + questionTotalCount);


            timeleftinMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();




        } else {

            // If Number of Questions Finishes then we need to finish the Quiz and Shows the User Quiz Performance


            Toast.makeText(this, "Quiz Finshed", Toast.LENGTH_SHORT).show();
            loadingDialog.startLoadingDialog();
            Context context;

            binding.rbOne.setClickable(false);
            binding.rbTwo.setClickable(false);
            binding.rbThree.setClickable(false);
            binding.rbFour.setClickable(false);
            binding.btnNext.setClickable(false);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finalResult();

                }
            }, 2000);
        }
    }

    private void quizOperation() {
        answerd = true;

        countDownTimer.cancel();

        RadioButton rbselected = findViewById(binding.rgOption.getCheckedRadioButtonId());
        int answerNr = binding.rgOption.indexOfChild(rbselected) + 1;

        checkSolution(answerNr, rbselected);

    }


    private void checkSolution(int answerNr, RadioButton rbselected) {

        switch (currentQuestions.getAnswerNr()) {
            case 1:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    binding.rbOne.setBackground(ContextCompat.getDrawable(
                            this, R.drawable.correct_option_background));
                    binding.rbOne.setTextColor(Color.WHITE);

                    correctAns++;


                    score += 10;
                    binding.txtScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score,this);


                    FLAG = 1;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);






                } else {
                    changetoIncorrectColor(rbselected);

                    wrongAns++;


                    String correctAnswer = (String) binding.rbOne.getText();
                    wrongDialog.wrongDialog(correctAnswer,this);

                    FLAG = 2;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);



                }
                break;
            case 2:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    binding.rbTwo.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    binding.rbTwo.setTextColor(Color.WHITE);

                    correctAns++;


                    score += 10;
                    binding.txtScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score,this);

                    FLAG = 1;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);



                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) binding.rbTwo.getText();
                    wrongDialog.wrongDialog(correctAnswer,this);

                    FLAG = 2;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);




                }
                break;
            case 3:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    binding.rbThree.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    binding.rbThree.setTextColor(Color.WHITE);


                    correctAns++;


                    score += 10;
                    binding.txtScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score,this);

                    FLAG = 1;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);



                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) binding.rbThree.getText();
                    wrongDialog.wrongDialog(correctAnswer,this);

                    FLAG = 2;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);




                }
                break;
            case 4:
                if (currentQuestions.getAnswerNr() == answerNr) {

                    binding.rbFour.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    binding.rbFour.setTextColor(Color.WHITE);


                    correctAns++;


                    score += 10;
                    binding.txtScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score,this);

                    FLAG = 1;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);




                } else {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;


                    String correctAnswer = (String) binding.rbFour.getText();
                    wrongDialog.wrongDialog(correctAnswer,this);

                    FLAG = 2;
//                    playAudioForAnswers.setAudioforAnswer(FLAG);




                }
                break;
        }
        if (questionCounter == questionTotalCount) {
            binding.btnNext.setText("Confirm and Finish");
        }
    }

    void changetoIncorrectColor(RadioButton rbselected) {
        rbselected.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_background));

        rbselected.setTextColor(Color.WHITE);
    }



    //  the timer code

    private void startCountDown(){

        countDownTimer = new CountDownTimer(timeleftinMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinMillis = millisUntilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {

                timeleftinMillis = 0;
                updateCountDownText();

            }
        }.start();

    }



    private void updateCountDownText(){

        int minutes = (int) (timeleftinMillis/1000) /60;
        int seconds = (int) (timeleftinMillis/1000) % 60;

        //  String timeFormatted = String.format(Locale.getDefault(),"02d:%02d",minutes,seconds);

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes, seconds);
        binding.txtTime.setText(timeFormatted);


        if (timeleftinMillis < 10000){


            binding.txtTime.setTextColor(ContextCompat.getColor(this,R.color.Red));

            FLAG = 3;
//            playAudioForAnswers.setAudioforAnswer(FLAG);


        }else {
            binding.txtTime.setTextColor(ContextCompat.getColor(this,R.color.White));
        }


        if (timeleftinMillis == 0 ){


            Toast.makeText(this, "Times Up!", Toast.LENGTH_SHORT).show();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    timerDialog.timerDialog();

                }
            },2000);

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BUGBUG","onRestart() in QuizActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUGBUG","onStop() in QuizActivity");
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("BUGBUG","onPause() in QuizActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BUGBUG","onResume() in QuizActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BUGBUG","onStart() in QuizActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        Log.i("BUGBUG","onDestroy() in QuizActivity");


    }


    private void finalResult(){

        Intent resultData = new Intent(MainActivity.this, ResultActivity.class);

        resultData.putExtra("UserScore",score);
        resultData.putExtra("TotalQuestion",questionTotalCount);
        resultData.putExtra("CorrectQues",correctAns);
        resultData.putExtra("WrongQues",wrongAns);

        resultData.putExtra("Category",categoryValue);

        startActivity(resultData);

    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){

            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();
    }
}