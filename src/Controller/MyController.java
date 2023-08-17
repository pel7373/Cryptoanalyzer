package Controller;

import DAO.DAO;
import Model.Model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyController implements Controller {
    private String[] params;

    private static final int INPUT_FILE                = 0; //params[0] - input file name
    private static final int SHIFT                     = 1; //params[1] - shift
    private static final int OUTPUT_FILE               = 2; //params[2] - output file name
    private static final int COMMON_WORDS_FILE         = 3; //params[3] - file with common words
    private static final int EXAMPLE_TEXT_FILE         = 4; //params[4] - file with example text
    private static final int OPERATION_BEING_PERFORMED = 5; //params[5] - selected (being performed) operation
    private static final int INTERNAL_MESSAGE          = 6; //params[6] - internal (from method) message

    private StringBuilder outputMessage;
    private String inputText = null;

    private DAO dao;
    private Model model;

    public MyController(DAO dao, Model model, String[] params) {
        this.dao = dao;
        this.model = model;
        this.params = params;
    }

    public String handler() {
        outputMessage = new StringBuilder();
        inputText = null;
        String outputText;

        if(!checkFilesAndReadInput()) {
            return outputMessage.toString().trim();
        }

        //processed according to choice of operation
        try {
            if (params[OPERATION_BEING_PERFORMED].equals("1")) { //radioButton1Encryption.isSelected()
                outputText = performFirstOperationEncryption();
            } else if (params[OPERATION_BEING_PERFORMED].equals("2")) { //radioButton2Decryption.isSelected()
                outputText = performSecondOperationDecryption();
            } else if (params[OPERATION_BEING_PERFORMED].equals("3")) { //radioButton3BruteForce.isSelected()
                outputText = performThirdOperationBruteForce();
            } else if (params[OPERATION_BEING_PERFORMED].equals("4")) { //radioButton4Statistical.isSelected()
                outputText = performFourthOperationStatistics();
            } else {
                return outputMessage.toString().trim();
            }
            outputMessage.append("The operation was successfully performed!\n");
            writeToOutput(outputText);
        } catch (NumberFormatException e) {
            outputMessage.append("Error! Please, enter the correct shift value!\n");
        }

        return outputMessage.toString().trim();
    }

    private boolean checkFilesAndReadInput() {
        File file = new File(params[INPUT_FILE]);
        if(params[INPUT_FILE].trim().equals("")) {
            outputMessage.append("Error! The input file name can't be empty!\n");
            return false;
        }

        if(!file.exists()) {
            outputMessage.append("Error! The input file doesn't exist!\n");
            return false;
        }

        if(params[OUTPUT_FILE].trim().equals("")) {
            outputMessage.append("Error! The output file name can't be empty!\n");
            return false;
        }

        try {
            inputText = dao.readData(params[INPUT_FILE]);
        } catch (IOException e) {
            outputMessage.append("Error! The input file can't be read!\n");
            return false;
        }

        return true;
    }
    private boolean writeToOutput(String outputText) {
        try {
            dao.writeData(params[OUTPUT_FILE], outputText);
            outputMessage.append("The output file was written!\n");
            return true;
        } catch (IOException e) {
            outputMessage.append("Error! The output file can't be written!\n");
            return false;
        }
    }

    private String performFirstOperationEncryption() throws NumberFormatException {
        return model.encryptText(inputText, getShift());
    }

    private String performSecondOperationDecryption() throws NumberFormatException {
        return model.decryptText(inputText, getShift());
    }

    private String performThirdOperationBruteForce() {
        outputMessage = new StringBuilder();
        String outputText;

        outputMessage.append("You have selected: BruteForce\n");
        try {
            List<String> commonWords = readCommonWords(params[COMMON_WORDS_FILE]);
            outputText = model.bruteForce(inputText, commonWords);
        } catch (IOException e) {
            outputMessage.append("Error! The file with common words can't be read!\n");
            return null;
        }
        outputMessage.append(params[INTERNAL_MESSAGE]);
        outputMessage.append(System.lineSeparator());
        return outputText;
    }

    private String performFourthOperationStatistics() {
        outputMessage = new StringBuilder();
        String outputText;

        outputMessage.append("You have selected: Statistical analysis\n");
        try {
            String inputTextExample = dao.readData(params[EXAMPLE_TEXT_FILE]);
            outputText = model.DecryptionByStatistics(inputText, inputTextExample);
        } catch (IOException e) {
            outputMessage.append("Error! The file with text examples can't be read!\n");
            return null;
        }
        outputMessage.append(params[INTERNAL_MESSAGE]);
        outputMessage.append(System.lineSeparator());
        return outputText;
    }

    private int getShift() throws NumberFormatException {
        return Integer.parseInt(params[SHIFT]);
    }

    private List<String> readCommonWords(String commonWordsFileName) throws IOException {
        String w = dao.readData(commonWordsFileName);
        return Arrays.asList(w.split(System.lineSeparator()));
    }
}
