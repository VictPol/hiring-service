package com.hirix.controller.listeners;

import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SpecializationRepository;
import com.hirix.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
@AllArgsConstructor
public class CustomContextListener implements ServletContextListener {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
//    private final IndustryRepository industryRepository;
//    private final PositionRepository positionRepository;
//    private final ProfessionRepository professionRepository;
//    private final RankRepository rankRepository;
//    private final SpecializationRepository specializationRepository;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        locationRepository.findAll();
        userRepository.findAll();
//        industryRepository.findAll();
//        positionRepository.findAll();
//        professionRepository.findAll();
//        rankRepository.findAll();
//        specializationRepository.findAll();
        //here logic of DB start, caches start, etc.
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
