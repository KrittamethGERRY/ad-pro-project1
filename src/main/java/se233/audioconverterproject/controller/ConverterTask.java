package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import se233.audioconverterproject.Launcher;
import se233.audioconverterproject.model.AudioPresets;
import se233.audioconverterproject.model.exception.ConversionFailedException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;


public class ConverterTask extends Task<String> implements Callable<String> {

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;
    private final String format;
    private final int quality;
    private final int bitrate;
    private final int sampleRate;
    private final int channel;
    private final String inputPath;
    private final String outputDir;

    public ConverterTask(String format, int quality, int bitrate, int sampleRate, int channel, String inputPath) {
        this.format = format;
        this.quality = quality;
        this.bitrate = bitrate;
        this.sampleRate = sampleRate;
        this.channel = channel;
        this.inputPath = inputPath;
        this.outputDir = System.getProperty("java.io.tmpdir");
    }

    public String call() throws ConversionFailedException, URISyntaxException, IOException {
        System.out.println("Starting ConverterTask");
        File inputFile = new File(inputPath);
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
        FFmpegJob job = executor.createJob(builder.overrideOutputFiles(true));
        job.run();

        System.out.println("AUDIO CONVERTED!");
        return outputDir + inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")) + "." + format;
    }
}


