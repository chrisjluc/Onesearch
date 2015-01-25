package chrisjluc.funsearch.adapters;

import android.content.Context;
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

        ViewHolder holder;
        Node n = mNodes[pos];

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder.textView = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText("" + n.getLetter());
        holder.textView.setHeight(mColumnWidth);
        int difference = ((WordSearchManager.ADVANCED_MAX_WORDLENGTH + WordSearchManager.ADVANCED_MAX_DIMENSION_OFFSET - WordSearchManager.EASY_MIN_WORDLENGTH) / 3);
        if (WordSearchManager.EASY_MIN_WORDLENGTH <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference))
            holder.textView.setTextSize(28);
        else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 2))
            holder.textView.setTextSize(24);
        else
            holder.textView.setTextSize(22);

        int color;

        if ((pos / mWordSearchDimension) % 2 == 0)
            color = mContext.getResources().getColor(pos % 2 == 0 ? R.color.blue : R.color.green);
        else
            color = mContext.getResources().getColor((pos - mWordSearchDimension) % 2 == 0 ? R.color.green : R.color.blue);
        holder.textView.setTextColor(color);

        if (n.isHighlighted()) {
            holder.textView.setBackgroundResource(R.drawable.grid_item_highlight);
        }else{
            holder.textView.setBackgroundResource(0);
        }

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

    static class ViewHolder {
        TextView textView;
    }
}
