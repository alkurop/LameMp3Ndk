package com.omar.retromp3recorder.app.utils;

import java.util.Collections;
import java.util.LinkedList;

public class ListFromVararg {
    @SafeVarargs
    public static <T> LinkedList<T> createList(T... args) {
        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, args);
        return list;
    }
}
