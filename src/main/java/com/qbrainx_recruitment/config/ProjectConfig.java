package com.qbrainx_recruitment.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
public class ProjectConfig {

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.setMappingFiles(new ArrayList<>(Collections.singleton("dozerJdk8Converters.xml")));
        return dozerBeanMapper;
    }
}
