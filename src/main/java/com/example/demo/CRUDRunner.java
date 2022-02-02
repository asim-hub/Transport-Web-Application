package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootApplication
public class CRUDRunner {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = CRUDRunner.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        String decoded = URLDecoder.decode(file.getAbsolutePath(), StandardCharsets.UTF_8);
        System.out.println(decoded);
        FileInputStream serviceAccount = new FileInputStream(decoded);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://test-isi-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);



        SpringApplication.run(CRUDRunner.class, args);
    }
}
