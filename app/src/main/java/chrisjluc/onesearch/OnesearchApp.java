package chrisjluc.onesearch;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class OnesearchApp extends Application {
    private static final String PROPERTY_ID = "UA-50321210-2";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Enabling Advertising Features in Google Analytics allows you to take advantage of
        // Remarketing, Demographics & Interests reports, and more.
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);

        if(BuildConfig.DEBUG) {
            GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            GoogleAnalytics.getInstance(this).setDryRun(true);
        }
    }
}
