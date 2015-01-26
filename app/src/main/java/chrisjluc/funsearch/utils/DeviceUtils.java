package chrisjluc.funsearch.utils;

import android.content.Context;
import android.content.res.Configuration;

public class DeviceUtils {
    private static Boolean mIsTablet;
    public static boolean isTablet(Context context) {
        if (mIsTablet == null) {
            boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
            boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
            mIsTablet = (xlarge || large);
        }
        return mIsTablet;
    }
}
