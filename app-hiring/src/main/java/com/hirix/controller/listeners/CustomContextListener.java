package com.hirix.controller.listeners;

import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
@AllArgsConstructor
public class CustomContextListener implements ServletContextListener {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

//    @Cacheable
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        locationRepository.findAll();
        userRepository.findAll();
    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        ServletContextListener.super.contextDestroyed(sce);
//        //close connection with all external services
//    }
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("Context Is UP");
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        System.out.println("Context Is DOWN");
//    }
}
