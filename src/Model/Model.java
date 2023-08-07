package Model;

import java.util.List;

public interface Model {
    String encryptText(String inputText, int shift);
    String decryptText(String inputText, int shift);
    String bruteForce(String inputEncryptedText, List<String> commonWords);
    String DecryptionByStatistics(String inputText, String inputTextExample);
}
