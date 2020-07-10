package com.async.task.api;

/**
 * @author liucc
 * 异步任务处理器
 */
public interface AsyncHandler<R1, RP> {
    /**
     * 异步任务执行器
     *
     * @param r1 正向流程请求参数
     * @param r2 补偿流程请求参数
     * @return rp 正向流程响应参数
     */
    RP asyncTaskExecutor(R1 r1);

    /**
     * 获取指定类型处理器
     *
     * @param clazz 处理器字节码对象
     * @param r1    请求参数
     * @return rp 正常流程的处理结果
     */
    @SuppressWarnings("unchecked")
    static <R1, RP> RP handle(final Class<?> clazz, R1 r1) {
        try {
            Object o = clazz.newInstance();
            if (o instanceof AsyncHandler) {
                AsyncHandler<R1, RP> handler = (AsyncHandler<R1, RP>) o;
                return handler.asyncTaskExecutor(r1);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
