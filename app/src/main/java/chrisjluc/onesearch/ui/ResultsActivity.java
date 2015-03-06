package chrisjluc.onesearch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameAchievement;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class ResultsActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    public final static int REQUEST_LEADERBOARD = 2;
    public final static int REQUEST_ACHIEVEMENTS = 3;

    // Shared pref constants
    public final static String PREF_NAME = "results_and_game_metrics";
    public final static String SCORE_PREFIX = "score_in_mode_";
    public final static String COMPLETED_ROUND_PREFIX = "completed_rounds_in_mode_";
    public final static String HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX = "highest_score_for_achievement_in_mode_";

    private String mLeaderboardId;
    private int mScore = -1;
    private int mPreviousBestScore = -1;
    private int mSkipped = -1;
    private GameMode mGameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_results_screen;
        setContentView(R.layout.activity_results);
        findViewById(R.id.bReplay).setOnClickListener(this);
        findViewById(R.id.bReturnMenu).setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mScore = extras.getInt("score");
            mSkipped = extras.getInt("skipped");

            mGameMode = WordSearchManager.getInstance().getGameMode();
            if (mGameMode != null) {
                switch (mGameMode.getDifficulty()) {
                    case GameDifficulty.Easy:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__easy);
                        break;
                    case GameDifficulty.Medium:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__medium);
                        break;
                    case GameDifficulty.Hard:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__hard);
                        break;
                    case GameDifficulty.Advanced:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__advanced);
                        break;
                }

                // Track number of played rounds
                SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                int numRounds = prefs.getInt(COMPLETED_ROUND_PREFIX + mGameMode.getDifficulty(), 0);
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(COMPLETED_ROUND_PREFIX + mGameMode.getDifficulty(), ++numRounds);
                editor.apply();
            }

            updateSavedScoreAndRenderViews();
        }
    }

    private void updateSavedScoreAndRenderViews() {

        TextView scoreTextView = (TextView) findViewById(R.id.tvScoreResult);
        scoreTextView.setText(Integer.toString(mScore));

        if (mGameMode != null) {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            int bestScore = prefs.getInt(SCORE_PREFIX + mGameMode.getDifficulty(), 0);
            mPreviousBestScore = bestScore;
            if (mScore > bestScore) {
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(SCORE_PREFIX + mGameMode.getDifficulty(), mScore);
                editor.apply();

                findViewById(R.id.tvBestScoreResultNotify).setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(200);
                anim.setStartOffset(0);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(6);
                findViewById(R.id.tvBestScoreResultNotify).startAnimation(anim);
                ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(mScore));
            } else {
                ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(bestScore));
            }
        }
    }

    private void updateLeaderboard() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, mLeaderboardId, mScore);
        }
    }

    private void updateAchievements() {
        int score = Math.max(mPreviousBestScore, mScore);
        SparseArray<String> achievementsMap = null;
        switch (mGameMode.getDifficulty()) {
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
        // Remember the highest score associated with achievement unlocked, so we don't unlock it multiple times
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int highestScore = prefs.getInt(HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX + mGameMode.getDifficulty(), 0);

        for (int i = 0; i < achievementsMap.size(); i++) {
            int key = achievementsMap.keyAt(i);
            if (key > score) break;
            if (key <= highestScore) continue;
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                Games.Achievements.unlock(mGoogleApiClient, achievementsMap.get(key, ""));
        }

        if (score > highestScore) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX + mGameMode.getDifficulty(), score);
            editor.apply();
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
                analyticsTrackEvent(R.string.ga_click_leaderboard);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLeaderboardId != null) {
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                            mLeaderboardId), REQUEST_LEADERBOARD);
                }
                return;
            case R.id.bShowAchievements:
                analyticsTrackEvent(R.string.ga_click_achievement);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
                }
                return;

        }
        switch (view.getId()) {
            case R.id.bReplay:
                analyticsTrackEvent(R.string.ga_click_replay);
                Intent intent = new Intent(getApplicationContext(), WordSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.bReturnMenu:
                analyticsTrackEvent(R.string.ga_click_return_to_menu);
                break;
        }
        finish();
    }

    @Override
    protected void onStart() {
        // Prevent from trying to force connection if they haven't signed in here
        mAutoStartSignInflow = false;
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        analyticsTrackScreen(getString(categoryId));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (mGameMode != null) {
            findViewById(R.id.bResultSignIn).setVisibility(View.GONE);
            findViewById(R.id.bShowLeaderBoards).setVisibility(View.VISIBLE);
            findViewById(R.id.bShowLeaderBoards).setOnClickListener(this);
            findViewById(R.id.bShowAchievements).setVisibility(View.VISIBLE);
            findViewById(R.id.bShowAchievements).setOnClickListener(this);
            updateLeaderboard();
            updateAchievements();
        }
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
