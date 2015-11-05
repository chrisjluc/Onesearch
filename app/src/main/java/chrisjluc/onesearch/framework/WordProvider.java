package chrisjluc.onesearch.framework;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class WordProvider {

    // Category is a group of words with the same length
    private final static int[] WORDS_IN_EACH_CATEGORY = {0, 0, 0, 419, 1300, 1100, 1000, 1000, 1000, 800, 600, 300};
    private final static int NUM_WORDS = 2;

    private Context mContext;
    private int mMinWordLength;
    private int mWordLengthDiff;
    private String[] words;
    private int wordIndex;
    private Random mRandom;

    public WordProvider(Context mContext, int mMinWordLength, int mMaxWordLength) {
        this.mContext = mContext;
        this.mMinWordLength = mMinWordLength;
        mWordLengthDiff = mMaxWordLength - mMinWordLength;
        mRandom = new Random();
        words = new String[NUM_WORDS];
        wordIndex = 0;
        for (int i = 0; i < NUM_WORDS; i++) {
            int wordLength = getRandomWordLength();
            new AsyncFileReader(i, wordLength, getRandomIndex(wordLength)).execute();
        }
    }

    public String getWord() {
        int wordLength = getRandomWordLength();
        new AsyncFileReader(wordIndex, wordLength, getRandomIndex(wordLength)).execute();
        return words[wordIndex++ % NUM_WORDS];
    }

    private int getRandomWordLength() {
        return mMinWordLength + mRandom.nextInt(mWordLengthDiff + 1);
    }

    private int getRandomIndex(int wordLength) {
        return mRandom.nextInt(WORDS_IN_EACH_CATEGORY[wordLength]);
    }

    private class AsyncFileReader extends AsyncTask<Void, Void, Void> {

        private final static String FILE_PREFIX = "words/words-length-";

        private int wordIndex;
        private int wordLength;
        private int fileIndex;

        private AsyncFileReader(int wordIndex, int wordLength, int fileIndex) {
            this.wordIndex = wordIndex;
            this.wordLength = wordLength;
            this.fileIndex = fileIndex;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open(FILE_PREFIX + wordLength)));
                String sCurrentLine;
                int count = 0;
                while ((sCurrentLine = br.readLine()) != null) {
                    if (count++ >= fileIndex)
                        break;
                }
                br.close();
                if (sCurrentLine != null)
                    words[wordIndex % NUM_WORDS] = sCurrentLine;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
