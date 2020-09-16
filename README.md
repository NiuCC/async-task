# Async-Task 异步任务框架(暂停接入)

### 是做什么的？
异步任务框架，是一个开源的Java平台，它为更简单的开发Java程序提供了支持。

异步任务,是为了解决异步请求下无法实时拿到请求结果的问题,减少二次补偿逻辑的开发对定时任务的依赖,以达到"异步请求变同步"的效果。

### 项目架构图
<img src="./async-task.png" width="50%">
### 支持环境

要求JDK1.8及以上版本

### Maven

异步任务框架已经部署到公司Maven仓库
Latest Snapshot Version:
```maven
<dependency>
    <groupId>com.xxx</groupId>
    <artifactId>async-task</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
Latest Snapshot Version:
    暂未发布稳定版本, 请期待更新

### 快速开始
1.以SpringBoot为例,配置过程如下:
将AsyncTaskConfig加入配置对象,并设置核心线程池数量以及配置自定义持久化实现类所在的包路径
```java
@Configuration
public class AsyncTaskConfig {
    @Bean(name = "asyncTaskApplicationConfig")
    public AsyncTaskApplicationConfig asyncTaskApplicationConfig() {
        AsyncTaskApplicationConfig asyncTaskApplicationConfig = new AsyncTaskApplicationConfig();
        //自定义配置核心线程池数量
        asyncTaskApplicationConfig.setCorePoolSize(150);
        //配置自定义持久化实现类所在的包路径
        asyncTaskApplicationConfig.setPersistenceHandlerPackagePath("com.baijia.uqun.sales.task.config.*");
        return asyncTaskApplicationConfig;
    }
}
```
实现类实例如下:
```java
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
```
2.非Spring环境下使用方式
(1) 初始化全局配置
(2) 测试
```java
public class TestDemoAsyncTaskHandler {
    public static void main(String[] args) {
        //0.任务全局配置
        new AsyncTaskApplicationConfig();
        //1.实例化Demo,测试一下任务执行情况
        AsyncHandler.handle(DemoAsyncTaskHandler.class, 1);
    }
}
```

### 发布事件

### 未来规划

### 贡献者

* RD 刘成成 

* QA 刘振兴
