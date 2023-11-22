package com.fastwon;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

public class FirebaseInitializer {

	public void initialize() {

		try {

			FileInputStream fis = new FileInputStream("/var/task/fastwonboard-firebase-adminsdk-5at8g-590056fa54.json");
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.
					fromStream(fis)).build();
			if(FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
