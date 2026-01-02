package com.evoauth.spring;

import com.evoauth.core.AuthClient;
import com.evoauth.core.AuthConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(AuthClient.class)
@EnableConfigurationProperties(EvoAuthProperties.class)
public class EvoAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthConfig authConfig(EvoAuthProperties properties) {
        return new AuthConfig.Builder()
                .baseUrl(properties.getBaseUrl())
                .apiKey(properties.getApiKey())
                .connectionTimeoutMs(properties.getConnectionTimeoutMs())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthClient authClient(AuthConfig config) {
        return new AuthClient(config);
    }
}
