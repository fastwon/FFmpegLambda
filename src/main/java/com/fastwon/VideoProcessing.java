package com.fastwon;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
//import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class VideoProcessing {
    private FirebaseInitializer firebaseInitializer;

    public VideoProcessing() {
        firebaseInitializer = new FirebaseInitializer();
        firebaseInitializer.initialize();
    }

    private String firebaseBucket;

    public String handleRequest(VideoProcessingInput input) {

        String ans = "실패";

        String inputPath = input.getInputPath();
        String outputPath = input.getOutputPath();
        double start = input.getStart();
        double duration = input.getDuration();


        try {
            ans = search("/var/task", "fastwonboard-firebase-adminsdk-5at8g-590056fa54.json");

//            String ffmpegCheck = checkFfmpeg("/opt/bin/ffmpeg");
//            ans = ffmpegCheck;



            // 현재 디렉토리 경로 얻기
//            String currentDirectory = System.getProperty("user.dir");


            // FFmpeg 명령 생성 및 실행
            // 클라우드 리눅스용
            String command = "/opt/bin/ffmpeg -i " + inputPath + " -ss " + start + " -t " + duration + " " + outputPath;
            // 윈도우 실행파일용
//            String command = currentDirectory + "/src/main/resources/ffmpeg/bin/ffmpeg.exe -i " + inputPath + " -ss " + start + " -t " + duration + " " + outputPath;

            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            // 프로세스의 출력 로그 확인 (선택적)

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
//				System.out.println(line);
            }


            process.waitFor(); // 프로세스 완료 대기

            firebaseBucket = System.getenv("APP_FIREBASE_BUCKET");

            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

            String[] nameFileArr = inputPath.split("/");
            String nameFile = nameFileArr[nameFileArr.length-1];

            // 3. 새 파일을 Firebase Storage에 업로드하기
            FileInputStream contentStream = new FileInputStream(outputPath);
            bucket.create(nameFile.toString(), contentStream , "video/mp4");
            contentStream.close();

//            ans = "성공";

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            ans += " 에러: " + e.toString();
        }

        return ans;
    }

    // 특정 디렉토리 아래의 특정 파일을 찾는 메서드
    public static String search(String path, String target) {
        File dir = new File(path);
        File[] list = dir.listFiles();

        String ans = "없다 아무것도";

        if (list != null) {
            for (File file : list) {
                if (file.isDirectory()) {
                    String result = search(file.getAbsolutePath(), target);
                    if (!result.equals("없다 아무것도")) {
                        return result;  // 하위 디렉토리에서 ffmpeg를 찾았다면 그 결과를 반환합니다.
                    }
                } else {
                    if (file.getName().equals(target)) {
                        return "ffmpeg 파일 위치: " + file.getAbsolutePath();  // ffmpeg 파일을 찾았다면 그 위치를 반환합니다.
                    }
                }
            }
        }

        return ans;  // ffmpeg 파일을 찾지 못했다면 "없다 아무것도"를 반환합니다.
    }

    public static String checkFfmpeg(String ffmpegPath) {
        String result = "잘못된 파일";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath, "-version");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "";
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            result = "ffmpeg 실행 중 에러 발생: " + e.toString();
        }

        return result;
    }
}