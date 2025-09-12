package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import se233.audioconverterproject.Launcher;

import java.io.IOException;

public class ReduceMapTaskConverter extends Task<Void>  {
    @Override
    public Void call() throws IOException {
        FFmpeg ffmpeg = new FFmpeg(Launcher.class.getResource("/ffmfiles/ffmpeg.exe").toString());
        FFprobe ffprobe = new FFprobe(Launcher.class.getResource("/ffmfiles/ffmprobe.exe").toString());

        return null;
    }
}
