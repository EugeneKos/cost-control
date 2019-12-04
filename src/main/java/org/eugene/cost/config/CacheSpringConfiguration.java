package org.eugene.cost.config;

import org.eugene.cost.cache.PaymentCache;
import org.eugene.cost.cache.SessionCache;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.Session;
import org.eugene.cost.file.FileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FileSpringConfiguration.class})
public class CacheSpringConfiguration {
    @Bean
    public SessionCache sessionCache(FileManager<Session> fileManager){
        return new SessionCache(fileManager);
    }

    @Bean
    public PaymentCache paymentCache(FileManager<Payment> fileManager){
        return new PaymentCache(fileManager);
    }
}
