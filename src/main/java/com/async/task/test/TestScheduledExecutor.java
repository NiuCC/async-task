package com.async.task.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author liucc
 * 线程池任务执行验证
 * 1. 建立大小为2000的线程池, 批量添加延迟3s的任务到池中,频率为每2秒执行一次
 * 2. 每500毫秒监控一下当前系统线程数,验证是否会因为任务数量的增加而导致池子内的线程数增加
 * 3.验证结果为: 池子内会一直保持核心线程数,不会因为任务数量底层而创建新的线程, 如果一次性无法执行完成当前的任务,剩下的任务将顺延到下一次任务执行
 */
public class TestScheduledExecutor {

    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("pool-async-task2-rebuild-%s").build();
    private static final ScheduledThreadPoolExecutor EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(2000, NAMED_THREAD_FACTORY);

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int finalI = i;
            EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("我是线程%s,我正在执行第%s个任务, 当前时间戳(秒): %s", Thread.currentThread().getName(), finalI, System.currentTimeMillis() / 1000));
            }, 2, 2, TimeUnit.SECONDS);
        }
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
            System.out.print(maps.keySet().size() + "\t");
        }
    }


}
