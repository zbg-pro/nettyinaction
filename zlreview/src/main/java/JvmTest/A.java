package JvmTest;

import java.io.Serializable;
import java.util.*;

/**
 * Created by hl on 2019/4/22.
 */
public class A implements Serializable {
    private final int num;
    public A(int num) {
        System.out.println("Hello Mum");
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> arrayList = new ArrayList<String>();
        arrayList.add("s");
        arrayList.add("e");
        arrayList.add("n");
/**
 * ArrayList转数组
 */
        int size=arrayList.size();
        String[] a = arrayList.toArray(new String[size]);
//输出第二个元素
        System.out.println(a[1]);//结果：e
//输出整个数组
        System.out.println(Arrays.toString(a));//结果：[s, e, n]
/**
 * 数组转list
 */
        List<String> list=Arrays.asList(a);
/**
 * list转Arraylist
 */
        List<String> arrayList2 = new ArrayList<String>();
        arrayList2.addAll(list);
        System.out.println(list);

        StringBuffer sb = new StringBuffer("123");
        StringBuilder stringBuilder = new StringBuilder("23434");

        Map map = new HashMap();
        map=Collections.synchronizedMap(map);

        Map map2 = new TreeMap();

        Thread.sleep(0);
    }
}
