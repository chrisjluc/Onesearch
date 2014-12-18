package chrisjluc.funsearch.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.interfaces.WordFoundListener;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private WordSearchGridView grid;
    private View rootView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);
        WordSearchGenerator generator = WordSearchManager.getInstance().getGenerator(MainActivity.currentItem);
        tv.setText(generator.word);
        grid = (WordSearchGridView) rootView.findViewById(R.id.gridView);
        grid.setWordFoundListener((WordFoundListener)getActivity());
        return rootView;
    }
}
