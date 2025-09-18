package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se233.audioconverterproject.Launcher;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static se233.audioconverterproject.model.AudioPresets.*;

public class MainViewController {
    @FXML private ListView<String> inputListView;

    @FXML private Hyperlink Clickable_link;

    @FXML private Region dropRegion;

    @FXML private Button editButton;
    @FXML private Button doneButton;

    @FXML private Button convertBtn;

    @FXML private ComboBox<String> audioFormatComboBox;

    @FXML private ComboBox<String> audioQualityComboBox;

    
    @FXML private ImageView uploadIcon;

    @FXML private RadioButton monoRadio;
    @FXML private RadioButton stereoRadio;
    @FXML private ComboBox<String> sampleRateComboBox;
    @FXML private ComboBox<String> bitrateComboBox;

    private Map<String, String> fileMapList = new LinkedHashMap<>();

    private ToggleGroup channelsGroup;
    public void initialize(){
        uploadIcon.setImage(new Image(Launcher.class.getResourceAsStream("music-file.png")));
        // GROUPING RADIO BUTTON TOGETHER
        channelsGroup = new ToggleGroup();
        this.monoRadio.setToggleGroup(channelsGroup);
        this.stereoRadio.setToggleGroup(channelsGroup);
        stereoRadio.setSelected(true);
        editButton.setDisable(true);

        // INITIALIZE THE CONVERSION COMBOBOX
        if (audioFormatComboBox.getItems().isEmpty()) {
            audioFormatComboBox.getItems().addAll(formats);
            setDisableFunction(true);

            audioFormatComboBox.setOnAction(event -> {      // WHEN THE CONVERSION FORMAT IS CHANGED, THE QUALITIES ARE GOING TO CHANGE TOO, IN ORDER TO MATCH WITH THE FORMAT TYPE
                audioQualityComboBox.getItems().clear();
                sampleRateComboBox.getItems().clear();
                bitrateComboBox.getItems().clear();
                setDisableFunction(false);

                String key = audioFormatComboBox.getSelectionModel().getSelectedItem();
                audioQualityComboBox.setDisable(key.equals("flac") || key.equals("m4a") ||  key.equals("mp3"));

                List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(presets.get(audioFormatComboBox.getSelectionModel().getSelectedItem()).entrySet());
                sortedList.sort(Map.Entry.comparingByValue());

                for (Map.Entry<String, Integer> entry : sortedList) {
                    audioQualityComboBox.getItems().add(entry.getKey()); // ADD THE QUALITY SELECTION FROM PRESET AND SORTED ASCENDINGLY
                }
                audioQualityComboBox.getSelectionModel().select(0);     // SELECT A DEFAULT QUALITY

                Map<String, Integer> bitrateMap = switch (key) {
                    case "mp3" -> bitratesMP3;
                    case "m4a" -> bitratesM4A;
                    default -> null;
                };

                // Sort bitrate combobox ascendingly

                if (bitrateMap != null) {
                    sortedList = new ArrayList<>(bitrateMap.entrySet());
                    sortedList.sort(Map.Entry.comparingByValue());
                    for (Map.Entry<String, Integer> entry : sortedList) {
                        bitrateComboBox.getItems().add(entry.getKey());
                    }
                    if (!bitrateComboBox.getItems().isEmpty()) {
                        bitrateComboBox.getSelectionModel().select(0);
                    }
                    bitrateComboBox.setDisable(false);
                } else {
                    bitrateComboBox.getItems().clear();
                    bitrateComboBox.setDisable(true);
                }

                Map<String, Integer> sampleRateMap = switch (key) {
                    case "mp3" -> sampleRatesMP3;
                    case "wav" -> sampleRatesWAV;
                    case "m4a", "flac" -> sampleRatesM4A_FLAC;
                    default -> null;
                };

                if (sampleRateMap != null) {
                    sortedList = new ArrayList<>(sampleRateMap.entrySet());
                    sortedList.sort(Map.Entry.comparingByValue());
                    for (Map.Entry<String, Integer> entry : sortedList) {
                        sampleRateComboBox.getItems().add(entry.getKey());
                    }
                    sampleRateComboBox.getSelectionModel().select(0);
                }
            });

        } else {
            System.out.println("audioFormatComboBox already populated");
        }
        // END OF COMBOBOX EVENT HANDLER /////////////////////////////////////////////////////////////////////

        // ALL EVENT HANDLERS
        inputListView.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            //final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith("m4a");
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
        });

        inputListView.setOnDragEntered(event -> {
            if (event.getDragboard().hasFiles()) {
                inputListView.setStyle(
                        "-fx-border-color: #3498db; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-style: solid; " +
                                "-fx-border-radius: 10px;" +
                                "-fx-background-color: #eaf5ff;" +
                                "-fx-font-size: 16px;"
                );
            }
        });

        inputListView.setOnDragExited(event -> {
            inputListView.setStyle(
                    "-fx-border-color: #aaaaaa; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-style: dashed; " +
                            "-fx-border-radius: 10px;" +
                            "-fx-font-size: 16px;" +
                            "-fx-text-fill: #555555;"
            );
        });

        inputListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            String fileName;
            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0);
                fileName = file.getName();
                fileMapList.put(fileName, file.getAbsolutePath());
                inputListView.getItems().add(fileName);
            }

            event.setDropCompleted(success);
            event.consume();
        });

        Clickable_link.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a file");

            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio Formats", "*.mp3*", "*.wav*", "*.flac*", "*.m4a*");
            fileChooser.getExtensionFilters().add(filter);

            Stage stage = (Stage) Clickable_link.getScene().getWindow();
            List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);

            if (selectedFile != null) {
                for (File file : selectedFile) {
                    inputListView.getItems().add(file.getName());
                    fileMapList.put(file.getName(), file.getAbsolutePath());
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No File Selected");
                alert.setHeaderText(null);
                alert.setContentText("You didn't choose any file.");
                alert.showAndWait();
            }
        });


        // Remove element in ListView
        doneButton.setDisable(true);
        AtomicBoolean isEditing = new AtomicBoolean(false);
        editButton.setOnAction(event -> {
            isEditing.set(true);
            editButton.setDisable(true);
            doneButton.setDisable(false);
        });

        doneButton.setOnAction(event -> {
            isEditing.set(false);
            doneButton.setDisable(true);
            editButton.setDisable(false);
        });

        inputListView.setOnMouseClicked(event -> {
            if (isEditing.get()) {
                Object audioToRemove = inputListView.getSelectionModel().getSelectedItem();
                inputListView.getItems().remove(audioToRemove);
                fileMapList.remove(audioToRemove);
                System.out.println(fileMapList.size());
            }
        });

        // Handle Convert Button
            convertBtn.setOnAction(event -> {
                Parent bgRoot = Launcher.primaryStage.getScene().getRoot();
                if (!inputListView.getItems().isEmpty()) {
                    ProgressIndicator pi = new  ProgressIndicator();
                    VBox box = new VBox(pi);
                    box.setAlignment(Pos.CENTER);
                    Launcher.primaryStage.getScene().setRoot(box);
                    String format = audioFormatComboBox.getSelectionModel().getSelectedItem();
                    int quality;
                    if (!format.equals("flac")) {
                        quality = presets.get(format).get(audioQualityComboBox.getSelectionModel().getSelectedItem());
                    } else {
                        quality = 0;
                    }
                    int bitrate;
                    int sampleRate = switch (format) {
                        case "mp3" -> sampleRatesMP3.get(sampleRateComboBox.getSelectionModel().getSelectedItem());
                        case "wav" -> sampleRatesWAV.get(sampleRateComboBox.getSelectionModel().getSelectedItem());
                        case "m4a", "flac" ->
                                sampleRatesM4A_FLAC.get(sampleRateComboBox.getSelectionModel().getSelectedItem());
                        default -> { throw new IllegalStateException("Invalid format: " + format); }
                    };

                    short channel;
                    if (monoRadio.isSelected()) {
                        channel = 1;
                    } else {
                        channel = 2;
                    }
                    bitrate = switch (format) {
                        case "mp3" -> bitratesMP3.get(bitrateComboBox.getSelectionModel().getSelectedItem());
                        case "m4a" -> bitratesM4A.get(bitrateComboBox.getSelectionModel().getSelectedItem());
                        default -> 0;
                    };

                    //      LIST FOR STORING THE CONVERTED AUDIO FILES. NOTE: THIS ARRAY WILL BE USE FOR SAVING THE FILES ON THE SELECTED DIRECTORY
                    List<String> audioFiles = new ArrayList<>(inputListView.getItems().size());
                    Task<Void> processTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            fileMapList.forEach((key, value) -> {
                                ConverterTask task = new ConverterTask(format, quality, bitrate, sampleRate, channel, value);
                                ExecutorService executor = Executors.newFixedThreadPool(4);
                                ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executor);
                                try {
                                    audioFiles.add(completionService.submit(task).get());
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(Arrays.toString(audioFiles.toArray()));
                            });
                            return null;
                        }
                    };
                    processTask.setOnSucceeded(e -> {
                        Launcher.primaryStage.getScene().setRoot(bgRoot);
                        if (audioFiles.size() == inputListView.getItems().size()) {
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            directoryChooser.setTitle("Select a directory");
                            File selectedDir = directoryChooser.showDialog(Launcher.primaryStage);
                            if (selectedDir != null) {
                                for (String audioFile : audioFiles) {
                                    try {
                                        copyAudioFile(audioFile, selectedDir.getAbsolutePath());
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Conversion Complete.");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("File saved at '" +  selectedDir + "'.");
                                successAlert.showAndWait();
                            }
                        }
                    });
                    Thread thread = new Thread(processTask);
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please insert a file to convert.");
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.showAndWait();
             }
        });
    }

    public void setDisableFunction(boolean isDisabled) {
        audioQualityComboBox.setDisable(isDisabled);
        bitrateComboBox.setDisable(isDisabled);
        sampleRateComboBox.setDisable(isDisabled);
        monoRadio.setDisable(isDisabled);
        convertBtn.setDisable(isDisabled);
    }

    public void copyAudioFile(String source, String target) throws IOException {
        File sourceFile = new File(source);
        File targetFile = new File(target + "\\" + sourceFile.getName());

        try (FileInputStream fis = new FileInputStream(sourceFile)) {
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
        }
    }
}