package com.group2.KoiFarmShop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


    @Value("${credentialsFilePath}")
    private String credentialsFilePath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Sử dụng ClassPathResource để nạp tệp từ classpath

        ClassPathResource resource = new ClassPathResource(credentialsFilePath);
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setStorageBucket("koi-shop-3290e.appspot.com")
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();


        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();  // Trả về FirebaseApp đã được khởi tạo
        }


    }
}


