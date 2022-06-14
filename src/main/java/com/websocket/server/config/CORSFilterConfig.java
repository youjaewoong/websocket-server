package com.websocket.server.config;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSFilterConfig {

  @Bean
  public FilterRegistrationBean<?> corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.setAllowedMethods(new ArrayList<String>(Arrays.asList("PUT", "POST", "GET", "DELETE", "OPTION")));
    config.setAllowedHeaders(new ArrayList<String>(
        Arrays.asList("Content-Type", "Accept", "Authorization", "x-http-method-override",
            "protocol_version", "protocol_type", "protocol_id", "x-requested-with",
            "Access-Control-Request-Method", "Access-Control-Request-Headers")));
    config.setMaxAge((long) 3600);
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(-100);
    return bean;
  }

}