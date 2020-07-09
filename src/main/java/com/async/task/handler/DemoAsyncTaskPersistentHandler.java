package com.async.task.handler;

import com.async.task.abstracts.AbstractPersistenceHandler;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author liucc
 * 使用Redis做持久化
 */
public class DemoAsyncTaskPersistentHandler extends AbstractPersistenceHandler {
    @Override
    public void persistence2LocalStorage(Runnable command, String key) {

    }

    @Override
    public List<Runnable> loadFromLocalStorage(String key) {
        return Lists.newArrayList();
    }
}
