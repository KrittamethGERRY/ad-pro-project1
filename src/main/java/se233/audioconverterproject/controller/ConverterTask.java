package se233.audioconverterproject.controller;

import com.google.common.util.concurrent.Service;
import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import se233.audioconverterproject.Launcher;
import se233.audioconverterproject.model.AudioPresets;

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

        FFmpegBuilder builder = null;

        builder = switch(format) {
            case "mp3"-> AudioPresets.convertToMP3(quality, bitrate, sampleRate, channel, inputPath, outputDir);
            case "wav"-> AudioPresets.convertToWAV(quality, sampleRate, channel, inputPath, outputDir);
            case "flac"-> AudioPresets.convertToFLAC(sampleRate, channel, inputPath, outputDir);
            case "m4a"-> AudioPresets.convertToM4A(quality, bitrate, sampleRate, channel, inputPath, outputDir);
            default -> throw new IllegalStateException("Invalid format: " + format);
        };

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegJob job = executor.createJob(builder.overrideOutputFiles(true), progress -> {
        });
        job.run();

        System.out.println("AUDIO CONVERTED!");
        return "Audio created at ";
        }
    }
