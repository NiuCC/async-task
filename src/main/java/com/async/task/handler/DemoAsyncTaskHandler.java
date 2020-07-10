package com.async.task.handler;

import com.async.task.abstracts.AbstractAsyncTaskHandler;
import com.async.task.annotations.ATExecutorConfig;

import java.util.Random;

/**
 * @author liucc
 * 实例处理类
 */
@ATExecutorConfig(maxExecuteCount = 4, maxTimeOutSeconds = 3600, compensateRateSeconds = 30)
public class DemoAsyncTaskHandler extends AbstractAsyncTaskHandler<Object, Object> {
    @Override
    protected Object processor(Object o) {
        System.out.println("[主任务]邀请1001用户进群...");
        return o;
    }

    @Override
    protected Boolean postCompensateProcessor(Object o) {
        System.out.println("[补偿任务] 查询1001用户是否在群内: 不在!");
        return new Random().nextBoolean();
    }
}
