package se.gewalli;

import java.util.ArrayList;
import java.util.Collection;

public class Collections {
    public static <T> Collection<Collection<T>> batchesOf(Collection<T> collection, int count) {
        var batches = new ArrayList<Collection<T>>(count);
        var iterator = collection.iterator();
        while (true) {
            var list = new ArrayList<T>(count);
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
