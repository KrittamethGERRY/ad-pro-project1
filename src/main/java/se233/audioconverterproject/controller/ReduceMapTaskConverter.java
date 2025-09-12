package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import se233.audioconverterproject.Launcher;

import java.io.IOException;
import java.util.concurrent.Callable;

public class ReduceMapTaskConverter {

    public ReduceMapTaskConverter(String format, int quality, int bitrate, int channel, String filePath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(Launcher.class.getResource("/bin/ffmpeg.exe").toString());
        FFprobe ffprobe = new FFprobe(Launcher.class.getResource("/bin/ffprobe.exe").toString());

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(Launcher.class.getResource("Part 1.m4a").toString())
                .setFormat(format)
                .addOutput("output." + format)
                .setAudioBitRate(bitrate)
                .setAudioChannels(channel)
                .setAudioQuality(quality).done();

        new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder).run();
    }
}
