package com.gq.backed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gq.backed.entity.DatabaseInstance;
import com.gq.backed.entity.TomcatPoolConfig;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/2/26.
 */
public class GqDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(GqDataSource.class);
    private Map<Object,Object> targetDataSource = new HashMap<Object,Object>();
    /*初始化GqDataSource*/
    public GqDataSource(Configurator configurator,String databaseNames){
        Assert.notNull(configurator,"-----------配置文件不能为空！------");
        Assert.hasLength(databaseNames,"---------名称必须被声明-------");

        String[] dbNames = databaseNames.split(",");
        String defaultDB = null;
        for (String dbName:dbNames) {
            // 获取数据库实例  ？？
            String databaseInstance = configurator.readConfig("db", dbName);
            // 获取数据库的配置
            String poolConfig = configurator.readConfig("db",dbName+"--poolConfig");
            // 创建数据库实例，并将实例放入到map集合中
            targetDataSource.put(dbName,createDataSource(databaseInstance,poolConfig));
            logger.info("Create database(){}:{}",dbName,databaseInstance);
            if(dbName.contains("--default")){
                defaultDB = dbName;
            }
        }
        if(null == defaultDB && null != dbNames[0]){
            defaultDB = dbNames[0];
            logger.info("Usering {} as default database",defaultDB);
            setDefaultTargetDataSource(targetDataSource.get(defaultDB));
        }

        setTargetDataSources(targetDataSource);
    }

    private DataSource createDataSource(String instance, String poolConfig){
        try {
            ObjectMapper mapper = new ObjectMapper();
            DatabaseInstance bi = mapper.readValue(instance, DatabaseInstance.class);
            TomcatPoolConfig tpc;
            if (null == poolConfig){
                tpc = new TomcatPoolConfig();
            }else {
                tpc = mapper.readValue(poolConfig, TomcatPoolConfig.class);
            }
            PoolProperties pp = new PoolProperties();
            pp.setDriverClassName(bi.getDriverClassName());
            pp.setUrl(bi.getUrl());
            pp.setUsername(bi.getUsername());
            pp.setPassword(bi.getPassword());
            pp.setConnectionProperties(bi.getConnectionProperties());

            pp.setInitialSize(tpc.getInitialSize());
            pp.setMinIdle(tpc.getMinIdle());
            pp.setMaxActive(tpc.getMaxActive());
            pp.setMaxWait(tpc.getMaxWait());
            pp.setRemoveAbandoned(tpc.isRemoveAbandoned());
            pp.setRemoveAbandonedTimeout(tpc.getRemoveAbandonedTimeout());
            pp.setMinEvictableIdleTimeMillis(tpc.getMinEvictableIdleTimeMillis());
            pp.setValidationQuery(tpc.getValidationQuery());
            pp.setJmxEnabled(tpc.isJmxEnabled());
            pp.setTestWhileIdle(tpc.isTestWhileIdle());
            pp.setTestOnBorrow(tpc.isTestOnBorrow());
            pp.setTestOnReturn(tpc.isTestOnReturn());
            pp.setLogAbandoned(tpc.isLogAbandoned());
            pp.setValidationInterval(tpc.getValidationInterval());
            pp.setTimeBetweenEvictionRunsMillis(tpc.getTimeBetweenEvictionRunsMillis());
            pp.setJdbcInterceptors(tpc.getJdbcInterceptors());


            return new org.apache.tomcat.jdbc.pool.DataSource(pp);
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}
