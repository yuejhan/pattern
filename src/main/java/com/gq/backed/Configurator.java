package com.gq.backed;

import com.sun.net.httpserver.Authenticator;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**Zookeeper配置实现
 * Created by user on 2016/2/26.
 */
public class Configurator {
    private static final Logger logger = LoggerFactory.getLogger(Configurator.class);

    // 建立Zookeeper的默认实现
    private static final int DEFAULT_MAX_RECONNECT_TIME = 3;
    private static final int DEFAULT_RECONNECT_INTERVAL = 1000;
    private static final String ENV_ZK_SERVER = "ZK_CLUSTER";
    private static final String ENV_ZK_POOT = "ZK_ROOT";

    private String cluster;
    private String root;

    private int maxReconnectTime = DEFAULT_MAX_RECONNECT_TIME;
    private int reconnectInterval = DEFAULT_RECONNECT_INTERVAL;

    private CuratorFramework client = null;

    public Configurator(String cluster){
        Assert.hasText(cluster,"----读取配置中心地址失败！----");
        this.cluster = cluster;
    }

    public void init(){
        String envRoot = System.getProperty(ENV_ZK_POOT);
        if(StringUtils.isEmpty(envRoot)){
            logger.info("从系统获取envRoot为空，所以使用默认值  /gq/dev");
            this.root = "/gq/dev";
        }else{
            this.root = envRoot;
        }

        String envCluster = System.getProperty(ENV_ZK_SERVER);
        if(StringUtils.isEmpty(envCluster)){
            logger.info("从系统获取envCluster为空，所以使用构造默认的值");
        }else{
            this.cluster = envCluster;
        }

        Assert.hasText(cluster,"zookeeper 中的 cluster必须设值--");

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(reconnectInterval,maxReconnectTime);
        client = CuratorFrameworkFactory.newClient(cluster, retryPolicy);
        client.start();
    }

    public void destroy(){
        logger.info("-------关闭zookeeper连接-----");
        client.close();
    }

    private String makePath(String namespace){
        return root+"/"+namespace;
    }

    private String makePath(String namespace,String key){
        return root+"/"+namespace+"/"+key;
    }

    public String readConfig(String namespace,String key){
        String path = makePath(namespace,key);
        try {
            // ---------这个是什么---------
            byte[] bytes = client.getData().forPath(path);
            return new String(bytes);
        } catch (Exception e) {
            logger.error("read config path:{} --- have ---error:{}",path,e.getMessage());
            throw new RuntimeException(path);
        }
    }
}
