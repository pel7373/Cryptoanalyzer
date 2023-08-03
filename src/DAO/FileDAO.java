package DAO;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileDAO implements DAO {
    @Override
    public String readData(String source)  throws IOException {
        StringBuilder input = new StringBuilder();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(source), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            input.append(line);
            input.append(System.lineSeparator());
        }
        br.close();
        return input.toString().trim();
    }

    @Override
    public void writeData(String destination, String output)  throws IOException {
        PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(new File(destination), false), StandardCharsets.UTF_8));
        pw.write(output);
        pw.close();
    }
}
