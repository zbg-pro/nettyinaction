package JvmTest;

import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomicInteger implements Runnable {

    private AtomicInteger count = new AtomicInteger(0);


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestAtomicInteger ta = new TestAtomicInteger();

        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(ta);
            t.start();
        }

        Thread.sleep(2000);
        System.out.println(ta.count);
    }
}
