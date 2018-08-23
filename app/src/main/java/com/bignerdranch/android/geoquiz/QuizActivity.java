            package com.bignerdranch.android.geoquiz;

            import android.app.Activity;
            import android.content.Context;
            import android.content.Intent;
            import android.support.v7.app.AppCompatActivity;
            import android.os.Bundle;
            import android.view.View;
            import android.widget.Button;
            import android.widget.TextView;
            import android.widget.Toast;

            public class QuizActivity extends AppCompatActivity {
                private Button mTrueButton;
                private Button mFalseButton;
                private Button mNextButton;
                private Button mCheatButton;
                private TextView mQuestionTextView;

                private static final String TAG = "QuizActivity";
                private static final String KEY_INDEX = "index";
                private static final int REQUEST_CODE_CHEAT = 0;


                private Question[] mQuestionBank = new Question[]{
                        new Question(R.string.question_australia, true),
                        new Question(R.string.question_oceans, true),
                        new Question(R.string.question_mideast, false),
                        new Question(R.string.question_africa, false),
                        new Question(R.string.question_americas, true),
                        new Question(R.string.question_asia, true),
                };

                private int mCurrentIndex = 0;
                private boolean mIsCheater;

                @Override
                public void onSaveInstanceState(Bundle savedInstanceState){
                    super.onSaveInstanceState(savedInstanceState);
                    savedInstanceState.putInt("index", mCurrentIndex);
                }

                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data){
                    if (resultCode != Activity.RESULT_OK) {
                        return;
                    }
                    if (requestCode == REQUEST_CODE_CHEAT) {
                        if (data == null) {
                            return;
                        }
                        mIsCheater = CheatActivity.wasAnswerShown(data);
                    }
                }



                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_quiz);

                    mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
                    updateQuestion();

                    if (savedInstanceState != null){
                        mCurrentIndex = savedInstanceState.getInt("index", 0);
                    }


                    mTrueButton = (Button) findViewById(R.id.true_button);
                    mTrueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            checkAnswer(true);
                         }
                    });

                    mFalseButton = (Button) findViewById(R.id.false_button);
                    mFalseButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v){
                            checkAnswer(false);
                        }
                    });

                    mNextButton = (Button) findViewById(R.id.next_button);
                    mNextButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                        mIsCheater = false;
                        updateQuestion();
                    }
                    });

                    mCheatButton = (Button)findViewById(R.id.cheat_button);
                    mCheatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        // Start CheatActivity
                        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                        Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                            startActivityForResult(intent, REQUEST_CODE_CHEAT);
                        }
                    });


                    //updateQuestion();
                }

                private void updateQuestion(){
                    int question = mQuestionBank[mCurrentIndex].getTextResId();
                    mQuestionTextView.setText(question);
                }

                private void checkAnswer(boolean userPressedTrue){
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

                    int messageResId = 0;

                    if (mIsCheater){
                        messageResId = R.string.judgement_toast;
                    } else{
                        if (userPressedTrue == answerIsTrue){
                            messageResId = R.string.correct_toast;
                        } else{
                            messageResId = R.string.incorrect_toast;
                        }
                    }

                    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

                }
            }
