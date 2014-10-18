package chrisjluc.funsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;
import chrisjluc.funsearch.wordSearchGenerator.models.Node;
import chrisjluc.funsearch.wordSearchGenerator.models.Point;

/**
 * Created by chrisjluc on 2014-10-17.
 */
public class WordSearchGridView extends GridView {

    private int xLength;
    private int yLength;
    private String word;
    private Point p1, p2;
    private int dimension;
    private int paddingLeft = 16;
    private List<Node> nodes;
    private List<Node> highlightedNodes;
    // the location of the word
    private List<Point> wordPoints;
    public boolean isFound = false;
    private WordFoundListener listener;

    WordSearchGridAdapter adapter;

    public WordSearchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WordSearchManager manager = WordSearchManager.getInstance();
        WordSearchGenerator generator = manager.getGenerator(MainActivity.currentItem);
        xLength = generator.nCol;
        yLength = generator.nRow;
        word = generator.word;
        wordPoints = generator.getStartAndEndPointOfWord();
        nodes = generator.generateNodeList();
        highlightedNodes = new ArrayList<Node>();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dimension = width / (nCol + 1);
        setColumnWidth(dimension);
        adapter = new WordSearchGridAdapter(context, nodes, dimension);
        setAdapter(adapter);
    }

    public void setWordFoundListener(WordFoundListener listener){
        this.listener = listener;
    }


    int x1, y1;
    int x2, y2;
    boolean drawing=false;

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint p= new Paint();
        p.setColor(Color.BLACK);

        if (drawing)
            canvas.drawLine(x1, y1, x2, y2, p);
    }

    private void isWordFound(){
        if(wordPoints.contains(p1) && wordPoints.contains(p2)) {
            isFound = true;
            listener.notifyWordFound();
        }
    }

    private void updateCurrentHighlightedNodes(Point p){
        if(p.y < 0 || p.y >= yLength || p.x < 0 || p.x >= xLength) return;

        if(p.equals(p2)) return;

        p2 = p;

        //diagonal
        boolean isValid = false;
        if(Math.abs(p2.x - p1.x) == Math.abs(p2.y - p1.y)){
            isValid = true;
        // horizontal
        }else if(p2.x - p1.x == 0){
            isValid = true;
        // vertical
        }else if(p2.y - p1.y == 0){
            isValid = true;
        }

        if(!isValid) return;

        clearHighlightedNodes();
        int dX = p2.x - p1.x ;
        int dY = p2.y - p1.y;

        int length = 0;

        if(dX != 0)
            length = Math.abs(dX);

        if(dY != 0)
            length = Math.abs(dY);

        for(int i = 0; i < length+1; i++){
            Point point = new Point(p1.x, p1.y);
            if(dX != 0)
                point.x += dX > 0 ? i : -i;
            if(dY != 0)
                point.y += dY > 0 ? i : -i;
            try {
                highlightNodeAt(point);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void highlightNodeAt(Point p) throws Exception{
        int index = p.y * xLength + p.x;
        if(index < 0 || index >= nodes.size()){
            throw new Exception("Invalid Row: " + p.y + " and col: " + p.x);
        }
        Node n = nodes.get(index);
        if(!highlightedNodes.contains(n)){
            highlightedNodes.add(n);
            n.setHighlighted(true);
        }
    }

    private void clearHighlightedNodes(){
        for(Node n: highlightedNodes)
            n.setHighlighted(false);
        highlightedNodes.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result=false;
        if(isFound) return false;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1=x2= (int)event.getX();
                y1=y2= (int)event.getY();
                p1 = new Point(x1 / dimension, y1 / dimension);
                drawing=true;
                result=true;
                break;
            case MotionEvent.ACTION_MOVE:
                x2= (int)event.getX();
                y2= (int)event.getY();
                result=true;
                updateCurrentHighlightedNodes(new Point(x2 / dimension, y2 / dimension));
                break;
            case MotionEvent.ACTION_UP:
                x2= (int)event.getX();
                y2= (int)event.getY();
                p2 = new Point(x2 / dimension, y2 / dimension);
                isWordFound();
                if(!isFound)
                    clearHighlightedNodes();
                drawing=false;
                result=true;
                break;
        }

        if (result) invalidate();
        return result;
    }
}
