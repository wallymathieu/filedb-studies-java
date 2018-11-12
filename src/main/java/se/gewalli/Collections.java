package se.gewalli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <T> Collection<Collection<T>> batchesOf(Collection<T> collection, int count) {
        List<Collection<T>> batches = new ArrayList<>(count);
        Iterator<T> iterator = collection.iterator();
        while (true) {
            List<T> list = new ArrayList<>(count);
            for (int i = 0; i < count && iterator.hasNext(); i++) {
                T t = iterator.next();
                list.add(t);
            }
            if (list.isEmpty()) {
                break;
            }
            batches.add(list);
        }
        return batches;
    }
}
