package chrisjluc.funsearch.wordSearchGenerator.generators;
import chrisjluc.funsearch.wordSearchGenerator.models.Node;
import chrisjluc.funsearch.wordSearchGenerator.models.Point;

import java.lang.String;import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chrisjluc on 2014-10-16.
 */
public class WordSearchGenerator {
    public List<Character> uniqueChars;
    int sizeOfUniqeChars;
    private String word;
    private int nRow;
    private int nCol;
    private Node[][] wordSearch;
    private Random r = new Random();

    public Node[][] getWordSearch(){
        return wordSearch;
    }

    public int buildFailures = 0;

    // Orientations
    private int nOrientations = 8;
    private static final int RIGHT = 0, RIGHTDOWN = 1, DOWN = 2, LEFTDOWN = 3, LEFT = 4, LEFTUP = 5, UP = 6, RIGHTUP = 7;

    public WordSearchGenerator(int nRow, int nCol, String word){
        this.nRow = nRow;
        this.nCol = nCol;
        this.word = word;

        this.wordSearch = new Node[nRow][nCol];
        initializeWordSearch();
    }

    public void build(){

        while(true) {
            try {
                uniqueChars = getDistinctCharacters(word);
                sizeOfUniqeChars = uniqueChars.size();
                fillWordSearch();
                break;
            } catch (Exception e) {
                initializeWordSearch();
                buildFailures++;
            }
        }
    }

    public List<Node> generateNodeList(){
        List<Node> nodes = new ArrayList<Node>();
        for(int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                nodes.add(wordSearch[i][j]);
        return nodes;
    }

    private void initializeWordSearch() {
        for(int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                wordSearch[i][j] = new Node();
        try {
            insertWord();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillWordSearch() throws Exception{
        for(int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {

                Node n = wordSearch[i][j];
                if(!n.isEmpty()) continue;
                DistinctRandomGenerator charGenerator = new DistinctRandomGenerator(uniqueChars);
                Character c = (Character)charGenerator.next();
                while(c != null){
                    if(isAllOrientationsValid(c, new Point(i, j))) break;
                    c = (Character)charGenerator.next();
                    if(c == null) throw new Exception("Random Char generator returned null");
                }
                n.setLetter(c);
            }
        }
    }

    private boolean isAllOrientationsValid(char c, Point p){
        if(c != word.charAt(0) && c != word.charAt(word.length()-1)) return true;
        for(int i = 0; i < nOrientations; i++){
            String s = getStringByOrientation(i, p);
            if(s == null) continue;
            if(s.contains("0")) continue;
            s = "" + c + s;
            if(word.equals(s) || word.equals(reverse(s))) return false;
        }
        return true;
    }

    public void print(){
        for(int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                System.out.print(wordSearch[i][j].getLetter());
            }
            System.out.println();
        }
    }

    private void insertWord() throws Exception{

        // Choose random coordinate and orientation
        DistinctRandomGenerator rOrientation = new DistinctRandomGenerator(8);
        Point randomPoint = new Point(r.nextInt(nRow), r.nextInt(nCol));

        Integer randomOrientation = (Integer) rOrientation.next();
        while(!isValidOrientationForInsertion(randomOrientation, randomPoint)) {
            randomOrientation = (Integer) rOrientation.next();
            if(randomOrientation == null)
                throw new Exception("Random orientation generator returned null");
        }

        insertWordWithOrientation(randomOrientation, randomPoint);

    }

    private void insertWordWithOrientation(int orientation, Point p){
        switch(orientation) {
            case RIGHT:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x + i][p.y].setLetter(word.charAt(i));

            break;

            case RIGHTDOWN:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x + i][p.y + i].setLetter(word.charAt(i));

                break;

            case DOWN:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x][p.y + i].setLetter(word.charAt(i));

                break;

            case LEFTDOWN:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x - i][p.y + i].setLetter(word.charAt(i));

                break;

            case LEFT:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x - i][p.y].setLetter(word.charAt(i));

                break;

            case LEFTUP:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x - i][p.y - i].setLetter(word.charAt(i));

                break;

            case UP:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x][p.y - i].setLetter(word.charAt(i));

                break;

            case RIGHTUP:

                for (int i = 0; i < word.length(); i++)
                    wordSearch[p.x + i][p.y - i].setLetter(word.charAt(i));

                break;

            default:
            break;
        }
    }

    private String getStringByOrientation(int orientation, Point p){
        switch(orientation){
            case RIGHT:
                return lookRight(p, word.length()-1);

            case RIGHTDOWN:
                return lookRightDown(p, word.length() - 1);

            case DOWN:
                return lookDown(p, word.length()-1);

            case LEFTDOWN:
                return lookLeftDown(p, word.length() - 1);

            case LEFT:
                return lookLeft(p, word.length()-1);

            case LEFTUP:
                return lookLeftUp(p, word.length() - 1);

            case UP:
                return lookUp(p, word.length() - 1);

            case RIGHTUP:
                return lookRightUp(p, word.length() - 1);

            default:
                return null;
        }
    }

    private boolean isValidOrientationForInsertion(int orientation, Point p){
        switch(orientation){
            case RIGHT:
                return lookRight(p, word.length()-1) != null ? true: false;

            case RIGHTDOWN:
                return lookRightDown(p, word.length() - 1) != null ? true: false;

            case DOWN:
                return lookDown(p, word.length()-1) != null ? true: false;

            case LEFTDOWN:
                return lookLeftDown(p, word.length() - 1) != null ? true: false;

            case LEFT:
                return lookLeft(p, word.length()-1) != null ? true: false;

            case LEFTUP:
                return lookLeftUp(p, word.length() - 1) != null ? true: false;

            case UP:
                return lookUp(p, word.length() - 1) != null ? true: false;

            case RIGHTUP:
                return lookRightUp(p, word.length() - 1) != null ? true: false;

            default:
                return false;
        }
    }


    private String lookRight(Point p, int d){
        if(p.x + d >= nCol)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x + i][p.y].getLetter();
        }
        return ret;
    }

    private String lookRightDown(Point p, int d) {
        if(p.x + d >= nCol || p.y + d >= nCol)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x + i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookDown(Point p, int d){
        if(p.y + d >= nCol)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeftDown(Point p, int d) {
        if(p.x - d < 0 || p.y + d >= nCol)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x - i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeft(Point p, int d){
        if(p.x - d < 0)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x - i][p.y].getLetter();
        }
        return ret;
    }

    private String lookLeftUp(Point p, int d) {
        if(p.x - d < 0 || p.y - d < 0)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x - i][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookUp(Point p, int d){
        if(p.y - d < 0)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookRightUp(Point p, int d) {
        if(p.x + d >= nCol || p.y - d < 0)
            return null;
        String ret = "";
        for(int i = 1; i <= d; i++){
            ret += wordSearch[p.x + i][p.y - i].getLetter();
        }
        return ret;
    }

    public static List<Character> getDistinctCharacters(String s){
        List<Character> uniqueChars = new ArrayList<Character>();
        for(int i = 0; i < s.length(); i++)
            if(!uniqueChars.contains(s.charAt(i)))
                uniqueChars.add(s.charAt(i));
        return uniqueChars;
    }

    public static String reverse(String s){
        String ret = "";
        for(int i = 0; i < s.length(); i++)
            ret = s.charAt(i) + ret;
        return ret;
    }
}
