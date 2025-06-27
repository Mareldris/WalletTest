package org.example.wallet.configuration;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mazunin-sv
 * @version 27.06.2025 7:14
 */
@Slf4j
@Setter(AccessLevel.PACKAGE)
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "wallet")
public class WalletConfiguration {

    private Integer maxRetry;

    @Bean
    public Integer minAge() {
        if (maxRetry == null) {
            log.warn("WalletConfiguration - Не задано количество повторных попыток для обработки запроса. По умолчанию установлено 5");
            return 5;
        }
        return maxRetry;
    }
}
