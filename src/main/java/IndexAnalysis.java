import com.github.javafaker.Faker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
public class IndexAnalysis {
    public static void main(String[] args)  throws IOException{
        Faker faker = new Faker();
        //task
        //{"name": "XXX", "bugs": [bug1, bug2], "developers": [{"programmer": "xxx"}, {"designer": "xxx"}]}
        JSONObject obj = new JSONObject();
        obj.put("Name", faker.name().fullName());
        JSONArray bugs = new JSONArray();
        String s = java.util.UUID.randomUUID().toString();
        s = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        bugs.add(s);
        s = java.util.UUID.randomUUID().toString();
        s = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        bugs.add(s);
        obj.put("bugs", bugs);
        JSONArray developers = new JSONArray();
        JSONObject programmer = new JSONObject();
        programmer.put("programmer", faker.name().fullName());
        developers.add(programmer);
        JSONObject designer = new JSONObject();
        designer.put("designer", faker.name().fullName());
        developers.add(designer);
        obj.put("developers", developers);
        try (FileWriter file = new FileWriter("Output.json")) {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        }
        //mongoimport /d project11 /c task Output.json
        //measure-command {mongo C:\Users\aclas\M001\hw4}
        //mongoimport --db Project9 --collection task --file Output2.json --jsonArray
    }
}
