package chrisjluc.funsearch.ui.gameplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.adapters.WordSearchPagerAdapter;
import chrisjluc.funsearch.base.BaseActivity;
import chrisjluc.funsearch.ui.ResultsActivity;

public class WordSearchActivity extends BaseActivity implements WordSearchGridView.WordFoundListener, PauseDialogFragment.PauseDialogListener, View.OnClickListener {

    private final static int TIMER_GRANULARITY_IN_MS = 50;
    public static int currentItem;
    private final PauseDialogFragment mPauseDialogFragment = new PauseDialogFragment();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView mTimerTextView;
    private TextView mScoreTextView;
    private CountDownTimer mCountDownTimer;
    private String mGameState;
    private long mTimeRemaining;
    private long mStartTime;
    private int mScore;
    private int mSkipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch_activity);
        mStartTime = WordSearchManager.getInstance().getGameMode().getTime();
        mGameState = GameState.START;
        Button skipButton = (Button) findViewById(R.id.bSkip);
        Button pauseButton = (Button) findViewById(R.id.bPause);
        skipButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
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
        WordSearchPagerAdapter mWordSearchPagerAdapter = new WordSearchPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (WordSearchViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mWordSearchPagerAdapter);
        currentItem = 0;
        mScore = 0;
        mSkipped = 0;
        mTimeRemaining = mStartTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
    }

    private void pauseGameplay() {
        if (mGameState.equals(GameState.PAUSE))
            return;
        mGameState = GameState.PAUSE;
        stopCountDownTimer();
        mPauseDialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSkip:
                mViewPager.setCurrentItem(currentItem);
                mSkipped++;
                break;
            case R.id.bPause:
                pauseGameplay();
                break;
        }
    }

    @Override
    public void notifyWordFound() {
        mViewPager.setCurrentItem(currentItem);
        mScoreTextView.setText(Integer.toString(++mScore));
    }

    @Override
    public void onDialogQuit() {
        finish();
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
        restart();
    }

    private void restart() {
        mScore = 0;
        mSkipped = 0;
        mTimeRemaining = mStartTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
        setFullscreen();
        mScoreTextView.setText("0");
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onResume() {
        if (mGameState.equals(GameState.START) || mGameState.equals(GameState.FINISHED))
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            System.out.println("Result not okay");
            return;
        }
        switch (requestCode) {
            case 0:
                int action = data.getIntExtra(ResultsActivity.ACTION_IDENTIFIER, -1);
                if (action == ResultsActivity.RESULT_REPLAY_GAME)
                    restart();
                else if (action == ResultsActivity.RESULT_EXIT_TO_MENU)
                    finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        pauseGameplay();
    }

    private void setupCountDownTimer(final long timeinMS) {
        mCountDownTimer = new CountDownTimer(timeinMS, TIMER_GRANULARITY_IN_MS) {

            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(Long.toString(millisUntilFinished / 1000 + 1));
                mTimeRemaining = millisUntilFinished;
            }

            public void onFinish() {
                mGameState = GameState.FINISHED;
                Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
                i.putExtra("score", mScore);
                i.putExtra("skipped", mSkipped);
                startActivityForResult(i, 0);
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

    public long getTimeRemaining() {
        return mTimeRemaining;
    }

    public int getScore() {
        return mScore;
    }

    private static class GameState {
        public static final String START = "s", PLAY = "pl", PAUSE = "pa", FINISHED = "fi";
    }
}
