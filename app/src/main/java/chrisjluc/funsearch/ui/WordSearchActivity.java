package chrisjluc.funsearch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.adapters.SectionsPagerAdapter;
import chrisjluc.funsearch.base.BaseActivity;
import chrisjluc.funsearch.models.GameDifficulty;
import chrisjluc.funsearch.models.GameMode;
import chrisjluc.funsearch.models.GameType;

public class WordSearchActivity extends BaseActivity implements WordSearchGridView.WordFoundListener, PauseDialogFragment.PauseDialogListener, View.OnClickListener {

    private enum GameState {START, PLAY, PAUSE}

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView mTimerTextView;
    private TextView mScoreTextView;
    private CountDownTimer mCountDownTimer;
    private final PauseDialogFragment mPauseDialogFragment = new PauseDialogFragment();

    private GameState mGameState;
    public static int currentItem;
    private final static int TIMER_GRANULARITY = 50;
    private long mTimeRemaining;
    private long mStartTime;
    private int mScore;
    private int mSkipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: DELETE WHEN MENUACTIVITY IS CREATED
        WordSearchManager.getInstance().setGameMode(new GameMode(GameType.Timed, GameDifficulty.Easy, 30000));

        setContentView(R.layout.wordsearch_activity);
        mStartTime = WordSearchManager.getInstance().getGameMode().getTime();
        mGameState = GameState.START;
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
        mSkipped = 0;
        mTimeRemaining = mStartTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
    }


    private void pauseGameplay() {
        if (mGameState == GameState.PAUSE)
            return;
        mGameState = GameState.PAUSE;
        stopCountDownTimer();
        mPauseDialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSkip:
                mViewPager.setCurrentItem(++currentItem);
                mSkipped++;
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

    @Override
    public void onDialogQuit() {
        // Exit to the main menu
    }

    @Override
    public void onDialogResume() {
        mGameState = GameState.PLAY;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
        setFullscreen();
    }

    @Override
    public void onDialogRestart() {
        mGameState = GameState.PLAY;
        mScore = 0;
        mSkipped = 0;
        mTimeRemaining = mStartTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
        setFullscreen();
        mScoreTextView.setText("0");
        mViewPager.setCurrentItem(++currentItem);
    }

    @Override
    protected void onResume() {
        if (mGameState == GameState.START)
            mGameState = GameState.PLAY;
        else
            pauseGameplay();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopCountDownTimer();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        pauseGameplay();
    }

    private void setupCountDownTimer(final long timeinMS) {
        mCountDownTimer = new CountDownTimer(timeinMS, TIMER_GRANULARITY) {

            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(Long.toString(millisUntilFinished / 1000 + 1));
                mTimeRemaining = millisUntilFinished;
            }

            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
                i.putExtra("score", mScore);
                i.putExtra("skipped", mSkipped);
                startActivity(i);
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
