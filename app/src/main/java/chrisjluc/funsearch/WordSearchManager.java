package chrisjluc.funsearch;

import android.os.AsyncTask;

import java.util.Random;

import chrisjluc.funsearch.models.GameDifficulty;
import chrisjluc.funsearch.models.GameMode;
import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class WordSearchManager {

    public final static int EASY_MIN_WORDLENGTH = 3;
    public final static int EASY_MAX_WORDLENGTH = 3;
    public final static int EASY_MIN_DIMENSION_OFFSET = 0;
    public final static int EASY_MAX_DIMENSION_OFFSET = 0;

    public final static int MEDIUM_MIN_WORDLENGTH = 3;
    public final static int MEDIUM_MAX_WORDLENGTH = 4;
    public final static int MEDIUM_MIN_DIMENSION_OFFSET = 1;
    public final static int MEDIUM_MAX_DIMENSION_OFFSET = 1;

    public final static int HARD_MIN_WORDLENGTH = 5;
    public final static int HARD_MAX_WORDLENGTH = 6;
    public final static int HARD_MIN_DIMENSION_OFFSET = 2;
    public final static int HARD_MAX_DIMENSION_OFFSET = 3;

    public final static int ADVANCED_MIN_WORDLENGTH = 7;
    public final static int ADVANCED_MAX_WORDLENGTH = 9;
    public final static int ADVANCED_MIN_DIMENSION_OFFSET = 3;
    public final static int ADVANCED_MAX_DIMENSION_OFFSET = 5;

    private final static String[] WORDS = {"alfred", "hello", "hey", "heat", "time", "steam", "elephant", "scissor", "point", "star", "tree", "bob", "airplane", "tail", "mouth", "chin", "phone", "jar", "ear", "drum", "room"};
    private final static int SIZE = 6;
    private static WordSearchManager mInstance;

    private int mMinWordLength;
    private int mMaxWordLength;
    /**
     * Dimension offset is the size of the word search relative to the chosen word length
     */
    private int mMinDimensionOffset;
    private int mMaxDimensionOffset;
    private Random mRandom;
    private GameMode mGameMode;

    public static WordSearchManager getInstance() {
        if (mInstance == null)
            mInstance = new WordSearchManager();
        return mInstance;
    }

    public static void nullify() {
        mInstance = null;
    }

    private WordSearchGenerator[] mWordSearchArray;

    private WordSearchManager() {
        mRandom = new Random();
        mWordSearchArray = new WordSearchGenerator[SIZE];
    }

    public void buildWordSearches() {
        new BuildWordSearchArrayTask().execute();
    }

    public WordSearchGenerator getWordSearch(final int i) {
        if (i < 0)
            return buildWordSearch();
        WordSearchGenerator ret = mWordSearchArray[i % SIZE];
        new BuildWordSearchTask(i % SIZE).execute();
        return ret;
    }

    private WordSearchGenerator buildWordSearch() {
        String[] words = new String[WORDS.length];
        System.arraycopy(WORDS, 0, words, 0, WORDS.length);
        String word = "";
        for (int i = 0; i < words.length; i++) {
            int index = mRandom.nextInt(words.length - i);
            word = words[index];
            if (word.length() >= mMinWordLength && word.length() <= mMaxWordLength)
                break;
            words[index] = words[words.length - 1 - i];
        }

        int dimen = word.length() + mMinDimensionOffset;
        int offsetDifference = mMaxDimensionOffset - mMinDimensionOffset;
        if (offsetDifference > 0)
            dimen += mRandom.nextInt(offsetDifference + 1);

        WordSearchGenerator gen = new WordSearchGenerator(dimen, dimen, word);
        gen.build();
        return gen;
    }

    public GameMode getGameMode() {
        return mGameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.mGameMode = gameMode;
        if (mGameMode.getDifficulty() == GameDifficulty.Easy) {
            mMinWordLength = EASY_MIN_WORDLENGTH;
            mMaxWordLength = EASY_MAX_WORDLENGTH;
            mMinDimensionOffset = EASY_MIN_DIMENSION_OFFSET;
            mMaxDimensionOffset = EASY_MAX_DIMENSION_OFFSET;
        } else if (mGameMode.getDifficulty() == GameDifficulty.Medium) {
            mMinWordLength = MEDIUM_MIN_WORDLENGTH;
            mMaxWordLength = MEDIUM_MAX_WORDLENGTH;
            mMinDimensionOffset = MEDIUM_MIN_DIMENSION_OFFSET;
            mMaxDimensionOffset = MEDIUM_MAX_DIMENSION_OFFSET;
        } else if (mGameMode.getDifficulty() == GameDifficulty.Hard) {
            mMinWordLength = HARD_MIN_WORDLENGTH;
            mMaxWordLength = HARD_MAX_WORDLENGTH;
            mMinDimensionOffset = HARD_MIN_DIMENSION_OFFSET;
            mMaxDimensionOffset = HARD_MAX_DIMENSION_OFFSET;
        } else if (mGameMode.getDifficulty() == GameDifficulty.Advanced) {
            mMinWordLength = ADVANCED_MIN_WORDLENGTH;
            mMaxWordLength = ADVANCED_MAX_WORDLENGTH;
            mMinDimensionOffset = ADVANCED_MIN_DIMENSION_OFFSET;
            mMaxDimensionOffset = ADVANCED_MAX_DIMENSION_OFFSET;
        }
    }

    private class BuildWordSearchArrayTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < SIZE; i++) {
                mWordSearchArray[i] = buildWordSearch();
            }
            return null;
        }
    }

    private class BuildWordSearchTask extends AsyncTask<Void, Void, Void> {

        private int i;

        public BuildWordSearchTask(int i) {
            this.i = i;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mWordSearchArray[i] = buildWordSearch();
            return null;
        }
    }
}
