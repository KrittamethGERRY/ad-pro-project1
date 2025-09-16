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
    private String inputPath;
    private String outputDir;

    public ConverterTask(String format, int quality, int bitrate, int sampleRate, int channel, String inputPath, String outputDir) {
        this.format = format;
        this.quality = quality;
        this.bitrate = bitrate;
        this.sampleRate = sampleRate;
        this.channel = channel;
        this.inputPath = inputPath;
        this.outputDir = outputDir;
    }

    public String call() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Starting ConverterTask");
        File ffmpegFile = new File(Launcher.class.getResource("ffmpeg/bin/ffmpeg.exe").toURI());
        File ffprobeFile = new File(Launcher.class.getResource("ffmpeg/bin/ffprobe.exe").toURI());

        ffmpeg = new FFmpeg(ffmpegFile.toString());
        ffprobe = new FFprobe(ffprobeFile.toString());

        File inputFile = new File(inputPath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .addOutput(outputDir + inputFile.getName() + "." + format)
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
