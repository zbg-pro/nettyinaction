package JvmTest;

/**
 * Created by hl on 2019/4/10.
 */
public class MemoryCpuTest {

    public static void main(String[] args) throws InterruptedException {
        cpuFix();
    }

    private static void cpuFix() throws InterruptedException {
        int busyTime = 8;

        int idealTime = 2;

        long startTime = 0;

        while (true) {
            startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < busyTime) {
                ;
            }

            Thread.sleep(idealTime);
        }
    }
}

