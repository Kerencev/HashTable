public class HashTableImpl <K, V> implements HashTable<K, V>{
    private final Item<K, V>[] data;
    private final Item<K, V> emptyItem;
    private int size;

    static class Item<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        Item(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("key: %s -> value: %s", key, value);
        }
    }

    public HashTableImpl(int initialCapacity) {
        this.data = new Item[initialCapacity * 2];
        emptyItem = new Item<>(null, null);
    }

    public HashTableImpl() {
        this(16);
    }

    @Override
    public boolean put(K key, V value) {
        if (size() == data.length) {
            return false;
        }

        int indexFromHashFunc = hashFunc(key);
        int n = 0;

        while (data[indexFromHashFunc] != null && data[indexFromHashFunc] != emptyItem) {
            if (isKeysEquals(data[indexFromHashFunc], key)) {
                data[indexFromHashFunc].setValue(value);
                return true;
            }
//            indexFromHashFunc += getStepLinear();
//            indexFromHashFunc += getStepQuadratic(n++);
            indexFromHashFunc += getDoubleHash(key);

            indexFromHashFunc %= data.length;
        }

        data[indexFromHashFunc] = new Item<>(key, value);
        size++;

        return true;
    }

    private int getDoubleHash(K key) {
        int n = data.length / 2;
        return n - (key.hashCode() % n);
    }

    private int getStepQuadratic(int i) {
        return (int) Math.pow(i, 2);
    }

    private int getStepLinear() {
        return 1;
    }

    private boolean isKeysEquals(Item<K, V> item, K key) {
        if (item == emptyItem) {
            return false;
        }
        return (item.getKey() == null) ? (key == null) : (item.getKey().equals(key));
    }

    private int hashFunc(K key) {
        return Math.abs(key.hashCode() % data.length);
    }

    @Override
    public V get(K key) {
        int index = indexOf(key);
        return index == -1 ? null : data[index].getValue();
    }

    private int indexOf(K key) {
        int indexFromHashFunc = hashFunc(key);

        int count = 0;
        while (count++ < data.length) {
            if (data[indexFromHashFunc] == null) {
                break;
            }
            if (isKeysEquals(data[indexFromHashFunc], key)) {
                return indexFromHashFunc;
            }
            indexFromHashFunc += getDoubleHash(key);
            indexFromHashFunc %= data.length;
        }

        return -1;
    }

    @Override
    public V remove(K key) {
        int index = indexOf(key);
        if (index == -1) {
            return null;
        }

        Item<K, V> removed = data[index];
        data[index] = emptyItem;

        return removed.getValue();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%s = [%s]%n", i, data[i]));
        }
        return sb.toString();
    }
}