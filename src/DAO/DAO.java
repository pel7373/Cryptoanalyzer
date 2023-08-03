package DAO;

import java.io.IOException;

public interface DAO {
    String readData(String source)  throws IOException;
    void writeData(String destination, String output) throws IOException;
}
