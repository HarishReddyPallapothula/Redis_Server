package org.scratch.command;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {
    private static final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> expiryMap = new ConcurrentHashMap<>();

    public static void set(String key, String value, Long expiryMillis) {
        store.put(key, value);
        if (expiryMillis != null) {
            expiryMap.put(key, System.currentTimeMillis() + expiryMillis);
        } else {
            expiryMap.remove(key); // no expiry
        }
    }

    public static String get(String key) {
        Long expireAt = expiryMap.get(key);
        if (expireAt != null && System.currentTimeMillis() > expireAt) {
            // Expired
            store.remove(key);
            expiryMap.remove(key);
            return null;
        }
        return store.get(key);
    }
}

