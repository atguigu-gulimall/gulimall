package com.atguigu.gulimall.product.zeanzai;

import java.util.concurrent.*;


public class MyCompletableFutureTest {
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            200,
            10,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(100000),
            new MyThreadFactory("zeanzai"),
            new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCompletableFutureTest myCompletableFutureTest = new MyCompletableFutureTest();
        myCompletableFutureTest.test1();


    }

    public void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 4;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).handle((res, thr) -> {
            if (res != null) {
                return res * 2;
            }
            if (thr != null) {
                return 0;
            }
            return 0;
        });
    }

    /**
     * 方法完成后的感知
     * <p>
     * 1. whenComplete 可以处理正常和异常的计算结果
     * <p>
     * whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     * whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
     * <p>
     * 2. exceptionally 处理异常情况。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void test2() throws ExecutionException, InterruptedException {

        /**
         *
         */
        CompletableFuture<Integer> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("当前线程：" + Thread.currentThread().getName());
                    int i = 10 / 0;
                    System.out.println("运行结果：" + i);
                    return i;
                }, executor) // 这里生成一个 <U> CompletableFuture<U> 对象

                // 由上一步生成的 CompletableFuture 对象继续调用 whenComplete 方法
                // 结果仍然是一个  CompletableFuture<T> 对象；
                .whenComplete((res, excption) -> {
                    //虽然能得到异常信息，但是没法修改返回数据。
                    System.out.println("异步任务成功完成了...");
                    System.out.println("结果是：" + res);
                    System.out.println("异常是：" + excption);
                    res = Integer.valueOf(res + "sss");
                })

                .exceptionally(throwable -> {
                    //可以感知异常，同时返回默认值
                    System.out.println("运行结果：10");
                    return 10;
                });

        executor.shutdown();

    }

    /**
     * 目的在于构造出一个 CompletableFuture 对象
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void testGenCompletableFuture() throws InterruptedException, ExecutionException {
        //1、使用runAsync或supplyAsync发起异步调用；
        //      如果不传自定义的线程池，则使用默认的线程池；
        //      使用自定义的线程池时，main线程会一直处于一个可运行的状态，这是因为没有关闭自定义线程池；
        // 1.1 supplyAsync : 有返回值
        CompletableFuture<String> cf11 = CompletableFuture.supplyAsync(() -> {
            System.out.println("cf11当前线程：" + Thread.currentThread().getName());
            return "cf11";
        });
        String s = cf11.get();
        cf11.complete(s);

        CompletableFuture<String> cf12 = CompletableFuture.supplyAsync(() -> {
            System.out.println("cf12当前线程：" + Thread.currentThread().getName());
            return "cf12";
        }, executor);
        cf12.complete(cf12.get());

        // 1.2 runAsync ： 没有返回值
        CompletableFuture<Void> cf21 = CompletableFuture.runAsync(() -> {
            System.out.println("cf21当前线程：" + Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println("cf21运行结果：" + i);
        });
        cf21.complete(cf21.get());

        CompletableFuture<Void> cf22 = CompletableFuture.runAsync(() -> {
            System.out.println("cf22当前线程：" + Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println("cf22运行结果：" + i);
        }, executor);
        cf22.complete(cf22.get());

        // executor.shutdown(); // 如果不关闭自定义线程池，会导致main线程一直处于runnalbe状态；

        //2、CompletableFuture.completedFuture()直接创建一个已完成状态的CompletableFuture
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("result2");
        cf2.complete("cf2-success");

        //3、先初始化一个未完成的CompletableFuture，然后通过complete()、completeExceptionally()，完成该CompletableFuture
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("cf-success");
    }

}