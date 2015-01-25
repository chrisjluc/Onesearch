package chrisjluc.funsearch.wordSearchGenerator.generators;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static Character[] getDistinctCharacters(String s) {
        List<Character> uniqueChars = new ArrayList<Character>();
        for (int i = 0; i < s.length(); i++)
            if (!uniqueChars.contains(s.charAt(i)))
                uniqueChars.add(s.charAt(i));
        return uniqueChars.toArray(new Character[uniqueChars.size()]);
    }

    public static String reverse(String s) {
        String ret = "";
        for (int i = 0; i < s.length(); i++)
            ret = s.charAt(i) + ret;
        return ret;
    }
}
