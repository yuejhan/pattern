package com.gq.backed;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by user on 2016/2/26.
 */
public class WebAppInstaller implements WebApplicationInitializer{
    private static final Logger log = LoggerFactory.getLogger(WebAppInstaller.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        String serverInfo = servletContext.getServerInfo();
        log.info("容器信息"+serverInfo);
        // 指定发布容器的访问端口
        if(serverInfo.toLowerCase().contains("tomcat")){
            String tomcatPort = System.getProperty("tomcatPort");
            if(StringUtils.isNoneBlank(tomcatPort)){
                ServiceRegister.setPort(tomcatPort);
            }else{
                ServiceRegister.setPort("8080");
                log.warn("--=-----获取服务端口失败！-----=--");
            }
        }else if(serverInfo.toLowerCase().contains("jetty")){
            ServiceRegister.setPort("9010");
        }else{
            ServiceRegister.setPort("80");
        }
        // 获取本地主机的IP host
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()){
                NetworkInterface nt = e.nextElement();
                Enumeration<InetAddress> eIps = nt.getInetAddresses();
                while(eIps.hasMoreElements()){
                    InetAddress ip = eIps.nextElement();
                    if(ip instanceof Inet4Address){
                        String ip4 = ip.getHostAddress();
                        if(0 == ip4.indexOf("10.")){
                            String hostName = ip.getHostName();
                            log.info("---ip---:"+ip4);
                            log.info("---host---"+hostName);
                            ServiceRegister.setIp(ip4);
                            ServiceRegister.setHost(hostName);
                        }
                    }
                }
            }

        } catch (SocketException e) {
            log.error("------获取网卡信息失败！----");
        }

        // ---------------------spring 的上下文------------------
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(SpringConfig.class);
        // 给上下文添加监听器
        servletContext.addListener(new ContextLoaderListener(springContext));

        // ----------------------MVC 的上下文-----------------
        AnnotationConfigWebApplicationContext mvcContext =  new AnnotationConfigWebApplicationContext();
        mvcContext.register(MVCConfig.class);
        // ---------------------DispatcherService mvc的前端控制器--------------------
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("GQ-MVC-Servlet",dispatcherServlet);
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping("/");
        dynamic.setMultipartConfig(new MultipartConfigElement(""));

    }
}
