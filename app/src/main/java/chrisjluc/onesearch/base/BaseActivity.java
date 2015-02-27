package chrisjluc.onesearch.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import chrisjluc.onesearch.OnesearchApp;

public class BaseActivity extends Activity {

    protected int categoryId = -1;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tracker = ((OnesearchApp) getApplication()).getTracker(
                OnesearchApp.TrackerName.APP_TRACKER);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullscreen();
    }

    protected void analyticsTrackEvent(int actionId) {
        if (categoryId != -1 && actionId != -1)
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(getString(categoryId))
                    .setAction(getString(actionId))
                    .build());
    }

    protected void analyticsTrackScreen(String screenName) {
        if (screenName != null && !screenName.isEmpty()) {
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.AppViewBuilder().build());
        }
    }

    protected void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();
    }
}
