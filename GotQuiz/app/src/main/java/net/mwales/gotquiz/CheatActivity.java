package net.mwales.gotquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mwales on 4/22/15.
 */
public class CheatActivity extends Activity
{

    public static final String CHEAT_ANSWER_KEY = "net.mwales.CheatAnswer";

    public static final String CHEAT_ACTIVATED = "net.mwales.CheatActivated";

    public Boolean getCheatAnswer()
    {
        return mCheatAnswer;
    }

    Boolean mCheatAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheater);

        Button cheatButton = (Button) findViewById(R.id.cheat_activate_button);

        mCheatAnswer = getIntent().getBooleanExtra(CheatActivity.CHEAT_ANSWER_KEY, false);

        cheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView tv = (TextView) findViewById(R.id.cheat_answer);

                if (CheatActivity.this.getCheatAnswer())
                {
                    tv.setText(R.string.true_button);
                }
                else
                {
                    tv.setText(R.string.false_button);
                }

                Intent i = new Intent();
                i.putExtra(CHEAT_ACTIVATED, true);
                setResult(RESULT_OK, i);
            }
        });
    }
}
