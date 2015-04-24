package net.mwales.gotquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;


public class GotActivity extends ActionBarActivity {

    private Button mTrueButton;

    private Button mFalseButton;

    private Button mNextButton;

    private Button mCheatButton;

    private Boolean mCheatActivated;

    private int mQuestionIndex = 0;

    private static final String BUNDLE_INDEX = "SavedIndex";

    private static final String TAG = "GotQuiz";



    private QuizQuestion[] mQuestions = new QuizQuestion[] {
            new QuizQuestion(R.string.question_first, false),
            new QuizQuestion(R.string.question_second, true),
            new QuizQuestion(R.string.question_third, true),
            new QuizQuestion(R.string.question_four, true),
            new QuizQuestion(R.string.question_five, false)
    };

    private TextView mQuestionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_got);

        Log.d(TAG, "onCreate started");

        if (savedInstanceState != null) {
            mQuestionIndex = savedInstanceState.getInt(BUNDLE_INDEX);
            Log.d(TAG, "Loaded State.  Index=" + mQuestionIndex);
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mQuestionText = (TextView) findViewById(R.id.quiz_question);
        mNextButton = (Button) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        displayQuestion(mQuestionIndex);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotActivity.this.displayResult(mQuestions[mQuestionIndex].getAnswerTrue());

                GotActivity.this.nextQuestion();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotActivity.this.displayResult(!mQuestions[mQuestionIndex].getAnswerTrue());

                GotActivity.this.nextQuestion();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotActivity.this.nextQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(GotActivity.this, CheatActivity.class);
                i.putExtra(CheatActivity.CHEAT_ANSWER_KEY, mQuestions[mQuestionIndex].getAnswerTrue());
                //startActivity(i);
                startActivityForResult(i, 0);
            }
        });
    }

    public void displayResult(boolean correct)
    {
        Log.d(TAG, "displayResult started");

        if (correct)
        {
            if (mQuestions[mQuestionIndex].getCheated())
            {
                Toast.makeText(this, R.string.judgement_message, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.incorrect, Toast.LENGTH_SHORT).show();
        }
    }

    public void nextQuestion()
    {
        Log.d(TAG, "nextQuestion started");

        mQuestionIndex = (mQuestionIndex + 1) % mQuestions.length;
        displayQuestion(mQuestionIndex);
    }

    protected void displayQuestion(int index)
    {
        Log.d(TAG, "displayQuestion started");
        mQuestionText.setText(mQuestions[index].getQuestionId());

        mCheatActivated = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_got, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart started");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause started");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume started");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy started");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop started");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_INDEX, mQuestionIndex);

        Log.d(TAG, "Saved State.  Index=" + mQuestionIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
        {
            Log.e(TAG, "Null activity result received");
            return;
        }

        if (requestCode != 0)
        {
            Log.e(TAG, "Activy result request code unexpected");
        }

        if (requestCode != RESULT_OK)
        {
            Log.e(TAG, "Activity result code unexpected");
        }

        mCheatActivated = data.getBooleanExtra(CheatActivity.CHEAT_ACTIVATED, false);

        if (mCheatActivated)
        {
            mQuestions[mQuestionIndex].setCheated(true);
            Log.d(TAG, "Cheat detected");
        }
    }
}
