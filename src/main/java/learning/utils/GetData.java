package learning.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GetData {
    private static String url_link = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m";
    private static final String FILEPATH = System.getProperty("user.dir") + "/src/main/resources/data.json";

    public GetData() {
    }

    // Constructor with custom URL
    public GetData(String url) {
        if (url != null && !url.isEmpty()) {
            url_link = url;
        }
    }

    public String getFilepath() {
        return FILEPATH;
    }

    private static HttpURLConnection establishConnection() {
        try {
            URI uri = new URI(url_link);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configure connection
            connection.setRequestMethod("GET"); // Use "POST", "PUT", etc., as needed
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Java-HttpClient");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return connection;
            } else {
                System.out.printf("Connection failed, error code: %d%n", responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeToFile(String data) {
        try {
            File file = new File(FILEPATH);
            file.getParentFile().mkdirs(); // ensure that the parent directory exists
            file.createNewFile();
            FileWriter myWriter = new FileWriter(FILEPATH);
            // System.out.println("Writing to file: " + FILEPATH);
            System.out.println("file path:" + file.getAbsolutePath());
            // System.out.println("file:" + file);
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to data.json file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // Establish connection
            HttpURLConnection connection = establishConnection();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            DataReader dataReader = new DataReader();
            String data = dataReader.read(new BufferedReader(inputStreamReader));

            // Write to JSON
            writeToFile(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GetData data = new GetData(url_link);
        data.run();
    }
}
