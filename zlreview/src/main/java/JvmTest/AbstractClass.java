package JvmTest;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hl on 2019/4/11.
 */
public abstract  class AbstractClass {
    public static void aa(){

    }
    public abstract void bb();
}

class DD extends AbstractClass{

    @Override
    public void bb() {
    }

    public static void main(String[] args) {
        int hash = 101;
        int length = 16;
        int a = hash & (length-1);
        int b = hash | (length-1);
        System.out.println(a + "=" + hash + "&" + (length-1));
        System.out.println(b + "=" + hash + "|" + (length-1));

        System.out.println(true|false);
        System.out.println(true|true);
        System.out.println(false|false);

        System.out.println(true&false);
        System.out.println(true&true);
        System.out.println(false&false);

        int aa=2;
        System.out.println("a 非的结果是："+(~aa));

        Map map = new HashMap();
        map.put(1, "one");
        map.put(2, "two");
        map  = Collections.unmodifiableMap(map);

        map.put("2","2232");
    }
}