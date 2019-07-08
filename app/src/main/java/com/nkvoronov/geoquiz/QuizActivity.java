package com.nkvoronov.geoquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private Button mCheatButton;
    private Button mNextButton;
    private TextView mProgressTextView;
    private TextView mQuestionTextView;
    private Button mResultButton;

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
    private boolean mIsCheater;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT = "count";
    private static final String KEY_CHEATER = "cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_COUNT, mCountTrueAnswer);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
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
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
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

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mQuestionBank.length - 1) {
                    mCurrentIndex++;
                    mIsCheater = false;
                    updateQuestion();
                }
            }
        });

        mResultButton = findViewById(R.id.result_button);
        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResults();
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        mNextButton.setEnabled(mCurrentIndex < mQuestionBank.length -1);
        mResultButton.setEnabled(mCurrentIndex == mQuestionBank.length -1);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mProgressTextView.setText(String.format("%d / %d", mCurrentIndex + 1, mQuestionBank.length));
        mQuestionTextView.setText(question);
        updateAnswerButton(true);
    }

    private void updateAnswerButton(boolean btStats) {
        mTrueButton.setEnabled(btStats);
        mFalseButton.setEnabled(btStats);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        Toast mToast;
        updateAnswerButton(false);
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                mCountTrueAnswer++;
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        mToast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void getResults() {
        Toast mToast;
        int res;
        res = (int)((mCountTrueAnswer * 100)/mQuestionBank.length);
        mToast = Toast.makeText(this, getString(R.string.result_string, res), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 150);
        mToast.show();
    }
}
