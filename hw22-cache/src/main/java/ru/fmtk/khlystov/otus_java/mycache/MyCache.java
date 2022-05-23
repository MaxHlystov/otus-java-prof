package ru.fmtk.khlystov.otus_java.mycache;


import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    public enum Action {ADD, REMOVE, GET}

    private final Set<HwListener<K, V>> listeners = new HashSet<>();
    private final Map<K, V> values;

    public MyCache() {
        values = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        notifyListeners(key, value, Action.ADD);
        values.put(key, value);
    }

    @Override
    public void remove(K key) {
        V value = values.remove(key);
        notifyListeners(key, value, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = values.get(key);
        notifyListeners(key, value, Action.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public String toString() {
        return "MyCache{" + values + '}';
    }

    private void notifyListeners(K key, V value, Action action) {
        listeners.forEach(listener -> listener.notify(key, value, action.toString()));
    }
}
