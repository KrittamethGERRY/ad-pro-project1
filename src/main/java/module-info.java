module se233.audioconverterproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.audioconverterproject to javafx.fxml;
    opens se233.audioconverterproject.controller to javafx.fxml;
    exports se233.audioconverterproject;
    exports se233.audioconverterproject.controller;
}