package View;

import Controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class MyView extends Application implements View  {
    private static String[] params;
    private String[] params2;

    private static final int INPUT_FILE                = 0; //params[0] - input file name
    private static final int SHIFT                     = 1; //params[1] - shift
    private static final int OUTPUT_FILE               = 2; //params[2] - output file name
    private static final int COMMON_WORDS_FILE         = 3; //params[3] - file with common words
    private static final int EXAMPLE_TEXT_FILE         = 4; //params[4] - file with example text
    private static final int OPERATION_BEING_PERFORMED = 5; //params[5] - selected (being performed) operation
    private static final int INTERNAL_MESSAGE          = 6; //params[6] - internal (from method) message

    private static Controller controller;

    public void letsStart(Controller controller, String[] params){
        this.controller = controller;
        this.params = params;
        this.params2 = params;
        System.out.println("params2 in MyView: " + Arrays.asList(params2));
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Label labelChoiceRadioButton = new Label("Select your operation: ");
        ToggleGroup toggle = new ToggleGroup();
        RadioButton radioButton1Encryption = new RadioButton("Encryption");
        RadioButton radioButton2Decryption = new RadioButton("Decryption");
        RadioButton radioButton3BruteForce = new RadioButton("BruteForce");
        RadioButton radioButton4Statistical = new RadioButton("Decryption by statistical analysis");
        radioButton1Encryption.setToggleGroup(toggle);
        radioButton2Decryption.setToggleGroup(toggle);
        radioButton3BruteForce.setToggleGroup(toggle);
        radioButton4Statistical.setToggleGroup(toggle);
        Label labelInputFileName = new Label("\nInput file name:");
        TextField textInputFileName = new TextField("Write input filename here");
        textInputFileName.setMaxWidth(200);

        Label labelInputShift = new Label("\nWrite your shift:");
        TextField textInputShift = new TextField("Write shift here");
        textInputShift.setMaxWidth(100);

        Label labelOutputFileName = new Label("\nOutput file name:");
        TextField textOutputFileName = new TextField("Write output filename here");
        textOutputFileName.setMaxWidth(200);

        Label labelCommonWordsFileName = new Label("\nFile with common words for brute force:");
        labelCommonWordsFileName.setWrapText(true);
        TextField textCommonWordsFileName = new TextField("common-words.txt");
        textCommonWordsFileName.setMaxWidth(200);

        Label labelTextExampleFileName = new Label("\nFile with text example for statistical analysis:");
        labelTextExampleFileName.setWrapText(true);
        TextField textTextExampleFileName = new TextField("textExample.txt");
        textTextExampleFileName.setMaxWidth(200);

        Button buttonSubmit = new Button("Submit");

        VBox vBoxRoot = new VBox();
        vBoxRoot.getChildren().add(labelChoiceRadioButton);
        vBoxRoot.getChildren().addAll(
                radioButton1Encryption,
                radioButton2Decryption,
                radioButton3BruteForce,
                radioButton4Statistical);
        vBoxRoot.getChildren().addAll(
                labelInputFileName,
                textInputFileName,
                labelInputShift,
                textInputShift,
                labelOutputFileName,
                textOutputFileName,
                labelCommonWordsFileName,
                textCommonWordsFileName,
                labelTextExampleFileName,
                textTextExampleFileName,
                buttonSubmit);

        Label labelOutputMessage = new Label();
        labelOutputMessage.setTextFill(Color.RED);
        labelOutputMessage.setMaxWidth(200);
        labelOutputMessage.setWrapText(true);

        VBox vBoxRoot2 = new VBox();
        vBoxRoot2.getChildren().add(labelOutputMessage);

        //we need to add this layout to a scene
        GridPane gridPaneRoot = new GridPane();
        gridPaneRoot.add(vBoxRoot, 0, 0);
        gridPaneRoot.add(vBoxRoot2, 0, 1);
        gridPaneRoot.setAlignment(Pos.CENTER);
        Scene sc = new Scene(gridPaneRoot);

        primaryStage.setHeight(750);
        primaryStage.setWidth(500);
        primaryStage.setTitle("Cryptoanalyzer by Pavlo Liebiediev");
        primaryStage.setScene(sc);
        primaryStage.show();

        radioButton1Encryption.fire();
        labelCommonWordsFileName.setDisable(true);
        textCommonWordsFileName.setDisable(true);
        labelTextExampleFileName.setDisable(true);
        textTextExampleFileName.setDisable(true);
        labelOutputMessage.setText(radioButton1Encryption.getText() + " selected\n");

        toggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton rb = (RadioButton)toggle.getSelectedToggle();
                if (rb != null) {
                    // change the label
                    labelOutputMessage.setText(rb.getText() + " selected\n");
                }

                labelInputShift.setDisable(false);
                textInputShift.setDisable(false);

                if(radioButton1Encryption.isSelected()) {
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);
                    labelTextExampleFileName.setDisable(true);
                    textTextExampleFileName.setDisable(true);
                } else if(radioButton2Decryption.isSelected()) {
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);
                    labelTextExampleFileName.setDisable(true);
                    textTextExampleFileName.setDisable(true);
                } else if(radioButton3BruteForce.isSelected()) {
                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(false);
                    textCommonWordsFileName.setDisable(false);
                    labelTextExampleFileName.setDisable(true);
                    textTextExampleFileName.setDisable(true);
                } else if(radioButton4Statistical.isSelected()) {
                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);
                    labelTextExampleFileName.setDisable(false);
                    textTextExampleFileName.setDisable(false);
                }
            }
        });

        buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(params2 != null)
                    System.out.println("params2 in buttonSubmit.setOnAction" + Arrays.asList(params2));
                else
                    System.out.println("params2 in buttonSubmit.setOnAction  is null!!!");

                MyView.this.params[INPUT_FILE] = textInputFileName.getText();
                MyView.this.params[SHIFT] = textInputShift.getText();
                MyView.this.params[OUTPUT_FILE] = textOutputFileName.getText();
                MyView.this.params[COMMON_WORDS_FILE] = textCommonWordsFileName.getText();
                MyView.this.params[EXAMPLE_TEXT_FILE] = textTextExampleFileName.getText();

                if (radioButton1Encryption.isSelected()) {
                    MyView.this.params[OPERATION_BEING_PERFORMED] = "1";
                } else if (radioButton2Decryption.isSelected()) {
                    MyView.this.params[OPERATION_BEING_PERFORMED] = "2";
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);
                } else if (radioButton3BruteForce.isSelected()) {
                    MyView.this.params[OPERATION_BEING_PERFORMED] = "3";
                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(false);
                    textCommonWordsFileName.setDisable(false);
                } else if (radioButton4Statistical.isSelected()) {
                    MyView.this.params[OPERATION_BEING_PERFORMED] = "4";
                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);
                } else {
                    MyView.this.params[OPERATION_BEING_PERFORMED] = null;
                }

                String outputMessage = MyView.this.controller.handler();
                labelOutputMessage.setText(outputMessage);
            }
        });
    }
}