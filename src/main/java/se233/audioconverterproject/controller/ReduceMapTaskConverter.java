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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class ReduceMapTaskConverter {

    private static FFmpeg ffmpeg;
    private static FFprobe ffprobe;
    private String format;
    private int quality;
    private int bitrate;
    private int channel;
    private String filePath;

    public ReduceMapTaskConverter(String format, int quality, int bitrate, int channel, String filePath) {
        this.format = format;
        this.quality = quality;
        this.bitrate = bitrate;
        this.channel = channel;
        this.filePath = filePath;
    }

    public String call() throws URISyntaxException, IOException {
        File ffmpegFile = new File(Launcher.class.getResource("ffmpeg/bin/ffmpeg.exe").toURI());
        File ffprobeFile = new File(Launcher.class.getResource("ffmpeg/bin/ffprobe.exe").toURI());

        ffmpeg = new FFmpeg(ffmpegFile.toString());
        ffprobe = new FFprobe(ffprobeFile.toString());

        File inputFile = new File(Launcher.class.getResource("sample-3s.mp3").toURI());
        System.out.println("Input file: " + inputFile.exists());

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .addOutput("output.ogg")
                .setFormat(format)
                .setAudioSampleRate(44100)
                .setAudioCodec("libvorbis")
                .setAudioQuality(quality)
                .setAudioChannels(2)
                .setAudioBitRate(128_000)
                .done();


        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        return "Audio created at " ;
    }
}
