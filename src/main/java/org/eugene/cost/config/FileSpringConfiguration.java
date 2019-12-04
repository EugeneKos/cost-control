package org.eugene.cost.config;

import org.eugene.cost.data.Payment;
import org.eugene.cost.data.Session;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.file.encryption.EncryptionService;
import org.eugene.cost.file.encryption.impl.ByteEncryptionService;
import org.eugene.cost.file.impl.BasicFileManager;
import org.eugene.cost.file.impl.JSONFileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSpringConfiguration {
    @Bean
    public  EncryptionService<byte[]> encryptionService(){
        return new ByteEncryptionService();
    }

    @Bean
    public FileManager<Session> sessionFileManager(EncryptionService<byte[]> encryptionService){
        return new BasicFileManager<>(encryptionService);
    }

    @Bean
    public FileManager<Payment> paymentFileManager(EncryptionService<byte[]> encryptionService){
        return new BasicFileManager<>(encryptionService);
    }

    @Bean
    public <T> FileManager<T> jsonFileManager(){
        return new JSONFileManager<>();
    }
}
