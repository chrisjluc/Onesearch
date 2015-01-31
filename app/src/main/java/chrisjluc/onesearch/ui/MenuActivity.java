package chrisjluc.onesearch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.WordSearchManager;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.models.GameType;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SignInButton signin = (SignInButton) findViewById(R.id.bMenuSignIn);
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            signin.setOnClickListener(this);
            signin.setVisibility(View.VISIBLE);
        }

        ((TextView) findViewById(R.id.tvTitle)).setTextSize(36);
        findViewById(R.id.bMenuEasy).setOnClickListener(this);
        findViewById(R.id.bMenuMedium).setOnClickListener(this);
        findViewById(R.id.bMenuHard).setOnClickListener(this);
        findViewById(R.id.bMenuAdvanced).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bMenuSignIn) {
            mInSignInFlow = true;
            mSignInClicked = true;
            mGoogleApiClient.connect();
            return;
        }
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

    @Override
    public void onConnected(Bundle bundle) {
        findViewById(R.id.bMenuSignIn).setVisibility(View.GONE);
    }
}
