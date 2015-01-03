package chrisjluc.funsearch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.wordSearchGenerator.models.Node;

public class WordSearchGridAdapter extends BaseAdapter {

    private Context context;
    private List<Node> nodes;
    private int dimension;
    private int xLength;
    private LayoutInflater inflater;

    public WordSearchGridAdapter(Context context, List<Node> nodes, int dimension, int xLength) {
        this.context = context;
        this.nodes = nodes;
        this.dimension = dimension;
        this.xLength = xLength;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int pos, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }
        Node n = nodes.get(pos);
        TextView tv = (TextView) convertView;
        tv.setText("" + n.getLetter());
        tv.setHeight(dimension);
        int color;
        if (n.isHighlighted())
            color = Color.BLUE;
        else if ((pos / xLength) % 2 == 0)
            color = context.getResources().getColor(pos % 2 == 0 ? R.color.blue : R.color.green);
        else
            color = context.getResources().getColor((pos - xLength) % 2 == 0 ? R.color.green : R.color.blue);
        tv.setTextColor(color);

        return convertView;
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int pos) {
        return nodes.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }
}
