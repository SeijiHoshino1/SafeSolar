package com.safesolar.report;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class QuickSortTest {
    @Test
    void sortsValuesUsingProvidedComparator() {
        List<Integer> values = new ArrayList<>(List.of(8, 2, 9, 1, 5, 3));
        QuickSort.sort(values, Comparator.naturalOrder());
        assertThat(values).containsExactly(1, 2, 3, 5, 8, 9);
    }
}
