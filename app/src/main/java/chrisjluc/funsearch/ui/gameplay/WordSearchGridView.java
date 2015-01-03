package chrisjluc.funsearch.ui.gameplay;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.adapters.WordSearchGridAdapter;
import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;
import chrisjluc.funsearch.wordSearchGenerator.models.Node;
import chrisjluc.funsearch.wordSearchGenerator.models.Point;

public class WordSearchGridView extends GridView {

    private int mXLength, mYLength;
    private int mColumnWidth;
    private int mHorizontalMargin, mVerticalMargin;
    private Point mStartDrag, mEndDrag;
    private List<Node> mWordSearchNodes;
    private List<Node> mWordSearchHighlightedNodes;
    private String mWord;
    private Point mWordStart, mWordEnd;
    public boolean mIsWordFound = false;
    private WordFoundListener mListener;
    private WordSearchGridAdapter mAdapter;

    int x1, y1;
    int x2, y2;

    public WordSearchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WordSearchManager manager = WordSearchManager.getInstance();
        WordSearchGenerator wordSearch = manager.getWordSearch(WordSearchActivity.currentItem++);
        mXLength = wordSearch.nCol;
        mYLength = wordSearch.nRow;
        mWord = wordSearch.word;
        List<Point> points = wordSearch.getStartAndEndPointOfWord();

        // Convert cartesian from matrix coordinates
        mWordStart = new Point(points.get(0).y, points.get(0).x);
        mWordEnd = new Point(points.get(1).y, points.get(1).x);
        mWordSearchNodes = wordSearch.generateNodeList();
        mWordSearchHighlightedNodes = new ArrayList<Node>();
        mHorizontalMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        mVerticalMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

        // Calculate column dimensions
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        mColumnWidth = (int) (width - 2 * mHorizontalMargin / displayMetrics.density) / (mXLength);
        setColumnWidth(mColumnWidth);
        mAdapter = new WordSearchGridAdapter(context, mWordSearchNodes, mColumnWidth);
        setAdapter(mAdapter);
    }

    public void setWordFoundListener(WordFoundListener listener) {
        this.mListener = listener;
    }

    private void isWordFound() {
        if ((mWordStart.equals(mStartDrag) && mWordEnd.equals(mEndDrag))
                || (mWordStart.equals(mEndDrag) && mWordEnd.equals(mStartDrag))) {
            mIsWordFound = true;
            mListener.notifyWordFound();
        }
    }

    private void updateCurrentHighlightedNodes(Point p) {
        if (p.y < 0 || p.y >= mYLength || p.x < 0 || p.x >= mXLength) return;

        if (p.equals(mEndDrag)) return;

        mEndDrag = p;

        boolean isValid = false;
        // diagonal
        if (Math.abs(mEndDrag.x - mStartDrag.x) == Math.abs(mEndDrag.y - mStartDrag.y)) {
            isValid = true;
            // horizontal
        } else if (mEndDrag.x - mStartDrag.x == 0) {
            isValid = true;
            // vertical
        } else if (mEndDrag.y - mStartDrag.y == 0) {
            isValid = true;
        }

        if (!isValid) return;

        clearHighlightedNodes();
        int dX = mEndDrag.x - mStartDrag.x;
        int dY = mEndDrag.y - mStartDrag.y;

        int length = 0;

        if (dX != 0)
            length = Math.abs(dX);

        if (dY != 0)
            length = Math.abs(dY);

        for (int i = 0; i < length + 1; i++) {
            Point point = new Point(mStartDrag.x, mStartDrag.y);
            if (dX != 0)
                point.x += dX > 0 ? i : -i;
            if (dY != 0)
                point.y += dY > 0 ? i : -i;
            try {
                highlightNodeAt(point);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void highlightNodeAt(Point p) throws Exception {
        int index = p.y * mXLength + p.x;
        if (index < 0 || index >= mWordSearchNodes.size()) {
            throw new Exception("Invalid Row: " + p.y + " and col: " + p.x);
        }
        Node n = mWordSearchNodes.get(index);
        if (!mWordSearchHighlightedNodes.contains(n)) {
            mWordSearchHighlightedNodes.add(n);
            n.setHighlighted(true);
        }
    }

    private void clearHighlightedNodes() {
        for (Node n : mWordSearchHighlightedNodes)
            n.setHighlighted(false);
        mWordSearchHighlightedNodes.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        if (mIsWordFound) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x2 = (int) event.getX();
                y1 = y2 = (int) event.getY();
                mStartDrag = new Point(calcRelativeX(x1), calcRelativeY(y1));
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = (int) event.getX();
                y2 = (int) event.getY();
                result = true;
                updateCurrentHighlightedNodes(new Point(calcRelativeX(x2), calcRelativeY(y2)));
                break;
            case MotionEvent.ACTION_UP:
                x2 = (int) event.getX();
                y2 = (int) event.getY();
                isWordFound();
                if (!mIsWordFound)
                    clearHighlightedNodes();
                result = true;
                break;
        }

        if (result) invalidate();
        return result;
    }

    private int calcRelativeX(int d) {
        return (d - mHorizontalMargin) / mColumnWidth;
    }

    private int calcRelativeY(int d) {
        return (d - mVerticalMargin) / mColumnWidth;
    }

    public interface WordFoundListener {
        public void notifyWordFound();
    }

    public String getWord() {
        return mWord;
    }
}
