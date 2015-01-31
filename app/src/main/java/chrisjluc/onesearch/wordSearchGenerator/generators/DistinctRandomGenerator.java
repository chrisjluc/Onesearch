package chrisjluc.onesearch.wordSearchGenerator.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Returns a distinct element from a given array in random order
 * until there is no elements left, it will then return null
 */
public class DistinctRandomGenerator<T> {
    private Object[] objects;
    private int maxIndex;
    private Random r = new Random();

    public DistinctRandomGenerator(Object[] o) {
        objects = Arrays.copyOfRange(o, 0, o.length);
        maxIndex = o.length - 1;
    }

    public DistinctRandomGenerator(List<T> o) {
        objects = Arrays.copyOfRange(o.toArray(), 0, o.size());
        maxIndex = o.size() - 1;
    }

    public DistinctRandomGenerator(int n) {
        objects = new Integer[n];
        for (int i = 0; i < n; i++)
            objects[i] = i;
        maxIndex = n - 1;
    }

    public Object next() {
        if (maxIndex <= -1)
            return null;
        else if (maxIndex == 0) {
            maxIndex = -1;
            return objects[0];
        }
        int index = r.nextInt(maxIndex + 1);

        Object ret = objects[index];
        if (index != maxIndex)
            objects[index] = objects[maxIndex];
        maxIndex--;

        return ret;
    }
}
