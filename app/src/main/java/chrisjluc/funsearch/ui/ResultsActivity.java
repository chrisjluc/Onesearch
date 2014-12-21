package chrisjluc.funsearch.ui;

import android.os.Bundle;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.base.BaseActivity;
import chrisjluc.funsearch.models.GameMode;

public class ResultsActivity extends BaseActivity {

    private GameMode mGameMode;
    private int mScore;
    private int mSkipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ResultsFragment())
                    .commit();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGameMode = (GameMode) extras.get("mode");
            mScore = extras.getInt("score");
            mSkipped = extras.getInt("skipped");
        }
    }
}
