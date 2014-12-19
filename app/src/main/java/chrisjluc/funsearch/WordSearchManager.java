package chrisjluc.funsearch;

import java.util.Random;

import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class WordSearchManager {

    private final static int MAX_DIMENSION = 8;
    private final static String[] WORDS = {"alfred", "hello", "hey"};
    private final static int SIZE = 3;

    private Random mRandom;
    private static WordSearchManager mInstance = new WordSearchManager();

    public static WordSearchManager getInstance() {
        return mInstance;
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
            if(word.length() <= MAX_DIMENSION)
                break;
            words[index] = words[words.length - 1 - i];
        }
        int maxPossibleOffset = MAX_DIMENSION - word.length();
        int randomOffset = mRandom.nextInt(maxPossibleOffset + 1);
        int dimen = word.length() + randomOffset;
        WordSearchGenerator gen = new WordSearchGenerator(dimen, dimen, word);
        gen.build();
        return gen;
    }
}
