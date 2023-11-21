package com.fastwon.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;

public class FirebaseInitializer {

//	@Value("${app.firebase-configuration-file}")
	private String firebaseConfigPath;

	public void initialize() {
//		firebaseConfigPath = System.getenv("app.firebase-configuration-file");
		firebaseConfigPath = System.getenv("APP_FIREBASE_CONFIGURATION_FILE");



		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.
					fromStream(new ByteArrayInputStream(firebaseConfigPath.getBytes()))).build();
			if(FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
