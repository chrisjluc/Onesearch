package chrisjluc.funsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by chrisjluc on 2014-10-17.
 */
public class WordSearchGridView extends GridView {

    private int nRow = 4;
    private int nCol = 10;
    private int dimension;
    private int colHeight;

    private static final String[] letters = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};


    public WordSearchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dimension = width / (nCol + 1);
        setColumnWidth(dimension);
        WordSearchGridAdapter adapter = new WordSearchGridAdapter(context, letters, dimension);
        setAdapter(adapter);
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result=false;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1=x2= (int)event.getX();
                y1=y2= (int)event.getY();
                drawing=true;
                result=true;
                break;
            case MotionEvent.ACTION_MOVE:
                x2= (int)event.getX();
                y2= (int)event.getY();
                System.out.println("Col:" + x2 / dimension);
                System.out.println("Row:" + y2 / dimension);

                result=true;
                break;
            case MotionEvent.ACTION_UP:
                x2= (int)event.getX();
                y2= (int)event.getY();
                drawing=false;
                result=true;
                break;
        }

        if (result) invalidate();
        return result;
    }
}
