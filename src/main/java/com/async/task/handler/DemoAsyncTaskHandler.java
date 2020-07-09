package com.async.task.handler;

import com.async.task.abstracts.AbstractAsyncTaskHandler;
import com.async.task.annotations.ATExecutorConfig;

/**
 * @author liucc
 * 实例处理类
 */
@ATExecutorConfig(maxExecuteCount = 4)
public class DemoAsyncTaskHandler extends AbstractAsyncTaskHandler<Object, Object, Object> {
    @Override
    protected Object processor(Object o) {
        System.out.println("[主任务]订单10001支付中...");
        return o;
    }

    @Override
    protected void postCompensateProcessor(Object o) {
        System.out.println("[补偿任务] 查询订单1001支付状态: 支付成功!");
    }
}
