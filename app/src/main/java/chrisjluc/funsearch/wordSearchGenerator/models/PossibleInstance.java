package chrisjluc.funsearch.wordSearchGenerator.models;

/**
 * Holds the data required to determine if the current node can hold the candidate character
 *
 * Created by chrisjluc on 2014-10-19.
 */
public class PossibleInstance {

    /**
     * Position of character in word
     *
     * ex. Word is abcd, position is 1, so we know the letter of interest is b
     */
    public int positionInWord;

    /**
     * orientation of possible instance
     */
    public int orientation;

    /**
     * if the possible instance of interest is reverse of the original word
     */
    public boolean reversed;

    public PossibleInstance(int orientation, boolean reversed, int positionInWord) {
        this.orientation = orientation;
        this.reversed = reversed;
        this.positionInWord = positionInWord;
    }
}
