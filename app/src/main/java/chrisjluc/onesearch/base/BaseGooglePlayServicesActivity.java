package chrisjluc.onesearch.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.BaseGameUtils;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.ui.ResultsActivity;

public class BaseGooglePlayServicesActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    protected static int RC_SIGN_IN = 9001;
    private static String BGP_PREF_MAME = "base_google_play";
    private static String FIRST_CONNECT = "first_connect";
    protected boolean mInSignInFlow = false;
    protected boolean mResolvingConnectionFailure = false;
    protected boolean mAutoStartSignInflow = true;
    protected boolean mSignInClicked = false;
    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;

            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.sign_in_failed));
        }
    }

    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.sign_in_failed);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        SharedPreferences prefs = getSharedPreferences(BGP_PREF_MAME, MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_CONNECT, true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = getSharedPreferences(BGP_PREF_MAME, MODE_PRIVATE).edit();
            editor.putBoolean(FIRST_CONNECT, false);
            editor.apply();

            // Push high scores
            prefs = getSharedPreferences(ResultsActivity.PREF_NAME, MODE_PRIVATE);
            String easyLeaderboardId = getString(R.string.leaderboard_highest_scores__easy);
            String mediumLeaderboardId = getString(R.string.leaderboard_highest_scores__medium);
            String hardLeaderboardId = getString(R.string.leaderboard_highest_scores__hard);
//            String advancedLeaderboardId = getString(R.string.leaderboard_highest_scores__advanced);

            int easyScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Easy, 0);
            int mediumScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Medium, 0);
            int hardScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Hard, 0);
//            int advancedScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Advanced, 0);

            if (easyScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, easyLeaderboardId, easyScore);
            if (mediumScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, mediumLeaderboardId, mediumScore);
            if (hardScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, hardLeaderboardId, hardScore);
//            if (advancedScore > 0)
//                Games.Leaderboards.submitScore(mGoogleApiClient, advancedLeaderboardId, advancedScore);

            loadScoreOfLeaderBoardIfLarger(easyLeaderboardId, easyScore, GameDifficulty.Easy);
            loadScoreOfLeaderBoardIfLarger(mediumLeaderboardId, mediumScore, GameDifficulty.Medium);
            loadScoreOfLeaderBoardIfLarger(hardLeaderboardId, hardScore, GameDifficulty.Hard);
        }
    }

    private void loadScoreOfLeaderBoardIfLarger(final String leaderboardId, final int currentScore, final String gameDifficulty) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(mGoogleApiClient, leaderboardId, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
            @Override
            public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
                if (isScoreResultValid(scoreResult)) {
                    // here you can get the score like this
                    int score = (int) scoreResult.getScore().getRawScore();
                    if (score > currentScore) {
                        SharedPreferences.Editor editor = getSharedPreferences(ResultsActivity.PREF_NAME, MODE_PRIVATE).edit();
                        editor.putInt(ResultsActivity.SCORE_PREFIX + gameDifficulty, score);
                        editor.apply();
                    }
                }
            }
        });
    }

    private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {
        return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}
