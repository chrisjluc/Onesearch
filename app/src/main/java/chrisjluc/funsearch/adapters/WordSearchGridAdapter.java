package chrisjluc.funsearch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.wordSearchGenerator.models.Node;

public class WordSearchGridAdapter extends BaseAdapter {

    private Context mContext;
    private Node[] mNodes;
    private int mColumnWidth;
    private int mWordSearchDimension;
    private LayoutInflater mInflater;

    public WordSearchGridAdapter(Context context, Node[] nodes, int columnWidth, int wordSearchDimension) {
        this.mContext = context;
        this.mNodes = nodes;
        this.mColumnWidth = columnWidth;
        this.mWordSearchDimension = wordSearchDimension;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int pos, View convertView, ViewGroup parent) {

        TextView tv;
        Node n = mNodes[pos];

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item, null);
            tv = (TextView) convertView;
            tv.setText("" + n.getLetter());
            tv.setHeight(mColumnWidth);
            int difference = ((WordSearchManager.ADVANCED_MAX_WORDLENGTH + WordSearchManager.ADVANCED_MAX_DIMENSION_OFFSET - WordSearchManager.EASY_MIN_WORDLENGTH) / 3);
            if (WordSearchManager.EASY_MIN_WORDLENGTH <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference))
                tv.setTextSize(22);
            else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 2))
                tv.setTextSize(18);
            else
                tv.setTextSize(14);
        } else {
            tv = (TextView) convertView;
        }

        int color;
        if (n.isHighlighted())
            color = Color.BLUE;
        else if ((pos / mWordSearchDimension) % 2 == 0)
            color = mContext.getResources().getColor(pos % 2 == 0 ? R.color.blue : R.color.green);
        else
            color = mContext.getResources().getColor((pos - mWordSearchDimension) % 2 == 0 ? R.color.green : R.color.blue);
        tv.setTextColor(color);

        return convertView;
    }

    @Override
    public int getCount() {
        return mNodes.length;
    }

    @Override
    public Node getItem(int pos) {
        return mNodes[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }
}
