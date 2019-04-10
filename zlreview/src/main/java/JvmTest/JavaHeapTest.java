package JvmTest;

/**
 * Created by hl on 2019/4/10.
 */
public class JavaHeapTest {

    public final  static int OUTOFMEMORY = 2000000000;

    private String oom;

    private int length;

    StringBuffer tempOOM = new StringBuffer();

    public JavaHeapTest(int leng) {
        this.length = leng;
        int i = 0;
        while (true) {
            i++;
            try {
                Thread.sleep(100);
                tempOOM.append("a");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public String getOom() {
        return oom;
    }

    public int getLength() {
        return length;
    }

    public static void main(String[] args) {
        while (true) {
            JavaHeapTest javaHeapTest = new JavaHeapTest(OUTOFMEMORY);
            System.out.println(javaHeapTest.getOom().length());
        }
    }
}
