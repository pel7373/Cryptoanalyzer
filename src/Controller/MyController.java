package Controller;

import DAO.DAO;
import Model.Model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyController implements Controller {
    private StringBuilder outputMessage;
    private String inputText = null;

    private DAO dao;
    private Model model;
    private String[] params;

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
            if (params[5].equals("1")) { //radioButton1Encryption.isSelected()
                outputText = performFirstOperationEncryption();
            } else if (params[5].equals("2")) { //radioButton2Decryption.isSelected()
                outputText = performSecondOperationDecryption();
            } else if (params[5].equals("3")) { //radioButton3BruteForce.isSelected()
                outputText = performThirdOperationBruteForce();
            } else if (params[5].equals("4")) { //radioButton4Statistical.isSelected()
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
        File file = new File(params[0]);
        if(params[0].trim().equals("")) {
            outputMessage.append("Error! The input file name can't be empty!\n");
            return false;
        }

        if(!file.exists()) {
            outputMessage.append("Error! The input file doesn't exist!\n");
            return false;
        }

        if(params[2].trim().equals("")) {
            outputMessage.append("Error! The output file name can't be empty!\n");
            return false;
        }

        try {
            inputText = dao.readData(params[0]);
        } catch (IOException e) {
            outputMessage.append("Error! The input file can't be read!\n");
            return false;
        }

        return true;
    }
    private boolean writeToOutput(String outputText) {
        try {
            dao.writeData(params[2], outputText);
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
            List<String> commonWords = readCommonWords(params[3]);
            outputText = model.bruteForce(inputText, commonWords);
        } catch (IOException e) {
            outputMessage.append("Error! The file with common words can't be read!\n");
            return null;
        }
        outputMessage.append(params[6]);
        outputMessage.append(System.lineSeparator());
        return outputText;
    }

    private String performFourthOperationStatistics() {
        outputMessage = new StringBuilder();
        String outputText;

        outputMessage.append("You have selected: Statistical analysis\n");
        try {
            String inputTextExample = dao.readData(params[4]);
            outputText = model.DecryptionByStatistics(inputText, inputTextExample);
        } catch (IOException e) {
            outputMessage.append("Error! The file with text examples can't be read!\n");
            return null;
        }
        outputMessage.append(params[6]);
        outputMessage.append(System.lineSeparator());
        return outputText;
    }

    private int getShift() throws NumberFormatException {
        return Integer.parseInt(params[1]);
    }

    private List<String> readCommonWords(String commonWordsFileName) throws IOException {
        String w = dao.readData(commonWordsFileName);
        return Arrays.asList(w.split(System.lineSeparator()));
    }
}
