package Model;

import java.io.IOException;

public interface Model {
    String encryptText(String inputText, int shift);
    String decryptText(String inputText, int shift);
    String bruteForce(String inputEncryptedText, String commonWordsFileName) throws IOException;
    String DecryptionByStatistics(String inputText, String fileExampleName) throws IOException;
}
