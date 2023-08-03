package Controller;

import DAO.DAO;
import Model.Model;

import java.io.File;
import java.io.IOException;

public class MyController implements Controller {
    StringBuilder outputMessage;
    String inputText = null;

    private DAO dao;
    private Model model;

    public MyController(DAO dao, Model model) {
        this.dao = dao;
        this.model = model;
    }

    public String handler(String[] params) {
        outputMessage = new StringBuilder();
        inputText = null;
        String outputText;

        if(!checkFilesAndReadInput(params)) {
            return outputMessage.toString().trim();
        }

        //processed according to choice of operation
        if (params[5].equals("1")) { //radioButton1Encryption.isSelected()
            outputText = performFirstOperationEncryption(params);
        } else if (params[5].equals("2")) { //radioButton2Decryption.isSelected()
            outputText = performSecondOperationDecryption(params);
        } else if (params[5].equals("3")) { //radioButton3BruteForce.isSelected()
            outputText = performThirdOperationBruteForce(params);
        } else if (params[5].equals("4")) { //radioButton4Statistical.isSelected()
            outputText = performFourthOperationStatistics(params);
        } else {
            return null;
        }

        if(outputText != null) {
            outputMessage.append("The operation was successfully performed!\n");
            writeToOutput(params, outputText);
        }
        return outputMessage.toString().trim();
    }

    private boolean checkFilesAndReadInput(String[] params) {
        File file = new File(params[0]);
        if(params[0].trim().equals("")) {
            outputMessage.append("Error! The inputfile name can't be empty!\n");
            return false;
        }

        if(!file.exists()) {
            outputMessage.append("Error! The inputfile doesn't exist!\n");
            return false;
        }

        if(params[2].trim().equals("")) {
            outputMessage.append("Error! The outputfile name can't be empty!\n");
            return false;
        }

        try {
            inputText = dao.readData(params[0]);
        } catch (IOException e) {
            outputMessage.append("Error! The inputfile can't be read!\n");
            return false;
        }

        return true;
    }
    private boolean writeToOutput(String[] params, String outputText) {
        try {
            dao.writeData(params[2], outputText);
        } catch (IOException e) {
            outputMessage.append("Error! The outputfile can't be written!\n");
            return false;
        }
        outputMessage.append("The outputfile was written!\n");
        return true;
    }

    private String performFirstOperationEncryption(String[] params) {
        int shift;
        String outputText;
        outputMessage = new StringBuilder();

        outputMessage.append("You have selected: Encrypt\n");
        try {
            shift = getShift(params);
        } catch (NumberFormatException e) {
            return null;
        }

        return model.encryptText(inputText, shift);
    }

    private String performSecondOperationDecryption(String[] params) {
        int shift;
        outputMessage = new StringBuilder();

        outputMessage.append("You have selected: Decrypt\n");
        try {
            shift = getShift(params);
        } catch (NumberFormatException e) {
            return null;
        }
        return model.encryptText(inputText, -shift);
    }

    private String performThirdOperationBruteForce(String[] params) {
        outputMessage = new StringBuilder();
        String outputText;

        outputMessage.append("You have selected: BruteForce\n");
        try {
            outputText = model.bruteForce(inputText, params[3]);
        } catch (IOException e) {
            outputMessage.append("Error! The file with common words can't be written!\n");
            return null;
        }
        outputMessage.append(params[6] + "\n");
        return outputText;
    }

    private String performFourthOperationStatistics(String params[]) {
        outputMessage = new StringBuilder();
        int definedKey;
        String outputText;

        outputMessage.append("You have selected: Statistical analysis\n");
        try {
            outputText = model.DecryptionByStatistics(inputText, params[4]);
        } catch (IOException e) {
            outputMessage.append("Error! The file with text examples can't be read!\n");
            return null;
        }
        outputMessage.append(params[6] + "\n");
        return outputText;
    }

    private int getShift(String[] params) throws NumberFormatException {
        try {
            return Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            outputMessage.append("Error! Please, enter the correct shift value!\n");
            throw new NumberFormatException();
        }
    }
}
