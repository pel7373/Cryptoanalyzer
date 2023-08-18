package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyModel implements Model {
    private String[] params;

    private static final int INPUT_FILE                = 0; //params[0] - input file name
    private static final int SHIFT                     = 1; //params[1] - shift
    private static final int OUTPUT_FILE               = 2; //params[2] - output file name
    private static final int COMMON_WORDS_FILE         = 3; //params[3] - file with common words
    private static final int EXAMPLE_TEXT_FILE         = 4; //params[4] - file with example text
    private static final int OPERATION_BEING_PERFORMED = 5; //params[5] - selected (being performed) operation
    private static final int INTERNAL_MESSAGE          = 6; //params[6] - internal (from method) message

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,\":-!? ";

    public MyModel(String[] params) {
        this.params = params;
    }

    @Override
    public String encryptText(String inputText, int shift) {
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

    public String decryptText(String inputText, int shift) {
        return encryptText(inputText, -shift);
    }

    @Override
    public String bruteForce(String inputEncryptedText, List<String> commonWords) {
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
        //Send message from this method to controller
        params[INTERNAL_MESSAGE] = "Зсув: " + counterCoincidenceMaxShift + "; співпадінь зі словником: "
                + counterCoincidenceMax + "; на 2-му місці співпадінь " + counterCoincidenceMax2;
        return encryptText(inputEncryptedText, -counterCoincidenceMaxShift);
    }

    @Override
    public String DecryptionByStatistics(String inputText, String inputTextExample) {
        int definedKey = friquencyDefinedKey(inputText, inputTextExample);
        params[INTERNAL_MESSAGE] = "The key was calculated: " + definedKey;
        return encryptText(inputText, -definedKey);
    }

    private static int countSubstringCoincidenceIgnoreCase(String string, String substring) {
        return (string.length() - string.toLowerCase().replace(substring.toLowerCase(), "").length()) /
                substring.length();
    }

    private int friquencyDefinedKey(String inputText, String inputTextExample) {

        //Завантажуємо додатковий файл із текстом, бажано — того самого автора і тієї самої стилістики.
        //Програма складає статистику входження символів у закодований текст і додатковий файл із текстом.
        //Підраховуємо для вісьми найвживаніших літер у вхідному і додатковому тексті
        // - відстань між цими буквами у алфавіті.
        // Тобто вираховуємо різницю між першою найвживанішою літерою у вхідному і у додатковому тексті,
        // між другою..., і так до восьмої. Зрозуміло, що значення різниці для цих пар літер
        // (від першої і до восьмої) може бути різним. Тому підраховуємо, яка різниця зустрічається частіше
        // - це і буде наш ключ, з яким ми будемо разкодовувати вхідний текст.

        //count the quantity of times each letter is used in input text
        List<Map.Entry<Character, Integer>> listInputText = countCharsInText(inputText);

        //count the number of uses of each letter in example text
        List<Map.Entry<Character, Integer>> listExampleText = countCharsInText(inputTextExample);

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

        //Для різних літер ця різниця може бути різною - тому вираховуємо найбільше співпадіння цих різниць
        // для різних пар літер
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

    private static List<Map.Entry<Character, Integer>> countCharsInText(String inputText) {

        int[] countCharsInputText = new int[ALPHABET.length()];

        inputText.chars()
                .filter(c -> ALPHABET.indexOf(c) != -1)
                .forEach(c -> countCharsInputText[ALPHABET.indexOf(c)]++);

        //create map of each char (key) and its calculated quantity of uses in input text
        Map<Character, Integer> inputTextMap = ALPHABET.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(c -> c, c -> countCharsInputText[ALPHABET.indexOf(c)]));

        //create sorted list of each char (key) and its calculated quantity of uses in input text
        List<Map.Entry<Character, Integer>> listInputText = inputTextMap.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());
        return listInputText;
    }
}
