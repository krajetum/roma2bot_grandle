package krajetum.LTB.utils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Crunchify.com
 */

public class Cache<K, T> {

    private long timeToLive;
    private HashMap<K, T> crunchifyCacheMap;

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public Cache(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        crunchifyCacheMap = new HashMap<>(maxItems);

        if (timeToLive > 0 && timerInterval > 0) {
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timerInterval * 1000);
                    } catch (InterruptedException ex) {
                    }
                    cleanup();
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (crunchifyCacheMap) {
            crunchifyCacheMap.put(key, (T) new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (crunchifyCacheMap) {
            CacheObject c = (CacheObject) crunchifyCacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (crunchifyCacheMap) {
            crunchifyCacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (crunchifyCacheMap) {
            return crunchifyCacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (crunchifyCacheMap) {
            deleteKey = new ArrayList<>((crunchifyCacheMap.size() / 2) + 1);

            ArrayList<K> finalDeleteKey = deleteKey;
            crunchifyCacheMap.forEach((k, t) -> {
                CacheObject c = (CacheObject) t;

                if (t != null && (now > (timeToLive + c.lastAccessed))) {
                    finalDeleteKey.add(k);
                }
            });
            for(K key : finalDeleteKey) {
                synchronized (crunchifyCacheMap) {
                    crunchifyCacheMap.remove(key);
                }
                Thread.yield();
            }
        }

    }
}