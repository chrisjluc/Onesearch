package chrisjluc.onesearch.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtils {
    private static Boolean mIsTablet;
    private static Boolean mIsSmallScreen;

    public static boolean isTablet(Context context) {
        if (mIsTablet == null) {
            boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
            boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
            mIsTablet = (xlarge || large);
        }
        return mIsTablet;
    }

    public static boolean isSmallScreen(Context context) {
        if(mIsSmallScreen == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mIsSmallScreen = size.x <= 768;
        }
        return mIsSmallScreen;
    }
}
