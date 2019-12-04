package org.eugene.cost.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class SpringContext {
    private static ApplicationContext context = new AnnotationConfigApplicationContext(ServiceSpringConfiguration.class);

    private SpringContext(){}

    public static <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }
}
