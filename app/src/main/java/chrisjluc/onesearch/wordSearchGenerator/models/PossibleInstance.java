package chrisjluc.onesearch.wordSearchGenerator.models;

/**
 * Holds the data required to determine if the current node can be set to the candidate character
 */
public class PossibleInstance {

    /**
     * Index of character in word
     * ex. Word is abcd, position is 1, so we know the letter of interest is b
     * Index of word when reversed wordLength - 1 - indexInWord
     */
    public int indexInWord;

    /**
     * orientation of possible instance
     */
    public int orientation;

    /**
     * if the possible instance of interest is reverse of the original word
     */
    public boolean reversed;

    public PossibleInstance(int orientation, boolean reversed, int indexInWord) {
        this.orientation = orientation;
        this.reversed = reversed;
        this.indexInWord = indexInWord;
    }
}
