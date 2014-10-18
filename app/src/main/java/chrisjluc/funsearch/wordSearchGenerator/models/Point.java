package chrisjluc.funsearch.wordSearchGenerator.models; /**
 * Created by chrisjluc on 2014-10-16.
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o){
        if(o == null) return false;
        Point p = (Point) o;
        if(x == p.x && y == p.y)
            return true;
        return false;
    }
}
