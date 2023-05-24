package com.hirix;

import com.hirix.configuration.ApplicationCacheConfiguration;
import com.hirix.configuration.ApplicationConfig;
import com.hirix.configuration.HibernateConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.hirix")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
@EnableTransactionManagement
@EnableCaching
@Import(
        { ApplicationConfig.class,
            HibernateConfiguration.class,
                ApplicationCacheConfiguration.class,
                    com.hirix.configuration.SwaggerConfig.class}
        )

public class SpringBootApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationStarter.class, args);
    }
}
