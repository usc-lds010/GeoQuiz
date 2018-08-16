        package com.bignerdranch.android.geoquiz;

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


            private Question[] mQuestionBank = new Question[]{
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true),
            };

            private int mCurrentIndex = 0;

            @Override
            public void onSaveInstanceState(Bundle savedInstanceState){
                super.onSaveInstanceState(savedInstanceState);
                savedInstanceState.putInt("index", mCurrentIndex);
            }


            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_quiz);

                mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

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
                        startActivity(intent);
                    }
                });


                updateQuestion();
            }

            private void updateQuestion(){
                int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);
            }

            private void checkAnswer(boolean userPressedTrue){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

                int messageResId = 0;

                if (userPressedTrue == answerIsTrue){
                    messageResId = R.string.correct_toast;
                } else{
                    messageResId = R.string.incorrect_toast;
                }

                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                        .show();
            }

        }
