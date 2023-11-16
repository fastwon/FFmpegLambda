package com.fastwon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VideoProcessing {
    public static String handleRequest(VideoProcessingInput input) {
        String ans = "실패";

        String inputPath = input.getInputPath();
        String outputPath = input.getOutputPath();
        double start = input.getStart();
        double duration = input.getDuration();


        try {
            // 현재 디렉토리 경로 얻기
//            String currentDirectory = System.getProperty("user.dir");

            ans = "ㅅ1ㅅ";

            // FFmpeg 명령 생성 및 실행
            // 클라우드 리눅스용
            String command = "/opt/bin/ffmpeg -i " + inputPath + " -ss " + start + " -t " + duration + " " + outputPath;
            // 윈도우 실행파일용
//            String command = currentDirectory + "/src/main/resources/ffmpeg/bin/ffmpeg.exe -i " + inputPath + " -ss " + start + " -t " + duration + " " + outputPath;

            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            ans = "ㅅ2ㅅ";
            processBuilder.redirectErrorStream(true);

            ans ="ㅅ3ㅅ";
            Process process = processBuilder.start();

            // 프로세스의 출력 로그 확인 (선택적)

            ans ="ㅅ4ㅅ";
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
//				System.out.println(line);
            }

            ans ="ㅅ5ㅅ";

            process.waitFor(); // 프로세스 완료 대기

            ans = "성공";

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            ans += " 에러: " + e.toString();
        }

        return ans;
    }
}