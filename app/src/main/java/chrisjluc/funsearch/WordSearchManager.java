package chrisjluc.funsearch;

import java.util.Random;

import chrisjluc.funsearch.models.GameDifficulty;
import chrisjluc.funsearch.models.GameMode;
import chrisjluc.funsearch.models.GameType;
import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class WordSearchManager {

    private final static int EASY_MIN_WORDLENGTH = 3;
    private final static int EASY_MAX_WORDLENGTH = 4;
    private final static int EASY_MIN_DIMENSION_OFFSET = 0;
    private final static int EASY_MAX_DIMENSION_OFFSET = 0;

    private final static int MEDIUM_MIN_WORDLENGTH = 4;
    private final static int MEDIUM_MAX_WORDLENGTH = 6;
    private final static int MEDIUM_MIN_DIMENSION_OFFSET = 1;
    private final static int MEDIUM_MAX_DIMENSION_OFFSET = 2;

    private final static int HARD_MIN_WORDLENGTH = 5;
    private final static int HARD_MAX_WORDLENGTH = 8;
    private final static int HARD_MIN_DIMENSION_OFFSET = 3;
    private final static int HARD_MAX_DIMENSION_OFFSET = 5;

    private final static String[] WORDS = {"alfred", "hello", "hey"};
    private final static int SIZE = 4;
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
        mWordSearchArray[0] = buildWordSearch();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < SIZE; i++) {
                    mWordSearchArray[i] = buildWordSearch();
                }
            }
        };
        new Thread(r).start();
    }

    public WordSearchGenerator getGenerator(final int i) {
        if (i < 0)
            return null;
        if (i > 0) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mWordSearchArray[(i - 1) % SIZE] = buildWordSearch();
                }
            };
            new Thread(r).start();
        }
        return mWordSearchArray[i % SIZE];
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

        int dimen = word.length();
        int offsetDifference = mMaxDimensionOffset - mMinDimensionOffset;
        if (offsetDifference > 0)
            dimen += mRandom.nextInt(offsetDifference + 1) + mMinDimensionOffset;

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
        } else {
            mMinWordLength = HARD_MIN_WORDLENGTH;
            mMaxWordLength = HARD_MAX_WORDLENGTH;
            mMinDimensionOffset = HARD_MIN_DIMENSION_OFFSET;
            mMaxDimensionOffset = HARD_MAX_DIMENSION_OFFSET;
        }
    }
}
