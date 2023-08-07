package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyModel implements Model {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,\":-!? ";
    private String[] params;

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
        params[6] = "Зсув: " + counterCoincidenceMaxShift + "; співпадінь зі словником: "
                + counterCoincidenceMax + "; на 2-му місці співпадінь " + counterCoincidenceMax2;
        return encryptText(inputEncryptedText, -counterCoincidenceMaxShift);
    }

    @Override
    public String DecryptionByStatistics(String inputText, String inputTextExample) {
        int definedKey = friquencyDefinedKey(inputText, inputTextExample);
        params[6] = "The key was calculated: " + definedKey;
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
}
