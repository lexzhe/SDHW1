import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LRUCacheImplTest {

    LRUCache<Integer, Integer> lruCache;
    int capacity = 10;

    @Before
    public void setupLruCache() {
        lruCache = new LRUCacheImpl<>(capacity);
    }
    
    @Test
    public void incorrectCapacity() {
        try {
            new LRUCacheImpl<>(0);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Capacity should be >= 1");
        } catch (Exception e) {
            Assert.fail();
        }
    }
    
    @Test
    public void empty() {
        Assert.assertNull(lruCache.get(1));
    }

    @Test
    public void onePut() {
        Assert.assertNull(lruCache.get(1));
        lruCache.put(1, 1);
        Assert.assertEquals(1, (int) lruCache.get(1));
    }

    @Test
    public void onePutAndRemove() {
        lruCache.put(1,1);
        lruCache.remove(1);
        Assert.assertNull(lruCache.get(1));
    }

    @Test
    public void capacityOverflow() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(1);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        Assert.assertNull(lruCache.get(1));
        Assert.assertEquals(2, (int) lruCache.get(2));
    }

    public void manyPut(int capacity, int overflow) {
        LRUCache<Integer,Integer> lruCache = new LRUCacheImpl<>(capacity);
        for (int i = 0; i < capacity + overflow; i++) {
            lruCache.put(i, i);
        }
        // overwritten values
        for (int i = 0; i < overflow; i++) {
            Assert.assertNull(lruCache.get(i));
        }
        // normal values
        for (int i = overflow; i < capacity + overflow; i++) {
            Assert.assertEquals(i, (int) lruCache.get(i));
        }
    }

    @Test
    public void bigCapSmallOverflow() {
        manyPut(100, 10);
    }

    @Test
    public void bidCapBigOverflow() {
        manyPut(1234567, 1234567);
    }

    @Test
    public void manyPutAndRemoveString() {
        int capacity = 20;
        LRUCache<String, String> lruCache = new LRUCacheImpl<>(capacity);
        for (int i = 0; i < capacity; i++) {
            lruCache.put("k=" + i, "v=" + i);
        }
        for (int i = 0; i < capacity; i++) {
            Assert.assertEquals(lruCache.get("k=" + i), "v=" + i);
        }
        lruCache.remove("k=0");
        for (int i = 1; i < capacity; i++) {
            Assert.assertEquals(lruCache.get("k=" + i), "v=" + i);
        }
        Assert.assertNull(lruCache.get("k=0"));

        lruCache.remove("k=2");
        Assert.assertEquals("v=1", lruCache.get("k=1"));
        Assert.assertNull(lruCache.get("k=2"));
        for (int i = 3; i < capacity; i++) {
            Assert.assertEquals(lruCache.get("k=" + i), "v=" + i);
        }

        lruCache.put("k=" + 2, "v=" + 20);
        for (int i = 1; i < capacity; i++) {
            Assert.assertEquals(lruCache.get("k=" + i), "v=" + ((i == 2) ? 20 : i));
        }
    }

    @Test
    public void OrderTest() {
        for (int i = 0; i < capacity; i++) {
            lruCache.put(i, i);
        }
        lruCache.put(capacity, capacity);
        for (int i = 1; i < capacity + 1; i++) {
            Assert.assertEquals(i, (int) lruCache.get(i));
        }
        lruCache.get(2);
        lruCache.put(capacity + 1, capacity + 1);
        for (int i = 2; i < capacity + 1; i++) {
            Assert.assertEquals(i, (int) lruCache.get(i));
        }
    }
}
