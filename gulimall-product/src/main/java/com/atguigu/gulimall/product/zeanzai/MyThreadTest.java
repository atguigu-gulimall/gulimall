package com.atguigu.gulimall.product.zeanzai;

import java.util.concurrent.*;

public class MyThreadTest {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            200,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // test();
        // corePoolSize – the number of threads to keep in the pool, even if they are idle,
        // unless allowCoreThreadTimeOut is set
        executor.allowCoreThreadTimeOut(true);

        // 1. 使用 execute 执行 Thread 类型的任务；
        executor.execute(new Thread01());

        // 2. 使用 execute 执行 Runable 类型的任务；
        executor.execute(new Runable01());
        // 不能使用 execute 执行 Callable 类型的任务；
        // executor.execute(new Callable01());     // 报编译错误

        // 3. 使用 submit 提交 Runable 类型的任务
        executor.submit(new Thread01());

        // 4. 使用 submit 提交 Runable 类型的任务；
        executor.submit(new Runable01());

        // 5. 使用 submit 提交有 返回值的 Callable 类型的任务
        Future<Integer> submit = executor.submit(new Callable01());
        Integer result = submit.get(); // 获取结果
        System.out.println(result);
    }

    public static void test() throws ExecutionException, InterruptedException {
        /**
         * 1. 使用： public Thread(Runnable target) 构造函数；
         *  由于 Runnable 是一个 @FunctionalInterface 标注的接口，而这个注解的作用：
         *      标记Runnable接口是一个“函数式接口”。在Java中，“函数式接口”是有且仅有一个抽象方法的接口。
         *      反过来说，如果一个接口中包含两个或两个以上的抽象方法，就不能使用 @FunctionalInterface 注解，否则编译会报错。
         */
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }).start();

        // 使用的是 public Thread(Runnable target) 构造函数
        new Thread(new Thread01()).start();

        // 使用的是 public Thread(Runnable target) 构造函数
        new Thread(new Runable01()).start();

        // 使用的是 public Thread(Runnable target) 构造函数
        FutureTask<Integer> result = new FutureTask<>(new Callable01());
        new Thread(result).start();
        System.out.println(result.get());   // 阻塞获取，可以获取正常结果，也可以捕获异常；
    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }


    public static class Runable01 implements Runnable {

        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }
    }

}