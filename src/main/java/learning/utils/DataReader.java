package learning.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class DataReader {
    public DataReader() {
    }

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        System.out.println(dataReader.read(new BufferedReader(null)));
    }

    public String read(BufferedReader reader) {
        BufferedReader in = reader; // ???
        String inputLine;
        StringBuilder response = new StringBuilder(); // ???

        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }
}
