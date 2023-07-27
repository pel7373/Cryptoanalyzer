import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cryptoanalyzer {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvxyz.,\":-!? ";

    public static void main(String[] args) {
        //String inputString = "Mrs Reed then enlists the aid of the harsh Mr Brocklehurst, who is the director of Lowood Institution, a charity school for girls, to enroll Jane. Mrs Reed cautions Mr Brocklehurst that Jane has a \"tendency to deceit\", which he interprets as Jane being a liar. Before Jane leaves, however, she confronts Mrs Reed and declares that she'll never call her \"aunt\" again. Jane also tells Mrs Reed and her daughters, Georgiana and Eliza, that they are the ones who are deceitful, and that she will tell everyone at Lowood how cruelly the Reeds treated her. Mrs Reed is hurt badly by these words, but does not have the courage or tenacity to show this.";
        //String outputString = null;

        int shift;

        if (args.length != 2) {
            System.out.println("Аргументів програми повинно бути 2: ім'я файла та число - на скільки літер зсуваємо текст");
            return;
        }
        try {
            shift =  Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Помилка! Другим аргументом введіть правильне число - на скільки літер зсуваємо текст");
            return;
        }

        String inputText = readTextFromFile(args[0]);
        String outputText = encryptText(inputText, shift);
        writeToFile(args[0] + "-encrypt", outputText);

        String inputEncryptedText = readTextFromFile(args[0] + "-encrypt");
        String outputDecryptedText = encryptText(inputEncryptedText, -shift);
        writeToFile(args[0] + "-decrypt", outputDecryptedText);

        String outputBruteForceText = bruteForce(outputText);
        writeToFile(args[0] + "-decryptBruteForce", outputBruteForceText);

    }

    private static String bruteForce(String inputEncryptedText) {
        List<String> commonWords = readCommonWords();

        int counterCoincidenceMax = 0;
        int counterCoincidenceMaxShift = 0;
        for(int i = 0; i < ALPHABET.length(); i++) {
            String outputText = encryptText(inputEncryptedText, -i);
            int localCounterCoincidence = 0;
            for (String wordHit:
                    commonWords) {
                localCounterCoincidence += countSubstringCoincidenceIgnoreCase(outputText, wordHit);
            }

            if(localCounterCoincidence > counterCoincidenceMax) {
                counterCoincidenceMax = localCounterCoincidence;
                counterCoincidenceMaxShift = i;
            }
        }
        System.out.println("БрутФорс визначив зсув: " + counterCoincidenceMaxShift + "; при цьому співпадінь зі словником: " + counterCoincidenceMax);
        return encryptText(inputEncryptedText, -counterCoincidenceMaxShift);
    }

    private static int countSubstringCoincidenceIgnoreCase(String string, String substring) {
        return (string.length() - string.toLowerCase().replace(substring.toLowerCase(), "").length()) / substring.length();
    }

    private static List<String> readCommonWords() {
        List<String> result = new ArrayList<>(320);
        String nameFileCommonWordsEn = "common-words.txt";
        String s;
        try (FileReader in = new FileReader(nameFileCommonWordsEn);
             BufferedReader reader = new BufferedReader(in)) {
            while (reader.ready()) {
                s = reader.readLine();
                if(!s.isEmpty()) {
                    result.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні файла " + nameFileCommonWordsEn);
            e.printStackTrace();
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

    private static String readTextFromFile(String inputFileName) {
        StringBuilder input = new StringBuilder();
        try (FileReader in = new FileReader(inputFileName);
             BufferedReader reader = new BufferedReader(in)) {
            while (reader.ready()) {
                input.append(reader.readLine());
                input.append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні файла " + inputFileName);
            e.printStackTrace();
        }
        return input.toString().trim();
    }

    private static void writeToFile(String outputFileName, String output) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            bufferedWriter.append(output);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Помилка при запису файла " + outputFileName);
            e.printStackTrace();
        }
    }
}