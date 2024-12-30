package learning;

import learning.utils.UseData;

import org.json.simple.JSONObject;

public class App {
    public static void main(String[] args) {

        // implement different path files
        UseData ud = new UseData();
        JSONObject json = ud.retrieveJsonData();
        double averageTemperature = ud.averageTemperature(json);
        ud.mapTemperatureTimes(json);

        System.out.println("Average temperature: " + averageTemperature);
    }
}