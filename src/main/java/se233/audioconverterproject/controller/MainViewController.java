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

    @FXML private ComboBox<Integer> audioQualityComboBox;

    @FXML private ProgressBar progressBar;
    
    @FXML private ImageView uploadIcon;

    @FXML private RadioButton monoRadio;
    @FXML private RadioButton stereoRadio;
    @FXML private ComboBox<Integer> bitrateComboBox;

    @FXML private RadioButton sampleRate41kRadio;
    @FXML private RadioButton sampleRate48kRadio;
    @FXML private RadioButton sampleRate96kRadio;

    private ToggleGroup channelsGroup;
    private ToggleGroup qualityGroup;

    public static String[] formats = {"mp3", "wav", "ogg", "m4a", "flac"};
    public static HashMap<String, Integer> qualities = (HashMap<String, Integer>) Map.ofEntries(
            entry("Economy (64 kbps)", 64_000),
            entry("Standard (128 kbps)", 128_000),
            entry("Good (192 kbps)", 192_000),
            entry("Best (320 kbps)", 320_000)
    );

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
    }
}
