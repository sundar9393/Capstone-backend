package com.upgrad.FoodOrderingApp.api.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.upgrad.FoodOrderingApp.api.controller")
@ServletComponentScan("com.upgrad.FoodOrderingApp.api.filters")
public class WebConfiguration {
}
