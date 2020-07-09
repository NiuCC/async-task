package com.async.task.enums;

/**
 * @author liucc
 * 定时任务状态
 */
public enum TaskStatusEnum {
    /**
     * U初始化,P执行中,S完成
     */
    U, P, S;

    /**
     * 检查是否可需要继续执行
     *
     * @param instance 任务状态实例
     * @return 是 否
     */
    public static boolean checkIfNeedContinueExecute(final TaskStatusEnum instance) {
        return instance == U || instance == P;
    }
}

