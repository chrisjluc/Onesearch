package chrisjluc.onesearch.adapters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import chrisjluc.onesearch.ui.gameplay.WordSearchFragment;

/**
 * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class WordSearchPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public WordSearchPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentTags = new SparseArray<String>();
        mFragmentManager = fm;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return Fragment.instantiate(mContext, WordSearchFragment.class.getName(), null);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragmentFromCurrentItem(int currentItem) {
        String tag = mFragmentTags.get(currentItem - 2);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Integer.toString(position);
    }
}