package JvmTest;

/**
 * Created by hl on 2019/4/29.
 */
public class TestJoin implements Runnable {

    public static Object obj = new Object();

    public void run()  {
        synchronized (obj) {
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("睡眠" + i);
            }
        }

        System.out.println("TestJoin finished");
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(new TestJoin());
        t1.start();t1.join();
        Thread t = new Thread(new TestJoin());
        t.start();
        t.join(1000);//join是等待此线程执行完才执行下面的东西

        System.out.println(System.currentTimeMillis()-start);//打印出时间间隔
        System.out.println("Main finished");//打印主线程结束

    }


}
