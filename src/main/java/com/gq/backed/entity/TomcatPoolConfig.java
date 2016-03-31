package com.gq.backed.entity;

/**
 * 连接池配置
 * Created by ZhangLiang on 2015/12/4.
 */
public class TomcatPoolConfig {

    //初始化连接: 连接池启动时创建的初始化连接数量,1.2版本后支持
    private int initialSize = 5;

    //最小空闲连接: 连接池中容许保持空闲状态的最小连接数量, 低于这个数量将创建新的连接, 如果设置为0 则不创建
    //如果连接验证失败将缩小这个值（参考testWhileIdle）
    private int minIdle = 5;

    //最大活动连接: 连接池在同一时间能够分配的最大活动连接的数量, 如果设置为非正数则表示不限制
    private int maxActive = 300;

    //最大等待时间: 当没有可用连接时, 连接池等待连接被归还的最大时间(以毫秒计数), 超过时间则抛出异常,
    //如果设置为-1 表示无限等待
    private int maxWait = 10000;

    //标记是否删除泄露的连接, 如果他们超过了removeAbandonedTimeout 的限制.
    //如果设置为true, 连接被认为是被泄露并且可以被删除, 如果空闲时间超过removeAbandonedTimeout.
    //设置为true 可以为写法糟糕的没有关闭连接的程序修复数据库连接.
    private boolean removeAbandoned = true;

    //泄露的连接可以被删除的超时值, 单位秒这个值应该设为应用中查询执行最长的时间
    private int removeAbandonedTimeout = 60;

    //连接在池中保持空闲而不被空闲连接回收器线程(如果有) 回收的最小时间值，单位毫秒
    private int minEvictableIdleTimeMillis = 30000;

    private String validationQuery = "SELECT 1";//SELECT 1 FROM DUAL
    private boolean jmxEnabled = true;
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = true;
    private boolean testOnReturn;

    //标记当Statement 或连接被泄露时是否打印程序的stack traces 日志。
    //被泄露的Statements 和连接的日志添加在每个连接打开或者生成新的Statement, 因为需要生成stack trace
    private boolean logAbandoned = true;

    //避免过度验证，保证验证不超过这个频率——以毫秒为单位。如果一个连接应该被验证，但上次验证未达到指定间隔，将不再次验证
    private long validationInterval = 30000;

    //在空闲连接回收器线程运行期间休眠的时间值,以毫秒为单位. 如果设置为非正数,则不运行空闲连接回收器线程;
    //每timeBetweenEvictionRunsMillis毫秒秒检查一次连接池中空闲的连接,
    //把空闲时间超过minEvictableIdleTimeMillis毫秒的连接断开,直到连接池中的连接数到minIdle为止.
    private int timeBetweenEvictionRunsMillis = 30000;

    /*
      jdbc拦截器——jdbc-pool的高级扩展属性,用分号分隔的、继承org.apache.tomcat.jdbc.pool.JdbcInterceptor的类名列表。
      这些拦截器将被插入到对 java.sql.Connection操作之前的拦截器链上。
      预制的拦截器有：
      org.apache.tomcat.jdbc.pool.interceptor.ConnectionState
      - 追踪自动提交、只读状态、catalog和事务隔离等级等状态。（keeps track of auto commit,
      read only, catalog and transaction isolation level.）
      org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer
      - 追踪打开的statement，当连接被归还时关闭它们。（keeps track of opened statements,
      and closes them when the connection is returned to the pool.）
    */
    private String jdbcInterceptors = "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer";

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public long getValidationInterval() {
        return validationInterval;
    }

    public void setValidationInterval(long validationInterval) {
        this.validationInterval = validationInterval;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getJdbcInterceptors() {
        return jdbcInterceptors;
    }

    public void setJdbcInterceptors(String jdbcInterceptors) {
        this.jdbcInterceptors = jdbcInterceptors;
    }

}
