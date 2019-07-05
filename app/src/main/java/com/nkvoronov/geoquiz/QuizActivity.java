package com.nkvoronov.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mProgressTextView;
    private TextView mQuestionTextView;
    private Button mResButton;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private int mCountTrueAnswer = 0;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT = "count";

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_COUNT, mCountTrueAnswer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCountTrueAnswer = savedInstanceState.getInt(KEY_COUNT, 0);
        }

        mProgressTextView = findViewById(R.id.progress_text_view);
        mQuestionTextView = findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mQuestionBank.length - 1) {
                    mCurrentIndex++;
                    updateQuestion();
                }
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GeoQuiz", "TrueButton");
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GeoQuiz", "FalseButton");
                checkAnswer(false);
            }
        });

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex--;
                    updateQuestion();
                }
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mQuestionBank.length - 1) {
                    mCurrentIndex++;
                    updateQuestion();
                }
            }
        });

        mResButton = findViewById(R.id.res_button);
        mResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResults();
            }
        });


        updateQuestion();
    }

    private void updateQuestion() {
        mPrevButton.setEnabled(false);
        //mPrevButton.setEnabled(mCurrentIndex > 0);
        mNextButton.setEnabled(mCurrentIndex < mQuestionBank.length -1);
        mResButton.setEnabled(mCurrentIndex == mQuestionBank.length -1);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mProgressTextView.setText(Integer.toString(mCurrentIndex + 1) + "/" + Integer.toString(mQuestionBank.length));
        mQuestionTextView.setText(question);
        updateAnswerButton(true);
    }

    private void updateAnswerButton(boolean btStats) {
        mTrueButton.setEnabled(btStats);
        mFalseButton.setEnabled(btStats);
    }

    private void getResults() {
        Toast mToast;
        int res;
        res = (int)((mCountTrueAnswer * 100)/mQuestionBank.length);
        mToast = Toast.makeText(this, "Answers - " + Integer.toString(res) + "%", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 150);
        mToast.show();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        int topToast;
        int yOffsetToast;
        Toast mToast;
        updateAnswerButton(false);
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mCountTrueAnswer++;
            topToast = Gravity.BOTTOM;
            yOffsetToast = 0;
        } else {
            messageResId = R.string.incorrect_toast;
            topToast = Gravity.TOP;
            yOffsetToast = 150;
        }
        mToast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        mToast.setGravity(topToast, 0, yOffsetToast);
        mToast.show();
    }
}
