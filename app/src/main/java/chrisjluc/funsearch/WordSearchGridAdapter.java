package chrisjluc.funsearch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chrisjluc.funsearch.wordSearchGenerator.models.Node;

/**
 * Created by chrisjluc on 2014-10-17.
 */
public class WordSearchGridAdapter extends BaseAdapter {

    private Context context;
    private List<Node> nodes;
    private int dimension;
    LayoutInflater inflater;

    public WordSearchGridAdapter(Context context, List<Node> nodes, int dimension) {
        this.context = context;
        this.nodes = nodes;
        this.dimension = dimension;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int pos, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }
        Node n = nodes.get(pos);
        TextView tv = (TextView) convertView;
        tv.setText("" + n.getLetter());
        tv.setHeight(dimension);
        if(n.isHighlighted())
            tv.setTextColor(Color.BLUE);
        else
            tv.setTextColor(Color.BLACK);
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
