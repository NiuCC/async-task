package com.async.task.test;

import com.async.task.api.AsyncHandler;
import com.async.task.config.AsyncTaskApplicationConfig;
import com.async.task.handler.DemoAsyncTaskHandler;

/**
 * 测试程序
 *
 * @author liucc
 */
public class TestDemoAsyncTaskHandler {

    public static void main(String[] args) {
        //0.任务全局配置
        new AsyncTaskApplicationConfig(100);
        //1.实例化Demo,测试一下任务执行情况
        AsyncHandler.handle(DemoAsyncTaskHandler.class, 1, 2);
    }
}
