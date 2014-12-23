package chrisjluc.funsearch.ui.gameplay;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chrisjluc.funsearch.R;
import chrisjluc.funsearch.WordSearchManager;
import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

public class WordSearchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
        WordSearchGenerator generator = WordSearchManager.getInstance().getGenerator(WordSearchActivity.currentItem);
        tv.setText(generator.word);
        WordSearchGridView grid = (WordSearchGridView) rootView.findViewById(R.id.gridView);
        grid.setWordFoundListener((WordSearchGridView.WordFoundListener)getActivity());
        return rootView;
    }
}
