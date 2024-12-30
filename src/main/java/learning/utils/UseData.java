package learning.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

public class UseData {
    // private static String url_link =
    // "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m";
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static String dataString = "";
    private static String recousePath;
    private static String filepath;

    public UseData() {
        try {
            // GetData data = new GetData(url_link);
            GetData data = new GetData();
            data.run();
            filepath = data.getFilepath();
            recousePath = new File(filepath).getParent() + "/";
            retrieveJsonData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UseData ud = new UseData();
        JSONObject json = ud.retrieveJsonData();
        double averageTemperature = ud.averageTemperature(json);
        // System.out.println("Average temperature: " + averageTemperature);

        HashMap<String, Double> temperatureTimes = ud.mapTemperatureTimes(json);
        // System.out.println("Temperature times: " + temperatureTimes);
    }

    public JSONObject retrieveJsonData() {
        try {
            FileReader reader = new FileReader(filepath);
            DataReader dataReader = new DataReader();
            dataString = dataReader.read(new BufferedReader(reader));
            System.out.println("Successfully read the data.json file.");

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(dataString);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void WriteJsonFile(String data) {
        try {
            String filename = "temperature_map.json";
            FileWriter fileWrite = new FileWriter(recousePath + filename);
            fileWrite.write(data);
            fileWrite.close();
            System.out.println("Successfully wrote to temperature_map.json file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void temperatureMapToJson(HashMap<String, Double> temperatureTimes) {
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Double> entry : temperatureTimes.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }

        WriteJsonFile(json.toString());
    }

    public HashMap<String, Double> mapTemperatureTimes(JSONObject json) {
        HashMap<String, Double> temperatureTimes = new HashMap<>();

        // loop trhough Json and asign the times temperatures
        JSONObject hourlyData = (JSONObject) json.get("hourly");
        JSONArray temperatureArray = (JSONArray) hourlyData.get("temperature_2m");
        JSONArray timeArray = (JSONArray) hourlyData.get("time");

        int arraySize = temperatureArray.size();
        for (int i = 0; i < arraySize; i++) {
            double temperature = (double) temperatureArray.get(i);
            String time = timeArray.get(i).toString();

            temperatureTimes.put(time, temperature);
        }

        temperatureMapToJson(temperatureTimes);

        return temperatureTimes;
    }

    public double averageTemperature(JSONObject json) {
        JSONObject hourlyData = (JSONObject) json.get("hourly");
        JSONArray temperatureArray = (JSONArray) hourlyData.get("temperature_2m");
        int tempArraySize = temperatureArray.size();
        double[] temperatures = new double[tempArraySize];
        double sumOfTemperatures = 0;
        for (int i = 0; i < tempArraySize; i++) {
            temperatures[i] = (double) temperatureArray.get(i);
            sumOfTemperatures += temperatures[i];
        }

        sumOfTemperatures = Double.parseDouble(df.format(sumOfTemperatures));
        double averageTemperature = sumOfTemperatures / tempArraySize;

        return Double.parseDouble(df.format(averageTemperature));
    }

}
