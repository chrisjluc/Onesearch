package chrisjluc.onesearch.wordSearchGenerator.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chrisjluc.onesearch.wordSearchGenerator.models.FillType;
import chrisjluc.onesearch.wordSearchGenerator.models.Node;
import chrisjluc.onesearch.wordSearchGenerator.models.Point;
import chrisjluc.onesearch.wordSearchGenerator.models.PossibleInstance;

public class WordSearchGenerator {
    private Character[] mFillChars;
    private String mWord;
    private int nRow;
    private int nCol;
    private Node[][] mWordSearchNodeMatrix;
    private Node[] mWordSearchNodes;
    private Random mRandom = new Random();
    private Point mRandomPoint;
    private Integer mRandomOrientation;
    private int mBuildFailures = 0;

    public static final int RIGHT = 0, RIGHTDOWN = 1, DOWN = 2, LEFTDOWN = 3, LEFT = 4, LEFTUP = 5, UP = 6, RIGHTUP = 7;
    private static final Character[] alphabet =
            {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};


    public WordSearchGenerator(int nRow, int nCol, String mWord, String type) {
        this.nRow = nRow;
        this.nCol = nCol;
        this.mWord = mWord;

        this.mWordSearchNodeMatrix = new Node[nRow][nCol];
        this.mWordSearchNodes = new Node[nRow * nCol];
        if (type.equals(FillType.CharactersOfTheWord))
            this.mFillChars = StringUtils.getDistinctCharacters(mWord);
        else
            this.mFillChars = alphabet;
        initializeWordSearch();
    }

    public void build() {
        while (true) {
            try {
                insertWord();
                fillWordSearch();
                break;
            } catch (Exception e) {
                initializeWordSearch();
                mBuildFailures++;
            }
        }
        for (int i = 0; i < nRow; i++) {
            System.arraycopy(mWordSearchNodeMatrix[i], 0, mWordSearchNodes, i * nCol, nCol);
        }
    }

    public List<Point> getStartAndEndPointOfWord() {
        List<Point> points = new ArrayList<Point>(2);
        points.add(mRandomPoint);
        Point endPoint = getRelativePoint(mRandomOrientation, mRandomPoint, mWord.length() - 1);
        points.add(endPoint);
        return points;
    }

    private void initializeWordSearch() {
        for (int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                mWordSearchNodeMatrix[i][j] = new Node();
    }

// -------------- LETTER INSERTION ALGORITHM METHODS --------------------//

    /**
     * Manages general loop logic for letter insertion into the word search
     *
     * @throws Exception
     */
    private void fillWordSearch() throws Exception {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                Node n = mWordSearchNodeMatrix[i][j];
                // Word isn't empty if that letter is part of the inserted word to be found
                if (!n.isEmpty()) continue;
                Point currentPoint = new Point(i, j);
                DistinctRandomGenerator charGenerator = new DistinctRandomGenerator(mFillChars);
                // Generate random character from unique chars
                Character candidate = (Character) charGenerator.next();
                while (candidate != null) {

                    // If the first or last character, continue
                    if (candidate == mWord.charAt(0) || candidate == mWord.charAt(mWord.length() - 1)) {
                        if (areAllOrientationsValid(candidate, currentPoint))
                            break;
                    } else if (arePossibleInstancesSatisfied(candidate, currentPoint)) break;

                    // Characters violate constraints, get the next unique character
                    candidate = (Character) charGenerator.next();

                    // No character satisfies the constraints
                    if (candidate == null) throw new Exception("Random Char generator returned null");
                }
                n.setLetter(candidate);
            }
        }
    }

    private boolean areAllOrientationsValid(char candidate, Point p) {
        int nOrientations = 8;
        for (int o = 0; o < nOrientations; o++) {
            String s = getStringByOrientation(o, p);
            if (s == null) continue;
            s = "" + candidate + s;

            if (mWord.equals(s) || mWord.equals(StringUtils.reverse(s))) return false;

            if (isChanceOfPossibleInstance(s)) {
                // index of first '0', we will set this node to have a possible instance
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '0') {
                        validateAndSetPossibleInstance(p, i, o, false);
                        break;
                    } else if (s.charAt(i) != mWord.charAt(i)) {
                        break;
                    }
                }

                // Compare with the reverse and see if there's a chance
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '0') {
                        validateAndSetPossibleInstance(p, i, o, true);
                        break;
                    } else if (s.charAt(i) != mWord.charAt(s.length() - 1 - i)) {
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Detects if there is a chance we can have another instance of a string
     * Ex. If word is easy
     * and we find a string such as ea0y, e0sy or e00y
     * We want to create a flag which tells the generator
     * there is a chance of creating another instance of that word
     * e000 there is no possible instance needed because will check the 4th position
     * if there is a last character and invalidate it
     *
     * @param s
     * @return if there is a possible instance
     */
    private boolean isChanceOfPossibleInstance(String s) {
        if (s.length() != mWord.length()) return false;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != mWord.charAt(i) && s.charAt(i) != mWord.charAt(mWord.length() - 1 - i) && s.charAt(i) != '0')
                return false;
        int matches0 = StringUtils.countMatches(s, '0');
        // if 1 '0' and it's at beginning or end areAllOrientationsValid will check it
        if (matches0 == 1 && (s.charAt(0) == '0' || s.charAt(mWord.length() - 1) == '0'))
            return false;
        // If all but 1 character is '0' no possible chance because we check all orientation when at first or last character
        // also if there are no '0' there can't be a chance because no open spaces in to insert a character
        return s.length() - matches0 != 1 && matches0 != 0;
    }

    /**
     * Next node which is current point incremented by position by relative orientation
     * holds the possibleInstance
     *
     * @param point       current point
     * @param position    to set in possible instance
     * @param orientation
     * @return
     */
    private void validateAndSetPossibleInstance(Point point, int position, int orientation, boolean isReversed) {
        // Will be covered by other IsAllOrientationsValid
        if (position >= mWord.length() - 1) return;

        Point relativePoint = getRelativePoint(orientation, point, position);
        Node relativeNode = mWordSearchNodeMatrix[relativePoint.x][relativePoint.y];

        // There exists a character already, can't write a character here
        if (!relativeNode.isEmpty()) return;

        PossibleInstance pi = new PossibleInstance(orientation, isReversed, position);
        relativeNode.addToPossibleInstances(pi);
    }

    /**
     * Word is easy, ex. e00y
     * If 'a' is valid for 2nd letter
     * Will assign the possible instance of 2nd letter to 3rd letter to watch out for 's'
     * Handles reversed when creating possible instance
     *
     * @param point of current node of interest
     * @param delta
     * @param pi    possible instance to extend
     */
    private void extendPossibleInstance(Point point, int delta, PossibleInstance pi) {
        Point relativePoint = getRelativePoint(pi.orientation, point, delta + pi.indexInWord);
        Node relativeNode = mWordSearchNodeMatrix[relativePoint.x][relativePoint.y];
        if (!relativeNode.isEmpty()) return;
        PossibleInstance relativePi = new PossibleInstance(pi.orientation, pi.reversed, delta + pi.indexInWord);
        relativeNode.addToPossibleInstances(relativePi);
    }

    /**
     * Checks if possible instances are handled
     *
     * @param candidate    candidate character
     * @param currentPoint
     * @return candidate character is allowed to be set
     */
    private boolean arePossibleInstancesSatisfied(char candidate, Point currentPoint) {

        Node current = mWordSearchNodeMatrix[currentPoint.x][currentPoint.y];
        List<PossibleInstance> possibleInstances = current.getPossibleInstances();
        if (possibleInstances == null) return true;
        int wordLength = mWord.length();
        for (PossibleInstance pi : possibleInstances) {

            // Continue if candidate isn't the character at the position in the Word
            // ex. easy
            // current candidate is s for es0y, can continue if not the letter that causes possible instance
            if ((pi.reversed || mWord.charAt(pi.indexInWord) != candidate) && (!pi.reversed || mWord.charAt(wordLength - 1 - pi.indexInWord) != candidate))
                continue;

            Point nextPoint = getRelativePoint(pi.orientation, currentPoint, 1);
            Node next = mWordSearchNodeMatrix[nextPoint.x][nextPoint.y];

            // Next character is empty and next character isn't the end of the word
            if (next.isEmpty() && pi.indexInWord < wordLength - 2) {

                // ea0y, set possible instance for letter s because not at end of word yet
                extendPossibleInstance(currentPoint, 1, pi);

                // ex. word is easy
                // The next character is the end of the word
                // it would be checked by isAllOrientations Valid
            } else if (next.isEmpty() && pi.indexInWord >= wordLength - 2) {

                // Next letter isn't empty but isn't the next character in the word.
                // The string won't match the word anymore, so current letter and possibleInstance is valid, continue on
            } else if ((!pi.reversed && next.getLetter() != mWord.charAt(pi.indexInWord + 1))
                    || (pi.reversed && next.getLetter() != mWord.charAt(wordLength - 2 - pi.indexInWord))) {

                // Next letter is equal to the next character in the word
            } else if ((!pi.reversed && next.getLetter() == mWord.charAt(pi.indexInWord + 1))
                    || (pi.reversed && next.getLetter() == mWord.charAt(wordLength - 2 - pi.indexInWord))) {

                // The next character is the end of the word
                if (pi.indexInWord >= wordLength - 2)
                    return false;

                Point nextNextPoint = getRelativePoint(pi.orientation, nextPoint, 1);
                Node nextNext = mWordSearchNodeMatrix[nextNextPoint.x][nextNextPoint.y];

                // The next next character will be the end of the word
                // ex. word is hell
                // h0l0, isAllValidOrientations would check this
                if (pi.indexInWord >= wordLength - 3 && nextNext.isEmpty()) {
                    continue;

                    // h0ll, and candidate was 'e'
                    // don't allow 'e' if next next is end of word
                } else if (pi.indexInWord >= wordLength - 3
                        && ((!pi.reversed && nextNext.getLetter() == mWord.charAt(wordLength - 1)) ||
                        (pi.reversed && nextNext.getLetter() == mWord.charAt(0)))) {
                    return false;
                }

                // Ex Word is hello
                // h0l00, at index 1 testing 'e'
                if (nextNext.isEmpty())
                    extendPossibleInstance(nextPoint, 2, pi);
            }
        }
        current.clearPossibleInstances();
        return true;
    }


// -------- INSERTING THE SINGLE INSTANCE OF THE WORD METHODS -------------//

    private void insertWord() throws Exception {

        // Choose random coordinate and orientation
        DistinctRandomGenerator rOrientation = new DistinctRandomGenerator(8);
        mRandomPoint = new Point(mRandom.nextInt(nCol), mRandom.nextInt(nRow));

        mRandomOrientation = (Integer) rOrientation.next();
        while (!isValidOrientationForInsertion(mRandomOrientation, mRandomPoint)) {
            mRandomOrientation = (Integer) rOrientation.next();
            if (mRandomOrientation == null)
                throw new Exception("Random orientation generator returned null");
        }
        insertWordWithOrientation(mRandomOrientation, mRandomPoint);
    }

    private void insertWordWithOrientation(int orientation, Point p) {
        for (int i = 0; i < mWord.length(); i++) {
            Point relative = getRelativePoint(orientation, p, i);
            mWordSearchNodeMatrix[relative.x][relative.y].setLetter(mWord.charAt(i));
        }
    }

// -------- ORIENTATION METHODS -------------//


    private String getStringByOrientation(int orientation, Point p) {
        switch (orientation) {
            case RIGHT:
                return lookRight(p, mWord.length() - 1);

            case RIGHTDOWN:
                return lookRightDown(p, mWord.length() - 1);

            case DOWN:
                return lookDown(p, mWord.length() - 1);

            case LEFTDOWN:
                return lookLeftDown(p, mWord.length() - 1);

            case LEFT:
                return lookLeft(p, mWord.length() - 1);

            case LEFTUP:
                return lookLeftUp(p, mWord.length() - 1);

            case UP:
                return lookUp(p, mWord.length() - 1);

            case RIGHTUP:
                return lookRightUp(p, mWord.length() - 1);

            default:
                return null;
        }
    }

    public static Point getRelativePoint(int orientation, Point p, int d) {
        switch (orientation) {
            case RIGHT:
                return new Point(p.x + d, p.y);

            case RIGHTDOWN:
                return new Point(p.x + d, p.y + d);

            case DOWN:
                return new Point(p.x, p.y + d);

            case LEFTDOWN:
                return new Point(p.x - d, p.y + d);

            case LEFT:
                return new Point(p.x - d, p.y);

            case LEFTUP:
                return new Point(p.x - d, p.y - d);

            case UP:
                return new Point(p.x, p.y - d);

            case RIGHTUP:
                return new Point(p.x + d, p.y - d);

            default:
                return null;
        }
    }

    private boolean isValidOrientationForInsertion(int orientation, Point p) {
        return getStringByOrientation(orientation, p) != null;
    }


    private String lookRight(Point p, int d) {
        if (p.x + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x + i][p.y].getLetter();
        }
        return ret;
    }

    private String lookRightDown(Point p, int d) {
        if (p.x + d >= nCol || p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x + i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookDown(Point p, int d) {
        if (p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeftDown(Point p, int d) {
        if (p.x - d < 0 || p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x - i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeft(Point p, int d) {
        if (p.x - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x - i][p.y].getLetter();
        }
        return ret;
    }

    private String lookLeftUp(Point p, int d) {
        if (p.x - d < 0 || p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x - i][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookUp(Point p, int d) {
        if (p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookRightUp(Point p, int d) {
        if (p.x + d >= nCol || p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearchNodeMatrix[p.x + i][p.y - i].getLetter();
        }
        return ret;
    }

    public String getWord() {
        return mWord;
    }

    public int getnRow() {
        return nRow;
    }

    public int getnCol() {
        return nCol;
    }

    public Node[] getWordSearchNodes() {
        return mWordSearchNodes;
    }
}