package com.async.task.api;

/**
 * @author liucc
 */
public interface AsyncHandler<R1, R2, RP> {
    /**
     * 异步任务执行器
     *
     * @param v 正向流程请求参数
     * @param e 补偿流程请求参数
     * @return r 正向流程响应参数
     */
    RP asyncTaskExecutor(R1 r1, R2 r2);

    /**
     * 获取指定类型处理器
     *
     * @param clazz 处理器字节码对象
     * @return handler 特定处理器
     */
    @SuppressWarnings("unchecked")
    static <R1, R2, RP> AsyncHandler<R1, R2, RP> getHandler(final Class<?> clazz) {
        try {
            Object o = clazz.newInstance();
            if (o instanceof AsyncHandler) {
                return (AsyncHandler<R1, R2, RP>) o;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
