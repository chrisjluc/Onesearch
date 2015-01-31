package chrisjluc.onesearch.wordSearchGenerator.models;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Point))
            return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }
}
