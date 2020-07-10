package com.async.task.abstracts;

import com.async.task.annotations.ATExecutorConfig;
import com.async.task.api.AsyncHandler;
import com.async.task.config.AsyncTaskApplicationConfig;
import com.async.task.config.AsyncTaskExecuteConfig;
import com.async.task.enums.TaskStatusEnum;
import com.async.task.helper.AdderssHelper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liucc
 * 异步任务核心处理类
 */
public abstract class AbstractAsyncTaskHandler<R1, RP> implements AsyncHandler<R1, RP> {

    /**
     * 任务状态
     */
    private TaskStatusEnum taskStatusInstance = TaskStatusEnum.U;
    /**
     * 任务执行次数统计
     */
    private Integer executeCount = 0;
    /**
     * 任务创建时间,单位:毫秒
     */
    private Long taskCreateTime = 0L;
    /**
     * 异步补偿配置
     */
    private AsyncTaskExecuteConfig executeConfig = null;
    /**
     * 异步任务执行结果
     */
    private ScheduledFuture<?> scheduledFuture = null;
    /**
     * 持久化与装载组件
     */
    private AbstractPersistenceHandler persistenceHandler = null;
    /**
     * 线程池
     */
    private ScheduledThreadPoolExecutor threadPoolExecutor = null;

    /**
     * 正向处理器
     *
     * @param r1 参数
     * @return 返回值
     */
    protected abstract RP processor(R1 r1);

    /**
     * 反向补偿处理器
     *
     * @param r2 请求参数
     */
    protected abstract Boolean postCompensateProcessor(R1 r1);

    /**
     * 异步任务执行器
     *
     * @param r1 正向流程请求参数
     * @param r2 补偿流程请求参数
     * @return rp 正向流程响应参数
     */
    @Override
    public RP asyncTaskExecutor(R1 r1) {
        //0.检查必需组件是否初始化完成
        this.checkAllCompenetIfIsOk();
        //1.添加补偿任务到任务池中
        this.addAsyncTask2ThreadPool(r1);
        //2.执行正向业务流程
        return this.processor(r1);
    }

    /**
     * 检查组件,配置是否加载OK
     */
    private void checkAllCompenetIfIsOk() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = AsyncTaskApplicationConfig.getThreadPool();
        }
        if (executeConfig == null) {
            this.analysisAsynTaskConfig();
        }
        if (persistenceHandler == null) {
            persistenceHandler = AsyncTaskApplicationConfig.getPersistenceHandler();
        }
    }

    /**
     * 添加到任务执行队列中
     */
    public void addAsyncTask2ThreadPool(R1 r1) {
        //0.初始化任务的创建时间
        this.taskCreateTime = System.currentTimeMillis();
        //1.设置任务为处理中
        this.taskStatusInstance = TaskStatusEnum.P;
        //2.封装异步任务, 持久化到本地
        final Runnable buildCompensateRunningCommand = () -> buildCompensateRunningCommand(r1);
        this.persistence2LocalStorage(buildCompensateRunningCommand);
        //3.添加到任务池中,等待执行
        final Long compensateRateSeconds = executeConfig.compensateRateSeconds;
        scheduledFuture = threadPoolExecutor.scheduleWithFixedDelay(buildCompensateRunningCommand, compensateRateSeconds, compensateRateSeconds, TimeUnit.SECONDS);
    }

    /**
     * 任务体
     */
    private void buildCompensateRunningCommand(R1 r1) {
        //1.刷新任务状态,检查任务符合结束条件; 执行补偿任务,检查任务执行结果
        if (!this.checkAsyncTaskIfNeedContinue() || this.postCompensateProcessor(r1)) {
            this.taskStatusInstance = TaskStatusEnum.S;
            scheduledFuture.cancel(false);
            return;
        }
        //2.执行次数加1
        this.executeCount++;
    }

    /**
     * 持久化任务到本地
     */
    private void persistence2LocalStorage(final Runnable command) {
        if (persistenceHandler == null) {
            return;
        }
        persistenceHandler.persistence2LocalStorage(command, this.buildPersistenceLocalKey());
    }

    /**
     * 构建持久化关键字
     */
    private String buildPersistenceLocalKey() {
        final String localHostAddress = AdderssHelper.getHostIp();
        return executeConfig.undifineRedisPrefix + "::" + localHostAddress + "::" + this.getClass().getCanonicalName();
    }

    /**
     * 检查任务状态是否需要继续执行
     */
    private boolean checkAsyncTaskIfNeedContinue() {
        //1.检查任务状态
        if (!TaskStatusEnum.checkIfNeedContinueExecute(taskStatusInstance)) {
            return false;
        }
        //2.检查任务执行次数
        if (this.executeCount >= executeConfig.maxExecuteCount) {
            return false;
        }
        //3.检查任务是否执行超时
        return System.currentTimeMillis() - this.taskCreateTime < executeConfig.maxTimeOutSeconds * 1000;
    }

    /**
     * 解析异步定时任务
     */
    private void analysisAsynTaskConfig() {
        final ATExecutorConfig atExecutorConfig = this.getClass().getAnnotation(ATExecutorConfig.class);
        executeConfig = AsyncTaskExecuteConfig.builder()
                .compensateRateSeconds(atExecutorConfig.compensateRateSeconds())
                .maxExecuteCount(atExecutorConfig.maxExecuteCount())
                .maxTimeOutSeconds(atExecutorConfig.maxTimeOutSeconds())
                .undifineRedisPrefix(atExecutorConfig.undifineRedisPrefix())
                .build();
    }
}
