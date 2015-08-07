package it.playfellas.superapp.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by affo on 06/08/15.
 */
public class RandomUtils {

    public static <T> List<T> choice(Collection<T> coll, int n) {
        int size = n > coll.size() ? coll.size() : n;
        T[] a = (T[]) coll.toArray();

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < coll.size(); i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
        List<Integer> rndIndexes = indexes.subList(0, size);

        List<T> ret = new ArrayList<>();
        for (Integer i : rndIndexes) {
            ret.add(a[i]);
        }

        return ret;
    }

    public static <T> T choice(Collection<T> coll) {
        return choice(coll, 1).get(0);
    }

    public static <T> T choice(T[] a) {
        return a[(new Random()).nextInt(a.length)];
    }
}
