package chrisjluc.onesearch.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameAchievement;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.ui.components.GameButton;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class ResultsActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    public final static int RESULT_REPLAY_GAME = 0;
    public final static int RESULT_EXIT_TO_MENU = 1;
    public final static String ACTION_IDENTIFIER = "action_identifier";
    public final static int REQUEST_LEADERBOARD = 2;
    public final static int REQUEST_ACHIEVEMENTS = 3;

    // Shared pref constants
    public final static String PREF_NAME = "results_and_game_metrics";
    public final static String SCORE_PREFIX = "score_in_mode_";
    public final static String COMPLETED_ROUND_PREFIX = "completed_rounds_in_mode_";

    private String leaderboardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        findViewById(R.id.bReplay).setOnClickListener(this);
        findViewById(R.id.bReturnMenu).setOnClickListener(this);

        SignInButton signin = (SignInButton) findViewById(R.id.bResultSignIn);
        GameButton showLeaderBoard = (GameButton) findViewById(R.id.bShowLeaderBoards);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt("score");
            int skipped = extras.getInt("skipped");

            GameMode gameMode = WordSearchManager.getInstance().getGameMode();
            switch (gameMode.getDifficulty()) {
                case GameDifficulty.Easy:
                    leaderboardId = getResources().getString(R.string.leaderboard_highest_scores__easy);
                    break;
                case GameDifficulty.Medium:
                    leaderboardId = getResources().getString(R.string.leaderboard_highest_scores__medium);
                    break;
                case GameDifficulty.Hard:
                    leaderboardId = getResources().getString(R.string.leaderboard_highest_scores__hard);
                    break;
                case GameDifficulty.Advanced:
                    leaderboardId = getResources().getString(R.string.leaderboard_highest_scores__advanced);
                    break;
            }

            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            int numRounds = prefs.getInt(COMPLETED_ROUND_PREFIX + gameMode.getDifficulty(), 0);
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(COMPLETED_ROUND_PREFIX + gameMode.getDifficulty(), ++numRounds);
            editor.commit();

            handleScore(gameMode, score);
            handleLeaderboard(score);
        }
    }

    private void handleScore(GameMode gameMode, int score) {

        TextView scoreTextView = (TextView) findViewById(R.id.tvScoreResult);
        scoreTextView.setText(Integer.toString(score));

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int bestScore = prefs.getInt(SCORE_PREFIX + gameMode.getDifficulty(), 0);
        if (score > bestScore) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(SCORE_PREFIX + gameMode.getDifficulty(), score);
            editor.commit();

            findViewById(R.id.tvBestScoreResultNotify).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(score));
        } else {
            ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(bestScore));
        }

        handleAchievement(gameMode, Math.max(score, bestScore));
    }

    private void handleLeaderboard(int score) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, leaderboardId, score);
        }
    }

    private void handleAchievement(GameMode gameMode, int score) {
        SparseArray<String> achievementsMap = null;
        switch (gameMode.getDifficulty()) {
            case GameDifficulty.Easy:
                achievementsMap = GameAchievement.EASYACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Medium:
                achievementsMap = GameAchievement.MEDIUMACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Hard:
                achievementsMap = GameAchievement.HARDACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Advanced:
                achievementsMap = GameAchievement.ADVANCEDACHIEVEMENTSMAP;
                break;
        }
        if (achievementsMap == null) return;
        for (int i = 0; i < achievementsMap.size(); i++) {
            int key = achievementsMap.keyAt(i);
            if (key > score) break;
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                Games.Achievements.unlock(mGoogleApiClient, achievementsMap.get(key, ""));
            else
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bResultSignIn:
                mInSignInFlow = true;
                mSignInClicked = true;
                mGoogleApiClient.connect();
                return;
            case R.id.bShowLeaderBoards:
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && leaderboardId != null) {
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                            leaderboardId), REQUEST_LEADERBOARD);
                }
                return;
            case R.id.bShowAchievements:
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && leaderboardId != null) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
                }
                return;

        }
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

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent(getApplicationContext(), WordSearchActivity.class);
        resultIntent.putExtra(ACTION_IDENTIFIER, RESULT_EXIT_TO_MENU);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        findViewById(R.id.bResultSignIn).setVisibility(View.GONE);
        findViewById(R.id.bShowLeaderBoards).setVisibility(View.VISIBLE);
        findViewById(R.id.bShowLeaderBoards).setOnClickListener(this);
        findViewById(R.id.bShowAchievements).setVisibility(View.VISIBLE);
        findViewById(R.id.bShowAchievements).setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.bResultSignIn).setVisibility(View.VISIBLE);
        findViewById(R.id.bShowLeaderBoards).setVisibility(View.GONE);
        findViewById(R.id.bShowAchievements).setVisibility(View.GONE);
        findViewById(R.id.bResultSignIn).setOnClickListener(this);
    }
}
