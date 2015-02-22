package chrisjluc.onesearch.wordSearchGenerator.generators;

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
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }

    public static int countMatches(String s, char test){
        int count = 0;
        for (char c: s.toCharArray())
            if (c == test)
                count ++;
        return count;
    }
}
