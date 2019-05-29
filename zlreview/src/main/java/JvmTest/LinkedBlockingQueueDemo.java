package JvmTest;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hl on 2019/5/8.
 */
public class LinkedBlockingQueueDemo {

    // 生产者线程数量
    private final static int providerThreadAmount = 5;

    // 记录每一个生产者线程是否处理完毕的标记
    private static boolean[] providerDoneFlag = new boolean[providerThreadAmount];

    // 整个所有的生产者线程全部结束的标记
    private static boolean done = false;

    //线程安全队列，用于生产者和消费者异步消息交互
    private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();

    static class ProviderThread extends Thread{

        private String threadName;

        private int threadNo;

        private Thread thread;

        public ProviderThread(String threadName, int threadNo){
            this.threadName = threadName;
            this.threadNo = threadNo;
        }

        public void start(){
            if(thread == null) {
                thread = new Thread(this, threadName);
            }
            thread.start();
            System.out.println(
                    (new Date().getTime()) + " " + threadName + " starting... " + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            int rows = 0;
            for (int i = 0; i < 100; i++) {
                String string = String.format("%s-%d-%s", threadName, i, Thread.currentThread().getName());
                // offer不会去阻塞线程，put会
                //linkedBlockingQeque.offer(string);
                try {
                    linkedBlockingQueue.put(string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rows++;
                /*
                 * try { Thread.sleep((new Random()).nextInt(5) * 1000); } catch
                 * (InterruptedException e) { e.printStackTrace(); }
                 */
            }

            // 本线程处理完毕的标记
            LinkedBlockingQueueDemo.providerDoneFlag[threadNo] = true;
            System.out.println((new Date().getTime()) + " " + threadName + " end. total rows is " + rows + "\t"
                    + Thread.currentThread().getName());
        }
    }

    static class ConsumerThread implements Runnable {
        private Thread thread;
        private String threadName;

        public ConsumerThread(String threadName2) {
            this.threadName = threadName2;
        }

        public void start() {
            if (thread == null) {
                thread = new Thread(this, threadName);
            }

            thread.start();
            System.out.println(
                    (new Date().getTime()) + " " + threadName + " starting... " + Thread.currentThread().getName());
        }

        public void run() {
            int rows = 0;

            while(!getDone() || !linkedBlockingQueue.isEmpty()){
                try {

                    //在甘肃电信的实际应用中发现，当数据的处理量达到千万级的时候，带参数的poll会将主机的几百个G的内存耗尽，jvm会提示申请内存失败，并将进程退出。网上说，这是这个方法的一个bug。
                    //String string = linkedBlockingQeque.poll(3, TimeUnit.SECONDS);
                    String string = linkedBlockingQueue.poll();
                    if (string == null) {
                        continue;
                    }

                    rows ++;

                    System.out.println((new Date().getTime()) + " " + threadName + " get msg from linkedBlockingQeque is "
                            + string + "\t" + Thread.currentThread().getName());

                    /*
                      try {
                        Thread.sleep((new Random()).nextInt(5) * 1000);
                      } catch
                       (InterruptedException e) {
                        e.printStackTrace(); }
                       */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println((new Date().getTime()) + " " + threadName + " end total rows is " + rows + "\t"
                    + Thread.currentThread().getName());


        }
    }

    public static synchronized void setDone(boolean flag){
        done = flag;
    }

    public static synchronized boolean getDone(){
        return done;
    }


    public static void main(String[] args) {
        System.out.println((new Date().getTime()) + " " + "process begin at " + Thread.currentThread().getName());
        System.out.println(
                (new Date().getTime()) + " " + "linkedBlockingDeque.hashCode() is " + linkedBlockingQueue.hashCode());


        // 启动若干生产者线程
        for (int i = 0; i < providerThreadAmount; i++) {
            String threadName = String.format("%s-%d", "ProviderThread", i);
            ProviderThread providerThread = new ProviderThread(threadName, i);
            providerThread.start();
        }


        // 启动若干个消费者线程
        for (int i = 0; i < 10; i++) {
            String threadName = String.format("%s-%d", "ConsumerThread", i);
            ConsumerThread consumerThread = new ConsumerThread(threadName);
            consumerThread.start();
        }

        do {
            for (boolean b: providerDoneFlag) {
                if(!b) {
                    /*
                     * try { Thread.sleep(3 * 1000); System.out.println((new Date().getTime()) +
                     * " "+"sleep 3 seconds. linkedBlockingQeque.size() is "+linkedBlockingQeque.
                     * size() + "\t" + Thread.currentThread().getName()); } catch
                     * (InterruptedException e) { e.printStackTrace(); }
                     */

                    // 只要有一个生产者线程没有结束，则整个生产者线程检测认为没有结束
                    break;
                }
                LinkedBlockingQueueDemo.setDone(true);
            }

            // 生产者线程全部结束的时候，跳出检测
            if (LinkedBlockingQueueDemo.getDone() == true) {
                break;
            }

        } while (true);

        System.out.println((new Date().getTime()) + " process done successfully\t" + Thread.currentThread().getName());

    }
}
