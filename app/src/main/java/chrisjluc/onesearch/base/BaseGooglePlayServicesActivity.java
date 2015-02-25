package chrisjluc.onesearch.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.ui.ResultsActivity;

public class BaseGooglePlayServicesActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static int RC_SIGN_IN = 9001;
    protected boolean mInSignInFlow = false;
    protected boolean mResolvingConnectionFailure = false;
    protected boolean mAutoStartSignInflow = false;
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
                    RC_SIGN_IN, "Failed to sign in");
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        // Push high scores
        if (mSignInClicked) {
            SharedPreferences prefs = getSharedPreferences(ResultsActivity.PREF_NAME, MODE_PRIVATE);
            String easyLeaderboardId = getResources().getString(R.string.leaderboard_highest_scores__easy);
            String mediumLeaderboardId = getResources().getString(R.string.leaderboard_highest_scores__medium);
            String hardLeaderboardId = getResources().getString(R.string.leaderboard_highest_scores__hard);
            String advancedLeaderboardId = getResources().getString(R.string.leaderboard_highest_scores__advanced);

            int easyScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Easy, 0);
            int mediumScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Medium, 0);
            int hardScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Hard, 0);
            int advancedScore = prefs.getInt(ResultsActivity.SCORE_PREFIX + GameDifficulty.Advanced, 0);

            if (easyScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, easyLeaderboardId, easyScore);
            if (mediumScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, mediumLeaderboardId, mediumScore);
            if (hardScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, hardLeaderboardId, hardScore);
            if (advancedScore > 0)
                Games.Leaderboards.submitScore(mGoogleApiClient, advancedLeaderboardId, advancedScore);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}
