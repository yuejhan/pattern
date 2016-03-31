package com.gq.backed;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 初始化spring的上下文配置
 * 主要工作是：加载了两个配置文件，使用注解初始化所需要的bena对象
 * Created by user on 2016/2/26.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)
@EnableTransactionManagement(proxyTargetClass = false)
@ComponentScan(basePackages = {"com.gq"},excludeFilters={@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)})
@PropertySource("classpath:/service.properties")
@ImportResource("classpath:/spring-common.xml")
public class SpringConfig {
    /*生成zookeeper*/
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Configurator createrConfigurator(Environment env){
        return new Configurator(env.getProperty("config.service"));
    }
    /*生成数据源的bean*/
    @Bean
    public DataSource createDataSource(Configurator configurator){
        return new GqDataSource(configurator,"marketing");
    }
}
