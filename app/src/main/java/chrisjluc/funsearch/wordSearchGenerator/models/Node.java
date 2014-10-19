package chrisjluc.funsearch.wordSearchGenerator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chrisjluc on 2014-10-16.
 */
public class Node {
    private char letter = '0';
    private boolean highlighted = false;
    private List<PossibleInstance> possibleInstances = null;

    public Node(){}

    public void addToPossibleInstances(PossibleInstance pi){
        if(possibleInstances == null)
            possibleInstances = new ArrayList<PossibleInstance>();
        possibleInstances.add(pi);
    }

    public List<PossibleInstance> getPossibleInstances(){
        return possibleInstances;
    }

    public void clearPossibleInstances(){
        possibleInstances = null;
    }

    public boolean isEmpty(){
        if(letter == '0')
            return true;
        return false;
    }

    public boolean isHighlighted(){
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public char getLetter(){
        return letter;
    }

    public void setLetter(char letter){
        this.letter = letter;
    }

}
