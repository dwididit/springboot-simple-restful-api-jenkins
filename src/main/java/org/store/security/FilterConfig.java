package org.store.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyRequestFilter> apiKeyFilter(){
        FilterRegistrationBean<ApiKeyRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiKeyRequestFilter());
        registrationBean.addUrlPatterns("/api/*"); // Adjust the URL pattern based on your API paths
        return registrationBean;
    }
}
