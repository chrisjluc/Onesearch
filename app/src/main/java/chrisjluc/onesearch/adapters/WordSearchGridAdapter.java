package chrisjluc.onesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.utils.DeviceUtils;
import chrisjluc.onesearch.wordSearchGenerator.models.Node;

public class WordSearchGridAdapter extends BaseAdapter {

    public static final String DISPLAY_TYPE = DisplayType.STANDARD_BLUE;
    public static final String HIGHLIGHT_TYPE = HighlightType.BORDER_GREEN_CIRCLE;
    private Context mContext;
    private Node[] mNodes;
    private int mColumnWidth;
    private int mWordSearchDimension;
    private LayoutInflater mInflater;
    private boolean mIsTablet;
    private boolean mIsSmallScreen;

    public WordSearchGridAdapter(Context context, Node[] nodes, int columnWidth, int wordSearchDimension) {
        this.mContext = context;
        this.mNodes = nodes;
        this.mColumnWidth = columnWidth;
        this.mWordSearchDimension = wordSearchDimension;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mIsTablet = DeviceUtils.isTablet(context);
        this.mIsSmallScreen = DeviceUtils.isSmallScreen(context);
    }

    public View getView(final int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Node n = mNodes[pos];

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder.textView = (TextView) convertView;
            convertView.setTag(holder);

            holder.textView.setText("" + n.getLetter());
            holder.textView.setHeight(mColumnWidth);
            int size;
            int difference = ((WordSearchManager.ADVANCED_MAX_WORDLENGTH + WordSearchManager.ADVANCED_MAX_DIMENSION_OFFSET - WordSearchManager.EASY_MIN_WORDLENGTH) / 6);
            if (WordSearchManager.EASY_MIN_WORDLENGTH <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference))
                size = 36;
            else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 2))
                size = 34;
            else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference * 2) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 3))
                size = 30;
            else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference * 3) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 4))
                size = 28;
            else if ((WordSearchManager.EASY_MIN_WORDLENGTH + difference * 4) <= mWordSearchDimension && mWordSearchDimension < (WordSearchManager.EASY_MIN_WORDLENGTH + difference * 5))
                size = 24;
            else if (!mIsSmallScreen)
                size = 21;
            else
                size = 20;

            if (mIsTablet)
                size *= 1.5;
            else if (mIsSmallScreen)
                size /= 1.1;

            holder.textView.setTextSize(size);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int color;
        if (DISPLAY_TYPE.equals(DisplayType.STANDARD_BLUE)) {
            color = mContext.getResources().getColor(R.color.blue);
        } else {
            if ((pos / mWordSearchDimension) % 2 == 0)
                color = mContext.getResources().getColor(pos % 2 == 0 ? R.color.blue : R.color.green);
            else
                color = mContext.getResources().getColor((pos - mWordSearchDimension) % 2 == 0 ? R.color.green : R.color.blue);
        }
        holder.textView.setTextColor(color);

        if (n.isHighlighted()) {
            if (HIGHLIGHT_TYPE.equals(HighlightType.FULL_PURPLE_CIRCLE))
                holder.textView.setBackgroundResource(R.drawable.grid_item_highlight_purple);
            else {
                holder.textView.setBackgroundResource(R.drawable.grid_item_highlight_green);
                holder.textView.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        } else {
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

    public static final class DisplayType {
        public final static String STANDARD_BLUE = "SB", ALTERNATING_BLUE_GREEN = "ABG";
    }

    public static final class HighlightType {
        public final static String FULL_PURPLE_CIRCLE = "fpc", BORDER_GREEN_CIRCLE = "bgc";

    }

    static class ViewHolder {
        TextView textView;
    }
}
