package com.async.task.abstracts;

import java.util.List;

/**
 * @author liucc
 * 本地持久化方式
 */
public abstract class AbstractPersistenceHandler {

    /**
     * 持久化到本地存储器
     *
     * @param command 可行性命令
     * @param key     唯一关键字
     */
    public abstract void persistence2LocalStorage(final Runnable command, final String key);

    /**
     * 从本地存储器装载
     *
     * @param key 唯一关键字
     */
    public abstract List<Runnable> loadFromLocalStorage(final String key);

}
