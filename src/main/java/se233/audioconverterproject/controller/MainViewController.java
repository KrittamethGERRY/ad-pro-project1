package se233.audioconverterproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

public class MainViewController {
    @FXML private Region dropRegion;

    @FXML private Button convertBtn;

    @FXML private ComboBox audioFormatComboBox;

    @FXML private ComboBox audioQualityComboBox;

    @FXML private ProgressBar progressBar;

    @FXML private RadioButton monoRadio;
    @FXML private RadioButton stereoRadio;

    @FXML private RadioButton sampleRate41kRadio;
    @FXML private RadioButton sampleRate48kRadio;

    private ToggleGroup channelsGroup;
    private ToggleGroup qualityGroup;

    public void initialize(){
        channelsGroup = new ToggleGroup();
        qualityGroup = new ToggleGroup();
        this.monoRadio.setToggleGroup(channelsGroup);
        this.stereoRadio.setToggleGroup(channelsGroup);

        this.sampleRate41kRadio.setToggleGroup(qualityGroup);
        this.sampleRate48kRadio.setToggleGroup(qualityGroup);
    }
}
