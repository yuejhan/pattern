package com.gq.backed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2016/2/26.
 */
public class ServiceRegister {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegister.class);
    private static volatile String port;
    private static volatile String ip;
    private static volatile String host;

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        ServiceRegister.port = port;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ServiceRegister.ip = ip;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ServiceRegister.host = host;
    }

    private void register(boolean register){

    }
}
