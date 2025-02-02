import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GradingInterface {
    String FILEPATH = "";
    public GradingInterface() {
        //create json object
        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(new FileReader(FILEPATH));
            ReadData(json);
        }
        catch (IOException | ParseException e) {
            json = new JSONObject();
        }
    }

    void ReadData(@NotNull JSONObject json) {

    }

    void NewData(@NotNull JSONObject json) {

    }

    void WriteToFile(@NotNull JSONObject json) {
        try {
            FileWriter file = new FileWriter(FILEPATH);
            file.write(json.toJSONString());
            file.close();
        }
        catch (IOException e) {
            System.out.println("Could not write JSON file");
        }
    }
}
