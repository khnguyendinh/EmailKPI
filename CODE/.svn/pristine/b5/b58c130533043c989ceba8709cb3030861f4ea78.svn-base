/*
 * Copyright (C) 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.common.util;

import java.util.AbstractList;
import java.util.List;

public class Partition<T> extends AbstractList<List<T>> {

    final List<T> list;
    final int size;

    Partition(List<T> list, int size) {
        this.list = list;
        this.size = size;
    }

    @Override
    public List<T> get(int index) {
        int listSize = size();
        if (listSize < 0) {
            throw new IllegalArgumentException("negative size: " + listSize);
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                    "index " + index + " must not be negative");
        }
        if (index >= listSize) {
            throw new IndexOutOfBoundsException(
                    "index " + index + " must be less than size " + listSize);
        }
        int start = index * size;
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return (list.size() + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
