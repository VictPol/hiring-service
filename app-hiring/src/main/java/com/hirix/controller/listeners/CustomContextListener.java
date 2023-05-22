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

    private final IndustryRepository industryRepository;
    private final LocationRepository locationRepository;
    private final PositionRepository positionRepository;
    private final ProfessionRepository professionRepository;
    private final RankRepository rankRepository;
    private final SpecializationRepository specializationRepository;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        industryRepository.findAllByVisibleIs(true);
        locationRepository.findAllByVisibleIs(true);
        positionRepository.findAllByVisibleIs(true);
        professionRepository.findAllByVisibleIs(true);
        rankRepository.findAllByVisibleIs(true);
        specializationRepository.findAllByVisibleIs(true);
    }
}
