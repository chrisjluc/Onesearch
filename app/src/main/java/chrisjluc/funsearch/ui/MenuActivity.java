package chrisjluc.funsearch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.base.BaseActivity;
import chrisjluc.funsearch.models.GameDifficulty;
import chrisjluc.funsearch.models.GameMode;
import chrisjluc.funsearch.models.GameType;
import chrisjluc.funsearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.bMenuEasy).setOnClickListener(this);
        findViewById(R.id.bMenuMedium).setOnClickListener(this);
        findViewById(R.id.bMenuHard).setOnClickListener(this);
        findViewById(R.id.bMenuAdvanced).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        WordSearchManager.nullify();
        String gd = null;
        switch (view.getId()) {
            case R.id.bMenuEasy:
                gd = GameDifficulty.Easy;
                break;
            case R.id.bMenuMedium:
                gd = GameDifficulty.Medium;
                break;
            case R.id.bMenuHard:
                gd = GameDifficulty.Hard;
                break;
            case R.id.bMenuAdvanced:
                gd = GameDifficulty.Advanced;
                break;
        }
        WordSearchManager wsm = WordSearchManager.getInstance();
        wsm.setGameMode(new GameMode(GameType.Timed, gd, 60000));
        wsm.buildWordSearches();
        Intent i = new Intent(getApplicationContext(), WordSearchActivity.class);
        startActivity(i);
    }
}
