package org.eugene.cost.config;

import org.eugene.cost.cache.PaymentCache;
import org.eugene.cost.cache.SessionCache;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.Session;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.service.impl.BuyServiceImpl;
import org.eugene.cost.service.impl.DayServiceImpl;
import org.eugene.cost.service.impl.OperationServiceImpl;
import org.eugene.cost.service.impl.PaymentServiceImpl;
import org.eugene.cost.service.impl.SessionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CacheSpringConfiguration.class})
public class ServiceSpringConfiguration {
    @Bean
    public IBuyService buyService(){
        return new BuyServiceImpl();
    }

    @Bean
    public IDayService dayService(){
        return new DayServiceImpl();
    }

    @Bean
    public IPaymentService paymentService(PaymentCache paymentCache, FileManager<Payment> fileManager){
        return new PaymentServiceImpl(paymentCache, fileManager);
    }

    @Bean
    public IOperationService operationService(IPaymentService paymentService){
        return new OperationServiceImpl(paymentService);
    }

    @Bean
    public ISessionService sessionService(SessionCache sessionCache, IDayService dayService,
                                          IBuyService buyService, FileManager<Session> fileManager){

        return new SessionServiceImpl(sessionCache, dayService, buyService, fileManager);
    }
}
