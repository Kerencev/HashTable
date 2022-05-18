import java.util.LinkedList;
import java.util.List;

public class HashTableImpl<K, V> implements HashTable<K, V> {
    private final List<Item<K, V>>[] data;

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
        this.data = new LinkedList[initialCapacity * 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
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

        while (true) {
            if (data[indexFromHashFunc].size() == 0) {
                data[indexFromHashFunc].add(new Item<>(key, value));
                size++;
                break;
            } else if (data[indexFromHashFunc].get(0).getKey().equals(key)) {
                data[indexFromHashFunc].add(new Item<>(key, value));
                break;
            }

            indexFromHashFunc += getDoubleHash(key);
            indexFromHashFunc %= data.length;
        }

        return true;
    }

    private int getDoubleHash(K key) {
        int n = data.length / 2;
        return n - (key.hashCode() % n);
    }

    private int hashFunc(K key) {
        return Math.abs(key.hashCode() % data.length);
    }

    @Override
    public V[] get(K key) {
        int index = indexOf(key);

        if (index == -1) {
            return null;
        }

        V[] arr = (V[]) new Object[data[index].size()];

        for (int i = 0; i < data[index].size(); i++) {
            arr[i] = data[index].get(i).getValue();
        }

        return arr;
    }

    private int indexOf(K key) {
        int indexFromHashFunc = hashFunc(key);

        int count = 0;
        while (count++ < data.length) {
            if (data[indexFromHashFunc].size() == 0) {
                break;
            }
            if (data[indexFromHashFunc].get(0).getKey().equals(key)) {
                return indexFromHashFunc;
            }
            indexFromHashFunc += getDoubleHash(key);
            indexFromHashFunc %= data.length;
        }

        return -1;
    }

    @Override
    public V[] remove(K key) {
        int index = indexOf(key);
        if (index == -1) {
            return null;
        }

        LinkedList<Item<K, V>> removed = (LinkedList<Item<K, V>>) data[index];

        data[index] = new LinkedList<>();

        V[] arr = (V[]) new Object[data[index].size()];

        for (int i = 0; i < data[index].size(); i++) {
            arr[i] = removed.get(i).getValue();
        }

        return arr;
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
