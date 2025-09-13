package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import se233.audioconverterproject.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

public class ReduceMapTaskConverter implements Callable<String> {

    private static FFmpeg ffmpeg;
    private static FFprobe ffprobe;
    private String format;
    private int quality;
    private int samplerate;
    private int bitrate;
    private int channel;
    private String filePath;

    public ReduceMapTaskConverter(String format, int quality, int samplerate, Integer bitrate, int channel, String filePath) {
        this.format = format;
        this.quality = quality;
        this.samplerate = samplerate;
        this.bitrate = bitrate;
        this.channel = channel;
        this.filePath = filePath;
    }

    public String call() throws URISyntaxException, IOException {
        System.out.println("Starting Reduce Map Task Converter");
        File ffmpegFile = new File(Launcher.class.getResource("ffmpeg/bin/ffmpeg.exe").toURI());
        File ffprobeFile = new File(Launcher.class.getResource("ffmpeg/bin/ffprobe.exe").toURI());

        ffmpeg = new FFmpeg(ffmpegFile.toString());
        ffprobe = new FFprobe(ffprobeFile.toString());

        File inputFile = new File(Launcher.class.getResource("sample-3s.mp3").toURI());
        System.out.println("Input file: " + inputFile.exists());

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .addOutput("D:/output.m4a")
                .setFormat(format)
                .setAudioSampleRate(samplerate)
                .setAudioQuality(quality)
                .setAudioChannels(channel)
                .done()
                .overrideOutputFiles(true);


        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        return "Audio created at " ;
    }
}
