package se233.audioconverterproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import se233.audioconverterproject.Launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Map.entry;
import static se233.audioconverterproject.model.AudioPresets.formats;

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




/*        // CREATE ARRAY TO STORE QUALITY KEYS AND DISPLAY INSIDE THE QUALITY COMBOBOX
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
        try {
            convert();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    public void convert() throws IOException, URISyntaxException {
        System.out.println("Converting...");
        System.out.println(new ReduceMapTaskConverter("ogg", 64_000, 64_000, 1, "").call());

    }
}
