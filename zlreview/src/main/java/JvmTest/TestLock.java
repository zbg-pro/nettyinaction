package JvmTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLock {

    private Lock lock = new ReentrantLock();

    public void Locktest(Thread thread){
        lock.lock();

        try {
            System.out.println("线程： " + thread.getName() + "获取到当前锁");
            Thread.sleep(2000);//为看出执行效果，是线程此处休眠2秒
        } catch (Exception e) {
            System.out.println("线程"+thread.getName() + "发生了异常释放锁");
        } finally {
            System.out.println("线程"+thread.getName() + "执行完毕释放锁");
            lock.unlock();
        }

    }

    public void trlockTest(Thread thread){
        if(lock.tryLock()) {
            try {
                System.out.println("线程： " + thread.getName() + "获取到当前锁");
                Thread.sleep(2000);//为看出执行效果，是线程此处休眠2秒
            } catch (Exception e) {
                System.out.println("线程"+thread.getName() + "发生了异常释放锁");
            } finally {
                System.out.println("线程"+thread.getName() + "执行完毕释放锁");
                lock.unlock();
            }


        } else {
            System.out.println("我是线程"+Thread.currentThread().getName()+"当前锁被别人占用，我无法获取");
        }
    }

    public void tryLockParamsTest (Thread thread) throws InterruptedException {
        if(lock.tryLock(30, TimeUnit.MILLISECONDS)) {
            lock.lock();
            try {
                System.out.println("线程"+thread.getName() + "获取当前锁"); //打印当前锁的名称
                Thread.sleep(40);//为看出执行效果，是线程此处休眠2秒
            } catch (Exception e) {
                System.out.println("线程"+thread.getName() + "发生了异常释放锁");
            }finally {
                System.out.println("线程"+thread.getName() + "执行完毕释放锁");
                lock.unlock(); //释放锁
                System.out.println("线程"+thread.getName() + "已经释放锁");
            }
        } else {
            System.out.println("我是线程"+Thread.currentThread().getName()+"当前锁被别人占用，我无法获取");
        }
    }

    public void tryLockParamsTest2 (Thread thread) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        if(lock.tryLock(30000, TimeUnit.MILLISECONDS)) {
            lock.lock();
            try {
                System.out.println("线程"+thread.getName() + "获取当前锁"); //打印当前锁的名称
                Thread.sleep(400);//为看出执行效果，是线程此处休眠2秒
            } catch (Exception e) {
                System.out.println("线程"+thread.getName() + "发生了异常释放锁");
            }finally {
                System.out.println("线程"+thread.getName() + "执行完毕释放锁");
                lock.unlock(); //释放锁
            }
        } else {
            System.out.println("我是线程"+Thread.currentThread().getName()+
                    "当前锁被别人占用，我无法获取,等待时间：" +( System.currentTimeMillis()-startTime));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestLock testLock = new TestLock();

       Thread t1 =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    testLock.tryLockParamsTest(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        Thread.sleep(10);
        Thread t2 =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    testLock.tryLockParamsTest2(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();








    }

























}
