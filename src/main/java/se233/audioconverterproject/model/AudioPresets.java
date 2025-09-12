package se233.audioconverterproject.model;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.entry;

public class AudioPresets {
    public static String[] formats = {"mp3", "wav", "ogg", "m4a"};

    public static Map<String,Map<String, Integer>> presets = new LinkedHashMap<>(Map.ofEntries(
            entry("mp3",Map.ofEntries(
                    entry("Economy (64 kbps)", 64_000),
                    entry( "Standard (128 kbps)", 128_000),
                    entry("Good (192 kbps)", 192_000),
                    entry("Best (320 kbps)",320_000)
            )),
            entry("wav", Map.ofEntries(
                    entry("Tape (20 kHz)", 20_000),
                    entry( "CD Quality (44.1 kHz)", 44_100),
                    entry("DVD (48 kHz)", 48_000),
                    entry("Extra High (96 kHz)",96_000)
            )),
            entry("m4a", Map.ofEntries(
                    entry("Economy (64 kbps)", 64_000),
                    entry( "Standard (128 kbps)", 128_000),
                    entry("Good (160 kbps)", 160_000),
                    entry("Best (256 kbps)",256_000)
            )),
            entry("ogg", Map.ofEntries(
                    entry("Economy (64 kbps)", 64_000),
                    entry( "Standard (128 kbps)", 128_000),
                    entry("Good (160 kbps)", 160_000),
                    entry("Best (256 kbps)",256_000)
            ))
    ));


    // BITRATES // NOTE: WAV FORMAT DOESN'T HAVE BITRATE //
    public static Map<String, Integer> bitratesMP3 = new LinkedHashMap<>(Map.ofEntries(
            entry("32 kbps", 32_000),
            entry("40 kbps", 40_000),
            entry("48 kbps", 48_000),
            entry("56 kbps", 56_000),
            entry("64 kbps", 64_000),
            entry("80 kbps", 80_000),
            entry("96 kbps",  96_000),
            entry("112 kbps", 112_000),
            entry("128 kbps", 128_000),
            entry("160 kbps", 160_000),
            entry("192 kbps",  192_000),
            entry("224 kbps",  224_000),
            entry("256 kbps", 256_000),
            entry("320 kbps", 320_000)
    ));
    public static Map<String, Integer> bitratesM4A = new LinkedHashMap<>(Map.ofEntries(
            entry("16 kbps", 16_000),
            entry("32 kbps", 32_000),
            entry("40 kbps", 40_000),
            entry("48 kbps", 48_000),
            entry("56 kbps", 56_000),
            entry("64 kbps", 64_000),
            entry("80 kbps",  80_000),
            entry("96 kbps", 96_000),
            entry("112 kbps", 112_000),
            entry("128 kbps", 128_000),
            entry("160 kbps",  160_000),
            entry("192 kbps",  192_000),
            entry("224 kbps", 224_000),
            entry("256 kbps", 256_000),
            entry("320 kbps", 320_000),
            entry("384 kbps", 384_000),
            entry("448 kbps", 448_000),
            entry("512 kbps", 512_000)
    ));
    public static Map<String, Integer> bitratesOGG = new LinkedHashMap<>(Map.ofEntries(
            entry("96 kbps", 32_000),
            entry("112 kbps", 40_000),
            entry("128 kbps", 48_000),
            entry("160 kbps", 56_000),
            entry("192 kbps", 64_000),
            entry("224 kbps", 80_000),
            entry("256 kbps",  96_000)
    ));
    // END OF BITRATES

    // SAMPLE RATE
    public static Map<String, Integer> sampleRatesMP3 = new LinkedHashMap<>(Map.ofEntries(
            entry("32000 kHz", 32_000),
            entry("44100 kHz", 44_100),
            entry("48000 kHz", 48_000)
    ));
    public static Map<String, Integer> sampleRatesWAV = new LinkedHashMap<>(Map.ofEntries(
            entry("8000 kHz", 8_000),
            entry("11025 kHz", 11_025),
            entry("12000 kHz", 12_000),
            entry("16000 kHz", 16_000),
            entry("22050 kHz", 22_050),
            entry("24000 kHz", 24_000),
            entry("32000 kHz", 32_000),
            entry("44100 kHz", 44_100),
            entry("48000 kHz", 48_000),
            entry("64000 kHz", 64_000),
            entry("88200 kHz", 88_200),
            entry("96000 kHz", 96_000)
    ));

    public static Map<String, Integer> sampleRatesM4A_OGG = new LinkedHashMap<>(Map.ofEntries(
            entry("8000 kHz", 8_000),
            entry("11025 kHz", 11_025),
            entry("12000 kHz", 12_000),
            entry("16000 kHz", 16_000),
            entry("22050 kHz", 22_050),
            entry("24000 kHz", 24_000),
            entry("32000 kHz", 32_000),
            entry("44100 kHz", 44_100),
            entry("48000 kHz", 48_000)
    ));
}
