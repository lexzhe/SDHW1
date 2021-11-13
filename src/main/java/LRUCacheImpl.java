import java.util.HashMap;
import javax.annotation.Nullable;

public class LRUCacheImpl<K, V> implements LRUCache<K, V> {
    private final HashMap<K, Node<K, V>> core;
    private final Node<K,V> head;
    private final Node<K,V> tail;
    private final int capacity;
    private int size;

    public LRUCacheImpl(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Capacity should be >= 1");

        tail = new Node<>(null, null, null, null);
        head = new Node<>(null, tail, null, null);
        core = new HashMap<>();
        size = 0;
        this.capacity = capacity;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not supported");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values are not supported");
        }
        assert core.size() <= capacity;

        remove(key);
        if (capacity == size){
            removeLast();
        }
        var oldFirst = head.right;
        var newFirst = new Node<>(head, oldFirst, key, value);
        head.right = newFirst;
        oldFirst.left = newFirst;
        core.put(key, newFirst);
        size++;
    }

    @Override
    @Nullable
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not supported");
        }
        assert core.size() <= capacity;

        if (core.containsKey(key)){
            return core.get(key).value;
        } else {
            return null;
        }
    }

    @Override
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not supported");
        }
        assert core.size() <= capacity;


        if (core.containsKey(key)){
            var toRemove = core.get(key);
            var left = toRemove.left;
            var right = toRemove.right;

            left.right = right;
            right.left = left;
            core.remove(key);
            size--;
        }

        assert !core.containsKey(key);
        assert size >= 0;
    }

    private void removeLast(){
        var last = tail.left;
        remove(last.key);
    }

    private static class Node<Key, Value> {
        Node<Key, Value> left;
        Node<Key, Value> right;

        Key key;

        Value value;

        public Node(Node<Key, Value> left, Node<Key, Value> right, Key key, Value value) {
            this.left = left;
            this.right = right;
            this.key = key;
            this.value = value;
        }
    }
}
