package se233.audioconverterproject.controller;

import com.google.common.util.concurrent.Service;
import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import se233.audioconverterproject.Launcher;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

public class ConverterTask extends Task implements Callable<String> {

    private static FFmpeg ffmpeg;
    private static FFprobe ffprobe;
    private String format;
    private int quality;
    private int bitrate;
    private int sampleRate;
    private int channel;
    private String filePath;

    public ConverterTask(String format, int quality, int bitrate, int sampleRate, int channel, String filePath) {
        this.format = format;
        this.quality = quality;
        this.bitrate = bitrate;
        this.sampleRate = sampleRate;
        this.channel = channel;
        this.filePath = filePath;
    }

    public String call() throws URISyntaxException, IOException, InterruptedException {
        Thread.sleep(5000);
        File ffmpegFile = new File(Launcher.class.getResource("ffmpeg/bin/ffmpeg.exe").toURI());
        File ffprobeFile = new File(Launcher.class.getResource("ffmpeg/bin/ffprobe.exe").toURI());

        ffmpeg = new FFmpeg(ffmpegFile.toString());
        ffprobe = new FFprobe(ffprobeFile.toString());

        File inputFile = new File(Launcher.class.getResource("sample-3s.mp3").toURI());
        System.out.println("Input file: " + inputFile.exists());

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .addOutput(filePath)
                .setFormat(format)
                .setAudioSampleRate(sampleRate)
                .setAudioQuality(quality)
                .setAudioChannels(channel)
                .setAudioBitRate(bitrate)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        System.out.println("AUDIO CONVERTED!");
        return "Audio created at " ;
    }
}
