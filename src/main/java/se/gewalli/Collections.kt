package se.gewalli

object Collections {
    @JvmStatic
    fun <T> batchesOf(collection: Collection<T>, count: Int): Collection<Collection<T>> {
        val batches = ArrayList<Collection<T>>(count)
        val iterator = collection.iterator()
        while (true) {
            val list = ArrayList<T>(count)
            var i = 0
            while (i < count && iterator.hasNext()) {
                val t = iterator.next()
                list.add(t)
                i++
            }
            if (list.isEmpty()) {
                break
            }
            batches.add(list)
        }
        return batches
    }
}