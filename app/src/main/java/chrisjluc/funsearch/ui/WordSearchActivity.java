package chrisjluc.funsearch.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.adapters.SectionsPagerAdapter;
import chrisjluc.funsearch.interfaces.WordFoundListener;


public class WordSearchActivity extends Activity implements WordFoundListener, View.OnClickListener {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView mTimerTextView;
    private TextView mScoreTextView;
    private CountDownTimer mCountDownTimer;

    public static int currentItem;
    private long mTimeRemaining;
    private int mScore;
    private final static int TIMER_GRANULARITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch_activity);

        Button mSkipButton = (Button) findViewById(R.id.bSkip);
        Button mPauseButton = (Button) findViewById(R.id.bPause);
        mSkipButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mTimerTextView = (TextView) findViewById(R.id.tvTimer);
        mScoreTextView = (TextView) findViewById(R.id.tvScore);

        mScoreTextView.setText("0");

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        /*
          The {@link android.support.v4.view.PagerAdapter} that will provide
          fragments for each of the sections. We use a
          {@link FragmentPagerAdapter} derivative, which will keep every
          loaded fragment in memory. If this becomes too memory intensive, it
          may be best to switch to a
          {@link android.support.v13.app.FragmentStatePagerAdapter}.
         */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (WordSearchViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        currentItem = 0;
        mScore = 0;
        mTimeRemaining = 30000;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
    }

    private void resumeGameplay() {
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
    }

    private void pauseGameplay() {
        stopCountDownTimer();
        // Show pause dialog
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSkip:
                mViewPager.setCurrentItem(++currentItem);
                break;
            case R.id.bPause:
                pauseGameplay();
                break;
        }
    }

    @Override
    public void notifyWordFound() {
        mViewPager.setCurrentItem(++currentItem);
        mScoreTextView.setText(Integer.toString(++mScore));
    }

    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    @Override
    protected void onResume() {
        setFullscreen();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pauseGameplay();
        super.onPause();
    }

    private void setupCountDownTimer(final long timeinMS) {
        mCountDownTimer = new CountDownTimer(timeinMS, TIMER_GRANULARITY) {

            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(Long.toString(millisUntilFinished / 1000));
                mTimeRemaining = millisUntilFinished;
            }

            public void onFinish() {
                // Exit gameplay
            }
        };
    }

    private void startCountDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }
}
