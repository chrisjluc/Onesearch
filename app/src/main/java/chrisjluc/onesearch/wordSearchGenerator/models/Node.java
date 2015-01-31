package chrisjluc.onesearch.wordSearchGenerator.models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    /**
     * '0' means letter hasn't been set
     */
    private char letter = '0';
    private List<PossibleInstance> possibleInstances = null;
    private boolean isHighlighted;

    public Node() {
    }

    public void addToPossibleInstances(PossibleInstance pi) {
        if (possibleInstances == null)
            possibleInstances = new ArrayList<PossibleInstance>();
        possibleInstances.add(pi);
    }

    public List<PossibleInstance> getPossibleInstances() {
        return possibleInstances;
    }

    public void clearPossibleInstances() {
        possibleInstances = null;
    }

    public boolean isEmpty() {
        return letter == '0';
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }
}
