package com.async.task.annotations;

import com.async.task.enums.PersistenceTypeEnum;

import java.lang.annotation.*;

/**
 * @author liucc
 * 执行方案配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PersistenceBean {
    /**
     * 持久化数据源,作者强力推荐优先使用REDIS
     */
    PersistenceTypeEnum type()
            default PersistenceTypeEnum.REDIS;
}
