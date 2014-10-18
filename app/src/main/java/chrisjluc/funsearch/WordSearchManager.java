package chrisjluc.funsearch;

import java.util.ArrayList;
import java.util.List;

import chrisjluc.funsearch.wordSearchGenerator.generators.WordSearchGenerator;

/**
 * Created by chrisjluc on 2014-10-18.
 */
public class WordSearchManager {

    private int xLength = 3;
    private int yLength = 3;
    private String word = "bam";

    private static WordSearchManager instance = new WordSearchManager();

    public static WordSearchManager getInstance() {
        return instance;
    }

    private List<WordSearchGenerator> generatorList;

    private WordSearchManager() {

        generatorList = new ArrayList<WordSearchGenerator>();
        for(int i = 0; i < 101; i++) {
            WordSearchGenerator gen = new WordSearchGenerator(yLength, xLength, word);
            gen.build();
            generatorList.add(gen);
        }
    }

    public WordSearchGenerator getGenerator(int i){
        if(i < 0 || i > generatorList.size())
            return null;
        if(i > 1 && generatorList.get(i-1) != null )
            generatorList.set(i-1, null);
        return generatorList.get(i);
    }
}
