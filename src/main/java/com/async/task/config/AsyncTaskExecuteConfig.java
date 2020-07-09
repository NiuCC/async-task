package com.async.task.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liucc
 * 异步补偿任务配置项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskExecuteConfig {

    /**
     * 最大执行次数
     */
    public Integer maxExecuteCount;

    /**
     * 最大超时时间
     * 单位,秒
     */
    public Long maxTimeOutSeconds;

    /**
     * 任务补偿频率
     * 单位,秒
     */
    public Long compensateRateSeconds;

    /**
     * 自定义Redis前缀
     */
    public String undifineRedisPrefix;

}
