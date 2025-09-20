package se233.audioconverterproject.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import se233.audioconverterproject.Launcher;
import se233.audioconverterproject.model.exception.ConversionFailedException;

import java.awt.Desktop;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


import static se233.audioconverterproject.model.AudioPresets.*;

public class MainViewController {
	@FXML
	private ListView<String> inputListView;

	@FXML
	private Hyperlink hyperLink;

	@FXML
	private Button editButton;

	@FXML
	private Button convertBtn;

	@FXML
	private ComboBox<String> audioFormatComboBox;

	@FXML
	private ComboBox<String> audioQualityComboBox;

	@FXML
	private ImageView uploadIcon;

	@FXML
	private RadioButton monoRadio;
	@FXML
	private RadioButton stereoRadio;
	@FXML
	private ComboBox<String> sampleRateComboBox;
	@FXML
	private ComboBox<String> bitrateComboBox;

	private Map<String, String> fileMapList = new LinkedHashMap<>();

	private ToggleGroup channelsGroup;

	public void initialize() {
		uploadIcon.setImage(new Image(Launcher.class.getResourceAsStream("music-file.png")));
		uploadIcon.setMouseTransparent(true);
		

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

			audioFormatComboBox.setOnAction(event -> { // WHEN THE CONVERSION FORMAT IS CHANGED, THE QUALITIES ARE GOING
														// TO CHANGE TOO, IN ORDER TO MATCH WITH THE FORMAT TYPE
				audioQualityComboBox.getItems().clear();
				sampleRateComboBox.getItems().clear();
				bitrateComboBox.getItems().clear();
				setDisableFunction(false);

				String key = audioFormatComboBox.getSelectionModel().getSelectedItem();
				audioQualityComboBox.setDisable(key.equals("flac") || key.equals("m4a") || key.equals("mp3"));

				List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(
						presets.get(audioFormatComboBox.getSelectionModel().getSelectedItem()).entrySet());
				sortedList.sort(Map.Entry.comparingByValue());

				for (Map.Entry<String, Integer> entry : sortedList) {
					audioQualityComboBox.getItems().add(entry.getKey()); // ADD THE QUALITY SELECTION FROM PRESET AND
																			// SORTED ASCENDINGLY
				}
				audioQualityComboBox.getSelectionModel().select(0); // SELECT A DEFAULT QUALITY

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
		// END OF COMBOBOX EVENT HANDLER
		// /////////////////////////////////////////////////////////////////////

		// ALL EVENT HANDLERS
		inputListView.setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if (db.hasFiles()) {
				event.acceptTransferModes(TransferMode.COPY);
			}
			event.consume();
		});

		inputListView.setOnDragEntered(event -> {
			if (event.getDragboard().hasFiles()) {
				inputListView.setStyle("-fx-border-color: #3498db; " + "-fx-border-width: 2px; "
						+ "-fx-border-style: solid; " + "-fx-border-radius: 10px;" + "-fx-background-color: #E5E7EB;"
						+ "-fx-font-size: 16px;");
			}
		});

		inputListView.setOnDragExited(event -> {
			inputListView
					.setStyle("-fx-border-color: #aaaaaa; " + "-fx-border-width: 2px; " + "-fx-border-style: dashed; "
							+ "-fx-border-radius: 10px;" + "-fx-font-size: 16px;" + "-fx-text-fill: #555555;");
		});

		inputListView.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			String fileName;
			if (db.hasFiles()) {
				success = true;
				File file = db.getFiles().get(0);
				fileName = file.getName();
				try {
					if (!fileName.substring(fileName.lastIndexOf('.')).matches(".mp3|.wav|.flac|.m4a")) {
						// ALERT WHEN INPUT INVALID FILE FORMAT
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("WARNING");
						alert.setHeaderText("WARNING: Invalid format");
						alert.setContentText("Invalid audio file format (should an audio format: .mp3, .wav, .flac, .m4a)");
						alert.showAndWait();
						throw new InvalidFileFormatException("Invalid file format", new IOException());
					} else {
						fileMapList.put(fileName, file.getAbsolutePath());
						inputListView.getItems().add(fileName);
						editButton.setDisable(false);
					}
				} catch (InvalidFileFormatException e) {
					e.printStackTrace();
				}

			}

			event.setDropCompleted(success);
			event.consume();
		});

		hyperLink.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select a file");

			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio Formats", "*.mp3*", "*.wav*",
					"*.flac*", "*.m4a*");
			fileChooser.getExtensionFilters().add(filter);

			Stage stage = (Stage) hyperLink.getScene().getWindow();
			List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);

			if (selectedFile != null) {
				for (File file : selectedFile) {
					inputListView.getItems().add(file.getName());
					fileMapList.put(file.getName(), file.getAbsolutePath());
					editButton.setDisable(false);
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
		AtomicBoolean isEditing = new AtomicBoolean(false);
		editButton.setOnAction(e -> {
			{
				isEditing.set(!isEditing.get());
				if (isEditing.get()) {
					editButton.setText("Done");
					editButton.setStyle("-fx-font-weight: bold");
				} else {
					editButton.setText("Edit");
					editButton.setStyle("-fx-font-weight: normal");
					if (inputListView.getItems().isEmpty()) {
						editButton.setDisable(true);
					}
				}
			}
		});
		inputListView.setOnMouseClicked(event -> {
			Object selectedAudio = inputListView.getSelectionModel().getSelectedItem();
			if (isEditing.get()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Are you sure to remove the audio from the list?");
				alert.setHeaderText(null);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					inputListView.getItems().remove(selectedAudio);
					fileMapList.remove(selectedAudio);
					
					if (inputListView.getItems().isEmpty()) {
						editButton.setDisable(true);
						isEditing.set(false);
						editButton.setText("Edit");
						editButton.setStyle("-fx-font-weight: normal");
					}
				}
			} else if (!isEditing.get() && !inputListView.getItems().isEmpty() && selectedAudio != null){
				File audioFile = new File(fileMapList.get(selectedAudio));
	            if (audioFile != null) {
	                try {
	                    if (Desktop.isDesktopSupported()) {
	                        Desktop.getDesktop().open(audioFile);
	                    } else {
	                        System.err.println("Desktop API is not supported.");
	                    }
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
			}

		});

		// Handle Convert Button
		convertBtn.setId("convertBtn");
		convertBtn.setOnAction(event -> {
			Parent bgRoot = Launcher.primaryStage.getScene().getRoot();
			if (!inputListView.getItems().isEmpty()) {
				ProgressIndicator pi = new ProgressIndicator();
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
				case "m4a", "flac" -> sampleRatesM4A_FLAC.get(sampleRateComboBox.getSelectionModel().getSelectedItem());
				default -> {
					throw new IllegalStateException("Invalid format: " + format);
				}
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

				// LIST FOR STORING THE CONVERTED AUDIO FILES. NOTE: THIS ARRAY WILL BE USE FOR
				// SAVING THE FILES ON THE SELECTED DIRECTORY
				List<String> audioFiles = new ArrayList<>(inputListView.getItems().size());
				Task<Void> processTask = new Task<>() {
					@Override
					protected Void call() throws ConversionFailedException {
						fileMapList.forEach((key, value) -> {
							ConverterTask task = new ConverterTask(format, quality, bitrate, sampleRate, channel,
									value);
							ExecutorService executor = Executors.newFixedThreadPool(4);
							ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(
									executor);
							try {
								audioFiles.add(completionService.submit(task).get());
							} catch (InterruptedException | ExecutionException e) {
								throw new ConversionFailedException(e.getMessage());
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
							successAlert.setContentText("File saved at '" + selectedDir + "'.");
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
		sourceFile.delete();
	}
}