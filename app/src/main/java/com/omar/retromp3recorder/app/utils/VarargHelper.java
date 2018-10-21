package com.omar.retromp3recorder.app.utils;

import java.util.Collections;
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
        HashSet<T> list = new HashSet<>();
        Collections.addAll(list, args);
        return list;
    }
}
