package com.hirix.controller.listeners;

import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
@AllArgsConstructor
public class CustomContextListener implements ServletContextListener {

    private final UserRepository userRepository;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        //Add all auxiliary tables from DB but not users and make them cacheable
        userRepository.findAll();
    }
}
