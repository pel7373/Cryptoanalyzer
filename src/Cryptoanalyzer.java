import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class Cryptoanalyzer extends Application {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,\":-!? ";
    static String messageFromBruteForce = "";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Label labelChoiceRadioButton = new Label("Select your operation:");
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

                String inputFile = textInputFileName.getText();
                String outputFile = textOutputFileName.getText();
                String outputText;
                int shift;
                File file = new File(inputFile);
                if(inputFile.trim().equals("")) {
                    labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The inputfile name can't be empty!\n");
                    return;
                }

                if(!file.exists()) {
                    labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The inputfile doesn't exist!\n");
                    return;
                }

                if(outputFile.trim().equals("")) {
                    labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The outputfile name can't be empty!\n");
                    return;
                }
                String inputText;
                try {
                    inputText = readTextFromFile(inputFile);
                } catch (IOException e) {
                    labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The inpputfile can't be read!\n");
                    return;
                }

                //processed according to choice of operation
                if(radioButton1Encryption.isSelected())
                    {
                        labelOutputMessage.setText("You have selected: Encrypt\n");
                        try {
                            shift =  Integer.parseInt(textInputShift.getText());
                        } catch (NumberFormatException e) {
                            labelOutputMessage.setText(labelOutputMessage.getText() + "Error! Please, enter the correct shift value!\n");
                            return;
                        }

                        outputText = encryptText(inputText, shift);
                        try {
                            writeToFile(outputFile, outputText);
                        } catch (IOException e) {
                            labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The outputfile can't be written!\n");
                            return;
                        }
                        labelOutputMessage.setText(labelOutputMessage.getText() + "The outputfile was written!\n");
                    }
                else if(radioButton2Decryption.isSelected())
                {
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);

                    labelOutputMessage.setText("You have selected: Decrypt\n");
                    try {
                        shift =  Integer.parseInt(textInputShift.getText());
                    } catch (NumberFormatException e) {
                        labelOutputMessage.setText("Error! Please, enter the correct shift value!\n");
                        return;
                    }

                    outputText = encryptText(inputText, -shift);
                    try {
                        writeToFile(outputFile, outputText);
                    } catch (IOException e) {
                        labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The outputfile can't be written!\n");
                        return;
                    }
                    labelOutputMessage.setText(labelOutputMessage.getText() + "The outputfile was written!\n");
                }

                else if(radioButton3BruteForce.isSelected())
                {
                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);

                    labelOutputMessage.setText("You have selected: BruteForce\n");
                    try {
                        outputText = bruteForce(inputText, textCommonWordsFileName.getText());
                    } catch (IOException e) {
                        labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The file with common words can't be written!\n");
                        return;
                    }

                    try {
                        writeToFile(outputFile, outputText);
                    } catch (IOException e) {
                        labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The outputfile can't be written!\n");
                        return;
                    }
                    labelOutputMessage.setText(labelOutputMessage.getText() + messageFromBruteForce + "\n");
                    labelOutputMessage.setText(labelOutputMessage.getText() + "The outputfile was written!\n");
                }

                else if(radioButton4Statistical.isSelected())
                {
                    int definedKey;

                    labelInputShift.setDisable(true);
                    textInputShift.setDisable(true);
                    labelCommonWordsFileName.setDisable(true);
                    textCommonWordsFileName.setDisable(true);

                    labelOutputMessage.setText("You have selected: Statistical analysis\n");

                    try {
                        definedKey = friquencyDefinedKey(inputText, textTextExampleFileName.getText());
                        outputText = encryptText(inputText, -definedKey);
                    } catch (IOException e) {
                        labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The file with text examples can't be read!\n");
                        return;
                    }

                    try {
                        writeToFile(outputFile, outputText);
                    } catch (IOException e) {
                        labelOutputMessage.setText(labelOutputMessage.getText() + "Error! The outputfile can't be written!\n");
                        return;
                    }
                    labelOutputMessage.setText(labelOutputMessage.getText() + "The key was calculated: " + definedKey + "\n");
                    labelOutputMessage.setText(labelOutputMessage.getText() + "The outputfile was written!\n");
                }
            }
        });
    }

    private static String bruteForce(String inputEncryptedText, String commonWordsFileName) throws IOException {
        List<String> commonWords = readCommonWords(commonWordsFileName);

        int counterCoincidenceMax = 0;
        int counterCoincidenceMax2 = 0;
        int counterCoincidenceMaxShift = 0;
        for(int i = 0; i < ALPHABET.length(); i++) {
            String outputText = encryptText(inputEncryptedText, -i);
            int localCounterCoincidence = 0;
            for (String wordHit:
                    commonWords) {
                localCounterCoincidence += countSubstringCoincidenceIgnoreCase(outputText, wordHit);
            }

            if(localCounterCoincidence > counterCoincidenceMax) {
                counterCoincidenceMax2 = counterCoincidenceMax;
                counterCoincidenceMax = localCounterCoincidence;
                counterCoincidenceMaxShift = i;
            }
        }
        messageFromBruteForce = "Зсув: " + counterCoincidenceMaxShift + "; співпадінь зі словником: " + counterCoincidenceMax + "; на 2-му місці співпадінь " + counterCoincidenceMax2;
        return encryptText(inputEncryptedText, -counterCoincidenceMaxShift);
    }

    private static int countSubstringCoincidenceIgnoreCase(String string, String substring) {
        return (string.length() - string.toLowerCase().replace(substring.toLowerCase(), "").length()) / substring.length();
    }

    private static List<String> readCommonWords(String commonWordsFileName) throws IOException {
        List<String> result = new ArrayList<>(320);
        String s;
        FileReader in = new FileReader(commonWordsFileName);
        BufferedReader reader = new BufferedReader(in);
        while (reader.ready()) {
            s = reader.readLine();
            if(!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

    private static String encryptText(String inputText, int shift) {
        StringBuilder output = new StringBuilder();

        if (shift < 0) {
            shift = ALPHABET.length() + (shift % ALPHABET.length());
        }

        for (char c :
                inputText.toCharArray()) {
            int indexChar = ALPHABET.indexOf(c);
            if (indexChar != -1) {
                output.append(ALPHABET.charAt((indexChar + shift) % ALPHABET.length()));
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    private static int friquencyDefinedKey(String inputText, String fileExample) throws IOException {

        //Завантажуємо додатковий файл із текстом, бажано — того самого автора і тієї самої стилістики.
        //Програма складає статистику входження символів у закодований текст і додатковий файл із текстом.
        //Підраховуємо для вісьми найвживаніших літер у вхідному і додатковому тексті
        // - відстань між цими буквами у алфавіті.
        // Тобто вираховуємо різницю між першою найвживанішою літерою у вхідному і у додатковому тексті,
        // між другою..., і так до восьмої. Зрозуміло, що значення різниці для цих пар літер
        // (від першої і до восьмої) може бути різним. Тому підраховуємо, яка різниця зустрічається частіше
        // - це і буде наш ключ, з яким ми будемо разкодовувати вхідний текст.

        String inputTextExample = readTextFromFile(fileExample);
        int[] countCharsInputText = new int[ALPHABET.length()];

        //count the quantity of times each letter is used in input text
        for (char c :
                inputText.toCharArray()) {
            int indexChar = ALPHABET.indexOf(c);
            if (indexChar != -1) {
                countCharsInputText[indexChar]++;
            }
        }

        //create map of each char (key) and its calculated quantity of uses in input text
        Map<Character, Integer> inputTextMap = new HashMap<>();
        for (char c :
                ALPHABET.toCharArray()) {
            inputTextMap.put(c, countCharsInputText[ALPHABET.indexOf(c)]);
        }

        //create sorted list of each char (key) and its calculated quantity of uses in input text
        List<Map.Entry<Character, Integer>> listInputText = inputTextMap.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        //count the number of uses of each letter in example text
        int[] countCharsExampleText = new int[ALPHABET.length()];
        for (char c :
                inputTextExample.toCharArray()) {
            int indexChar = ALPHABET.indexOf(c);
            if (indexChar != -1) {
                countCharsExampleText[indexChar]++;
            }
        }

        //create map of each char (key) and its calculated quantity of uses in example text
        Map<Character, Integer> exampleTextMap = new HashMap<>();
        for (char c :
                ALPHABET.toCharArray()) {
            exampleTextMap.put(c, countCharsExampleText[ALPHABET.indexOf(c)]);
        }

        //create sorted list of each char (key) and its calculated quantity of uses in example text
        List<Map.Entry<Character, Integer>> listExampleText = exampleTextMap.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());


        //Тепер підраховуємо для вісьми найвживаніших літер у вхідному і зразковому тексті - різниця між цими буквами.
        //Тобто різницю між першою найвживанішою літерою у вхідному і зразковому тексті, між другою..., між восьмою -
        //і заносимо це в масив newShift

        Integer[] newShift = new Integer[8];

        char c, c2;
        for (int i = 0; i < 8; i++) {
            c = listInputText.get(i).getKey();
            c2 = listExampleText.get(i).getKey();
            newShift[i] = Math.abs((ALPHABET.length() - ALPHABET.indexOf(c2) + ALPHABET.indexOf(c)) % ALPHABET.length());
        }

        //Для різних літер ця різниця може бути різною - тому вираховуємо

        Map<Integer, Integer> newShiftCount = new HashMap<>();
        for (Integer n : newShift)
        {
            newShiftCount.put(n, newShiftCount.getOrDefault(n, 0) + 1);
        }

        int maxKey = 0;
        int maxValue = 0;
        for (Map.Entry<Integer,Integer> entry : newShiftCount.entrySet()) {
            if(entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }
        return maxKey;
    }

    private static String readTextFromFile(String inputFileName) throws IOException {
        StringBuilder input = new StringBuilder();
        FileInputStream fis = new FileInputStream(inputFileName);
        int i;
        while((i = fis.read())!= -1){
            input.append((char)i);
        }
        fis.close();
        return input.toString().trim();
    }

    private static void writeToFile(String outputFileName, String output) throws IOException {
        PrintWriter pw = new PrintWriter(outputFileName);
        pw.write(output);
        pw.close();
    }
}