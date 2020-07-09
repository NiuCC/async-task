package com.async.task.annotations;

import java.lang.annotation.*;

/**
 * @author liucc
 * 执行方案配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ATExecutorConfig {
    /**
     * 最大执行次数
     */
    int maxExecuteCount() default 999;

    /**
     * 最大超时时间
     * 单位,秒
     */
    long maxTimeOutSeconds() default 999L;

    /**
     * 任务补偿频率
     * 单位,秒
     */
    long compensateRateSeconds() default 5L;

    /**
     * 自定义Redis持久化前缀
     */
    String undifineRedisPrefix() default "async::task::";

}
