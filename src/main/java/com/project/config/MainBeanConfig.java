package com.project.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = { "com.project" })
@PropertySource("classpath:encrypt.properties")
public class MainBeanConfig {
}
