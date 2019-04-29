package JvmTest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hl on 2019/4/11.
 */
public class ReadBlackTree {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Map aa = new HashMap();
        aa.put("dd", "11");
        aa.get("dd");

        ConcurrentHashMap a = new ConcurrentHashMap();
        a.put("1", "2");

        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        //unsafe.compareAndSwapObject()

        unsafe.compareAndSwapLong(null, 12l, 9l, 6l);





    }
}
