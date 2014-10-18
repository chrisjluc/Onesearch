package chrisjluc.funsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by chrisjluc on 2014-10-17.
 */
public class WordSearchGridAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    private int dimension;
    LayoutInflater inflater;

    public WordSearchGridAdapter(Context context, String[] items, int dimension) {
        this.context = context;
        this.items = items;
        this.dimension = dimension;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int pos, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        TextView tv = (TextView) convertView;
        tv.setText(items[pos]);
        tv.setHeight(dimension);
        return convertView;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
