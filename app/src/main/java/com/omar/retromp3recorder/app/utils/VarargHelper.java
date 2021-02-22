package com.omar.retromp3recorder.app.utils;

import androidx.core.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class VarargHelper {

    @SafeVarargs
    public static <T> LinkedList<T> createLinkedList(T... args) {
        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, args);
        return list;
    }

    @SafeVarargs
    public static <T> HashSet<T> createHashSet(T... args) {
        HashSet<T> set = new HashSet<>();
        Collections.addAll(set, args);
        return set;
    }

    @SafeVarargs
    public static <K,V> HashMap<K,V> createHashMap(Pair<K,V>... args) {
        HashMap<K,V> map = new HashMap<>();
        for (Pair<K, V> arg : args) {
            map.put(arg.first, arg.second);
        }
        return map;
    }
}
