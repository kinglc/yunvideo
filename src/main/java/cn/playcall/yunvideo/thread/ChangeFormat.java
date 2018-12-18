package cn.playcall.yunvideo.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangeFormat implements Runnable {

    private String sourcePath;
    private String goalPath;

    public ChangeFormat(String sourcePath, String goalPath) {
        this.sourcePath = sourcePath;
        this.goalPath = goalPath;
    }

    @Override
    public void run() {
        System.out.println(sourcePath);
        System.out.println(goalPath);
        String command = "ffmpeg -i "+sourcePath+" "+goalPath;
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
