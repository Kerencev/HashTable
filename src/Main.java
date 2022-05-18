import java.util.Arrays;
import java.util.Hashtable;

public class Main {

    public static void main(String[] args) {

        HashTable<Product, Integer> hashTable = new HashTableImpl<>(5);

        hashTable.put(new Product(1, "Orange"), 150); //1
        hashTable.put(new Product(77, "Banana"), 100); //7
        hashTable.put(new Product(67, "Carrot"), 228); //8
        hashTable.put(new Product(60, "Lemon"), 250); //0
        hashTable.put(new Product(51, "Milk"), 120); //2
        hashTable.put(new Product(21, "Potato"), 67); //3

        hashTable.put(new Product(1, "Orange"), 200);

        hashTable.display();

        System.out.println(Arrays.toString(hashTable.get(new Product(1, "Orange"))));

        hashTable.remove(new Product(1, "Orange"));
        hashTable.display();
    }
}