package com.safesolar.report;

import java.util.Comparator;
import java.util.List;

public final class QuickSort {
    private QuickSort() {}

    public static <T> void sort(List<T> values, Comparator<T> comparator) {
        if (values == null || values.size() < 2) return;
        quickSort(values, 0, values.size() - 1, comparator);
    }

    private static <T> void quickSort(List<T> values, int low, int high, Comparator<T> comparator) {
        if (low >= high) return;
        int pivot = partition(values, low, high, comparator);
        quickSort(values, low, pivot - 1, comparator);
        quickSort(values, pivot + 1, high, comparator);
    }

    private static <T> int partition(List<T> values, int low, int high, Comparator<T> comparator) {
        T pivot = values.get(high);
        int smaller = low - 1;
        for (int current = low; current < high; current++) {
            if (comparator.compare(values.get(current), pivot) <= 0) {
                smaller++;
                swap(values, smaller, current);
            }
        }
        swap(values, smaller + 1, high);
        return smaller + 1;
    }

    private static <T> void swap(List<T> values, int first, int second) {
        T temporary = values.get(first);
        values.set(first, values.get(second));
        values.set(second, temporary);
    }
}
