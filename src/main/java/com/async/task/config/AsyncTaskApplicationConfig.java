package com.async.task.config;

import com.async.task.abstracts.AbstractPersistenceHandler;
import com.async.task.annotations.PersistenceBean;
import com.async.task.helper.AdderssHelper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author liucc
 * 项目入口配置类
 */
public class AsyncTaskApplicationConfig {

    /**
     * 公共任务池
     */
    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("pool-async-task-%s").build();
    private static ScheduledThreadPoolExecutor EXECUTOR_SERVICE = null;
    /**
     * 持久化组件
     */
    private static AbstractPersistenceHandler persistenceHandler = null;

    /**
     * 获取持久化处理器
     */
    public static AbstractPersistenceHandler getPersistenceHandler() {
        return persistenceHandler;
    }

    /**
     * 获取任务池
     */
    public static ScheduledThreadPoolExecutor getThreadPool() {
        return EXECUTOR_SERVICE;
    }

    public AsyncTaskApplicationConfig(int corePoolSize) {
        //0.初始化线程池
        this.rebuildThreadPool(corePoolSize);
        //1.加载持久化组件
        this.reflectGetPersistenceHandler();
        if (persistenceHandler == null) {
            return;
        }
        //2.本地装载上次未完成的任务
        List<Runnable> runnables = persistenceHandler.loadFromLocalStorage(this.buildPersistenceLocalKey());
        if (runnables.size() == 0) {
            return;
        }
        //3.任务重新执行
        runnables.forEach(command -> {
            EXECUTOR_SERVICE.scheduleWithFixedDelay(command, 5, 5, TimeUnit.SECONDS);
        });
    }

    /**
     * 初始化任务池
     *
     * @param corePoolSize 线程数
     */
    private void rebuildThreadPool(final int corePoolSize) {
        EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(corePoolSize, NAMED_THREAD_FACTORY);
        //0.设置当任务被取消时,立即从队列中删除任务
        EXECUTOR_SERVICE.setRemoveOnCancelPolicy(true);
    }

    /**
     * 反射获取项目入口配置处理器
     */
    private void reflectGetPersistenceHandler() {
        Reflections reflections = new Reflections("com.async");
        Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(PersistenceBean.class);
        for (Class<?> classes : clazzes) {
            try {
                persistenceHandler = (AbstractPersistenceHandler) classes.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建持久化关键字
     */
    private String buildPersistenceLocalKey() {
        final String localHostAddress = AdderssHelper.getHostIp();
        return "" + "::" + localHostAddress + "::" + this.getClass().getCanonicalName();
    }

}
