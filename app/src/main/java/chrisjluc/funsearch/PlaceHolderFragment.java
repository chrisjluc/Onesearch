package chrisjluc.funsearch;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class PlaceHolderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Activity activity;
    private WordSearchGridView grid;
    private View rootView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceHolderFragment newInstance(int sectionNumber) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceHolderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);
        WordSearchGenerator generator = WordSearchManager.getInstance().getGenerator(MainActivity.currentItem);
        tv.setText(generator.word);
        activity = getActivity();
        grid = (WordSearchGridView) rootView.findViewById(R.id.gridView);
        grid.setWordFoundListener((WordFoundListener)getActivity());
        return rootView;
    }
}
