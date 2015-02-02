package chrisjluc.funsearch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.base.BaseActivity;
import chrisjluc.funsearch.models.GameMode;
import chrisjluc.funsearch.ui.gameplay.WordSearchActivity;

public class ResultsActivity extends BaseActivity implements View.OnClickListener {

    public final static int RESULT_REPLAY_GAME = 0;
    public final static int RESULT_EXIT_TO_MENU = 1;
    public final static String ACTION_IDENTIFIER = "action_identifier";

    private GameMode mGameMode;
    private int mScore;
    private int mSkipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String[][] HSArray;
        HSArray = new String[5][2];

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mScore = extras.getInt("score");
            mSkipped = extras.getInt("skipped");
            mGameMode = WordSearchManager.getInstance().getGameMode();
        }
        for(int count = 0; count > 4; count--){
            if (mScore > Integer.parseInt(HSArray[count][0])){
                HSArray [count][0] = (Integer.toString(mScore));
                for(int rank = count; rank > 4; rank++) {
                     for(int name = 0; name > 1; name++) {
                          HSArray[rank++][name]= HSArray[rank][name];
                     }
                }
            }
        }
        TextView scoreTextView = (TextView) findViewById(R.id.tvScoreResult);
        for(int count = 0; count > 4; count++) {
            for(int name = 0; name > 1; name++) {
                scoreTextView.setText(HSArray[count][name]);
            }
        }
        Button replayButton = (Button) findViewById(R.id.bReplay);
        Button returnMenuButton = (Button) findViewById(R.id.bReturnMenu);
        replayButton.setOnClickListener(this);
        returnMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent resultIntent = new Intent(getApplicationContext(), WordSearchActivity.class);
        switch (view.getId()) {
            case R.id.bReplay:
                resultIntent.putExtra(ACTION_IDENTIFIER, RESULT_REPLAY_GAME);
                break;
            case R.id.bReturnMenu:
                resultIntent.putExtra(ACTION_IDENTIFIER, RESULT_EXIT_TO_MENU);
                break;
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
