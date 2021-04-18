package com.xiajun.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FFMpeg {
    public static void convertor(String videoInputPath, String videoOutputPath) throws IOException {
        //ffmpeg -i input.mp4 output.avi
        ArrayList<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutputPath);
        excuteCommand(command);
    }

    public static void mergeVideoAndBgm(String videoInputPath,String bgmInputPath,Double seconds,String videoOutputPath) throws IOException {
        //ffmpeg -i input.mp4 -i bgm.mp3 -t 7 -y new.mp4
        ArrayList<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(bgmInputPath);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-t");
        command.add(seconds.toString());
        command.add("-y");
        command.add(videoOutputPath);
        excuteCommand(command);
    }

    public static void getGIFCover(String videoInputPath, String coverOutputPath) throws IOException {
//        ffmpeg -ss 00:00:01 -t 2 -y -i new.mp4 new.gif
        ArrayList<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-ss");
        command.add("00:00:01");
        command.add("-t");
        command.add("2");
        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);
        command.add(coverOutputPath);
        excuteCommand(command);
    }

    public static void getImgCover(String videoInputPath, String coverOutputPath) throws IOException {
        ArrayList<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(videoInputPath);
        command.add("-ss");
        command.add("1");
        command.add("-f");
        command.add("image2");
        command.add(coverOutputPath);
        excuteCommand(command);
    }

    public static void main(String[] args) {
        try {
            convertor("/Users/xiajun/Desktop/1548333491664623.mp4", "/Users/xiajun/Desktop/out1.avi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void excuteCommand(ArrayList<String> command) throws IOException {
        //打印命令
        System.out.print("将执行：");
        for (String s : command) {
            System.out.print(s + " ");
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        //执行
        Process process = processBuilder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = reader.readLine()) != null) {

        }
        //关闭流
        if (reader != null) {
            reader.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
        System.out.println("执行完毕");
    }
}
