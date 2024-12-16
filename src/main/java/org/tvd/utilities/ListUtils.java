package org.tvd.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {
    public static boolean isListSorted(List<String> list) {
        List<String> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);

        return list.equals(sortedList);
    }
}
