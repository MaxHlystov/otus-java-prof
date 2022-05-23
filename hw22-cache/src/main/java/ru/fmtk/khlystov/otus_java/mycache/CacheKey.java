package ru.fmtk.khlystov.otus_java.mycache;

import java.util.Objects;

/**
 * Used for internalized references with MyCache to let keys be weak and be removed from cache.
 * I don't want to use String as substitute for Long values.
 *
 * @param key - key reference
 * @param <K> - key type.
 */
public record CacheKey<K>(K key) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CacheKey<?> cacheKey = (CacheKey<?>) o;
        return Objects.equals(key, cacheKey.key);
    }

    @Override
    public String toString() {
        return Objects.toString(key);
    }
}
