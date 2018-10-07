package se.gewalli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <T> Collection<Collection<T>> batchesOf(Collection<T> enumerable, int count) {
        List<Collection<T>> result = new ArrayList<>(count);
        Iterator<T> enumerator = enumerable.iterator();
        while (true) {
            List<T> list = new ArrayList<>(count);
            for (int i = 0; i < count && enumerator.hasNext(); i++) {
                T t = enumerator.next();
                list.add(t);
            }
            if (list.isEmpty()) {
                break;
            }
            result.add(list);
        }
        return result;
    }
}
