package chrisjluc.onesearch.ui.gameplay;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chrisjluc.onesearch.R;

public class WordSearchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private WordSearchGridView mGrid;

    public static WordSearchFragment newInstance(int sectionNumber) {
        WordSearchFragment fragment = new WordSearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WordSearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wordsearch_fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);
        mGrid = (WordSearchGridView) rootView.findViewById(R.id.gridView);
        mGrid.setWordFoundListener((WordSearchGridView.WordFoundListener) getActivity());
        tv.setText(mGrid.getWord());
        return rootView;
    }

    public void highlightWord() {
        mGrid.highlightWord();
    }
}
