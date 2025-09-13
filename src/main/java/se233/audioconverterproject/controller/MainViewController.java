package se233.audioconverterproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import se233.audioconverterproject.Launcher;
import se233.audioconverterproject.model.AudioPresets;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Map.entry;
import static se233.audioconverterproject.model.AudioPresets.formats;
import static se233.audioconverterproject.model.AudioPresets.presets;

public class MainViewController {
    @FXML private ListView InputListView;

    @FXML private Hyperlink Clickable_link;

    @FXML private Region dropRegion;

    @FXML private Button convertBtn;

    @FXML private ComboBox<String> audioFormatComboBox;

    @FXML private ComboBox<String> audioQualityComboBox;

    @FXML private ProgressBar progressBar;
    
    @FXML private ImageView uploadIcon;

    @FXML private RadioButton monoRadio;
    @FXML private RadioButton stereoRadio;
    @FXML private ComboBox<String> sampleRateComboBox;
    @FXML private ComboBox<String> bitrateComboBox;
    private ToggleGroup channelsGroup;

    public void initialize(){
        uploadIcon.setImage(new Image(Launcher.class.getResourceAsStream("music-file.png")));

        // GROUPING RADIO BUTTON TOGETHER
        channelsGroup = new ToggleGroup();
        this.monoRadio.setToggleGroup(channelsGroup);
        this.stereoRadio.setToggleGroup(channelsGroup);

        // INITIALIZE THE CONVERSION COMBOBOX
        if (audioFormatComboBox.getItems().isEmpty()) {

            audioFormatComboBox.getItems().addAll(formats);

            audioFormatComboBox.setOnAction(event -> {      // WHEN THE CONVERSION FORMAT IS CHANGED, THE QUALITIES ARE GOING TO CHANGE TOO, IN ORDER TO MATCH WITH THE FORMAT TYPE
                audioQualityComboBox.getItems().clear();
                String key = audioFormatComboBox.getSelectionModel().getSelectedItem();
                audioQualityComboBox.setDisable(key.equals("flac"));

                List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(presets.get(key).entrySet());
                sortedList.sort(Map.Entry.comparingByValue());

                for (Map.Entry<String, Integer> entry : sortedList) {
                    audioQualityComboBox.getItems().add(entry.getKey());        // ADD THE QUALITY SELECTION FROM PRESET AND SORTED ASCENDINGLY
                }
                audioQualityComboBox.getSelectionModel().select(0);     // SELECT A DEFAULT QUALITY
            });

        } else {
            System.out.println("audioFormatComboBox already populated");
        }

        dropRegion.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            //final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith("mp4");
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
        });

        dropRegion.setOnDragEntered(event -> {
            if (event.getDragboard().hasFiles()) {
                dropRegion.setStyle(
                        "-fx-border-color: #3498db; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-style: solid; " +
                                "-fx-border-radius: 10px;" +
                                "-fx-background-color: #eaf5ff;" +
                                "-fx-font-size: 16px;"
                );
            }
        });

        dropRegion.setOnDragExited(event -> {
            dropRegion.setStyle(
                    "-fx-border-color: #aaaaaa; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-style: dashed; " +
                            "-fx-border-radius: 10px;" +
                            "-fx-font-size: 16px;" +
                            "-fx-text-fill: #555555;"
            );
        });

        dropRegion.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            String fileName;
            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0);
                fileName = file.getName();
                InputListView.getItems().add(fileName);
                uploadIcon.setVisible(false);
                System.out.println(fileName);
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}
