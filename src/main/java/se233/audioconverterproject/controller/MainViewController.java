package se233.audioconverterproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import se233.audioconverterproject.Launcher;

import java.util.*;

import static java.util.Map.entry;

public class MainViewController {
    @FXML private Region dropRegion;

    @FXML private Button convertBtn;

    @FXML private ComboBox<String> audioFormatComboBox;

    @FXML private ComboBox<String> audioQualityComboBox;

    @FXML private ProgressBar progressBar;
    
    @FXML private ImageView uploadIcon;

    @FXML private RadioButton monoRadio;
    @FXML private RadioButton stereoRadio;
    @FXML private ComboBox<String> bitrateComboBox;

    @FXML private RadioButton sampleRate41kRadio;
    @FXML private RadioButton sampleRate48kRadio;
    @FXML private RadioButton sampleRate96kRadio;

    private ToggleGroup channelsGroup;
    private ToggleGroup qualityGroup;

    public static String[] formats = {"mp3", "wav", "ogg", "m4a", "flac"};
    public static Map<String, Integer> qualities = new LinkedHashMap<>(Map.ofEntries(
            entry("Economy (64 kbps)", 64_000),
            entry( "Standard (128 kbps)", 128_000),
            entry("Good (192 kbps)", 192_000),
            entry("Best (320 kbps)",320_000)
    ));

    public static Map<String, Integer> bitrates = new LinkedHashMap<>(Map.ofEntries(
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


    public void initialize(){
        uploadIcon.setImage(new Image(Launcher.class.getResourceAsStream("music-file.png")));
        channelsGroup = new ToggleGroup();
        qualityGroup = new ToggleGroup();
        this.monoRadio.setToggleGroup(channelsGroup);
        this.stereoRadio.setToggleGroup(channelsGroup);

        this.sampleRate41kRadio.setToggleGroup(qualityGroup);
        this.sampleRate48kRadio.setToggleGroup(qualityGroup);
        this.sampleRate96kRadio.setToggleGroup(qualityGroup);

        audioFormatComboBox.getItems().addAll(formats);
        audioFormatComboBox.getSelectionModel().select(0);  // SET DEFAULT VALUE TO `.mp3` FORMAT AND DISPLAY INSIDE THE FORMAT COMBOBOX


        // CREATE ARRAY TO STORE QUALITY KEYS AND DISPLAY INSIDE THE QUALITY COMBOBOX
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(qualities.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        for (Map.Entry<String, Integer> entry : sortedList) {
            audioQualityComboBox.getItems().add(entry.getKey());
        }
        audioQualityComboBox.getSelectionModel().select(2); // SET DEFAULT VALUE TO `GOOD (192 KBPS)`

        // CREATE ARRAY TO STORE BITRATE KEYS AND DISPLAY INSIDE THE BITRATE COMBOBOX
        bitrateComboBox.getItems().add("Bitrates");
        sortedList = new ArrayList<>(bitrates.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        for (Map.Entry<String, Integer> entry : sortedList) {
            bitrateComboBox.getItems().add(entry.getKey());
        }
        bitrateComboBox.getSelectionModel().select(0);
    }
}
