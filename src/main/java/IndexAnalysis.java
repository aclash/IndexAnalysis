import com.github.javafaker.Faker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class IndexAnalysis {
    private static final int developersNum = 10000;
    private static String[] devsID = new String[developersNum];
    private static final int bugsNum = 20000;
    private static String[] bugsID = new String[bugsNum];
    private static final int tasksNum = 8000;
    private static String[] tasksID = new String[tasksNum];
    private static final int teamsNum = 100;
    private static String[] teamsID = new String[teamsNum];
    private static List<String> managerList = new ArrayList<>();
    private static List<String> devNameList = new ArrayList<>();

    private static String GetUUID(){
        String s = java.util.UUID.randomUUID().toString();
        s = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        return s;
    }

    private static void CreateIDs(){
        for (int i = 0; i < developersNum; ++i)
            devsID[i] = GetUUID();
        for (int i = 0; i < bugsNum; ++i)
            bugsID[i] = GetUUID();
        for (int i = 0; i < tasksNum; ++i)
            tasksID[i] = GetUUID();
        for (int i = 0; i < teamsNum; ++i)
            teamsID[i] = GetUUID();
    }

    private static void CreateDevelopers() throws IOException{
        //{id:ObjectID(), type:"XXX", name: "XXX"}
        Faker faker = new Faker();
        String[] devs = {"manager", "designer", "programmer", "artist"};
        Random rand = new Random();
        try (FileWriter file = new FileWriter("developers.json")) {
            for (int i = 0; i < developersNum; ++i){
                JSONObject obj = new JSONObject();
                obj.put("id", devsID[i]);
                int rdIndex = rand.nextInt(4);
                obj.put("type", devs[rdIndex]);
                if (rdIndex == 0)
                    managerList.add(devsID[i]);
                String temp_Name = faker.name().fullName();
                devNameList.add(temp_Name);
                obj.put("Name", temp_Name);
                obj.put("age", faker.number().numberBetween(22, 65));
                file.write(obj.toJSONString());
                file.write("\r\n");
            }
        }
    }

    private static void CreateBugs() throws IOException{
        //{id:ObjectID(), fixed by: [dev1, dev2], priority: "XXX", status: "XXX"}
        Faker faker = new Faker();
        String[] priority = {"S", "A", "B", "C"};
        String[] status = {"to do", "doing", "done"};
        Random rand = new Random();
        try (FileWriter file = new FileWriter("bugs.json")) {
            for (int i = 0; i < bugsNum; ++i){
                JSONObject obj = new JSONObject();
                obj.put("id", bugsID[i]);
                int rdIndex = rand.nextInt(4);
                obj.put("priority", priority[rdIndex]);
                rdIndex = rand.nextInt(3);
                obj.put("status", status[rdIndex]);
                JSONArray devs = new JSONArray();
                //randomly pick some developers to fix the bug
                int bugOwners = rand.nextInt(3) + 1;
                int offset = developersNum / bugOwners;
                for (int j = 0; j < bugOwners; ++j){
                    rdIndex = faker.number().numberBetween(j * offset, j * offset + offset - 1);
                    devs.add(devsID[rdIndex]);
                }
                obj.put("fixedBy", devs);
                file.write(obj.toJSONString());
                file.write("\r\n");
            }
        }
    }

    private static void CreateTasks() throws IOException{
        //{id:ObjectID(),hasBugs: [bug1, bug2], status: "XXX", manager: "XXX", budget: xxx}
        Faker faker = new Faker();
        String[] status = {"to do", "doing", "done"};
        Random rand = new Random();
        try (FileWriter file = new FileWriter("tasks.json")) {
            for (int i = 0; i < tasksNum; ++i){
                JSONObject obj = new JSONObject();
                obj.put("id", tasksID[i]);
                int rdIndex = rand.nextInt(3);
                obj.put("status", status[rdIndex]);
                rdIndex = rand.nextInt(managerList.size());
                obj.put("manager", managerList.get(rdIndex));
                obj.put("budget", faker.number().numberBetween(100, 10000));
                JSONArray bugs = new JSONArray();
                //randomly pick some bugs to the task
                int taskBugsNum = rand.nextInt(5) + 1;
                int offset = bugsNum / taskBugsNum;
                for (int j = 0; j < taskBugsNum; ++j){
                    rdIndex = faker.number().numberBetween(j * offset, j * offset + offset - 1);
                    bugs.add(bugsID[rdIndex]);
                }
                obj.put("hasBugs", bugs);
                file.write(obj.toJSONString());
                file.write("\r\n");
            }
        }
    }

    private static void CreateTeams() throws IOException{
        //{id:ObjectID(), developers: [dev1, dev2], tasks: [task1, task2], manager: "XXX"}
        Faker faker = new Faker();
        Random rand = new Random();
        try (FileWriter file = new FileWriter("teams.json")) {
            for (int i = 0; i < teamsNum; ++i){
                JSONObject obj = new JSONObject();
                obj.put("id", teamsID[i]);
                int rdIndex = rand.nextInt(managerList.size());
                obj.put("manager", managerList.get(rdIndex));
                //randomly pick some developers to the team
                JSONArray devs = new JSONArray();
                int rd_devsNum = faker.number().numberBetween(25, 45);
                int offset = developersNum / rd_devsNum;
                for (int j = 0; j < rd_devsNum; ++j){
                    rdIndex = faker.number().numberBetween(j * offset, j * offset + offset - 1);
                    devs.add(devsID[rdIndex]);
                }
                obj.put("developers", devs);
                //randomly pick some tasks to the team
                JSONArray tasks = new JSONArray();
                int rd_tasksNum = faker.number().numberBetween(10, 25);
                offset = tasksNum / rd_tasksNum;
                for (int k = 0; k < rd_tasksNum; ++k){
                    rdIndex = faker.number().numberBetween(k * offset, k * offset + offset - 1);
                    tasks.add(tasksID[rdIndex]);
                }
                obj.put("tasks", tasks);
                file.write(obj.toJSONString());
                file.write("\r\n");
            }
        }
    }

    public static void main(String[] args) throws IOException{
        //create ID
        CreateIDs();
        //create the collection of developers(generating developers.json)
        CreateDevelopers();
        //create the collection of bugs(generating bugs.json)
        CreateBugs();
        //create the collection of tasks(generating tasks.json)
        CreateTasks();
        //create the collection of teams(generating teams.json)
        CreateTeams();

        //create some scripts to run on the data
        Create_JS_File_Aggregate();
        Create_JS_File_Mix();
    }

    private static void Create_JS_File_Aggregate(){
        Faker faker = new Faker();
        int query_Num = 50;
        String[] devs = {"manager", "designer", "programmer", "artist"};
        try {
            FileWriter writer=new FileWriter("aggregate.js");
            writer.write("conn = new Mongo();\n" + "db = conn.getDB(\"IndexAnalysis\");\n");
            for (int i = 0; i < query_Num; ++i){
                int limitNum = faker.number().numberBetween(4, 12);
                int lowBound = faker.number().numberBetween(22, 50);
                int highBound = lowBound + 8;
                writer.write("result = db.bugs.aggregate([\n" +
                        "   {\n" +
                        "      $unwind: \"$fixedBy\"\n" +
                        "   },\n" +
                        "   {\n" +
                        "      $lookup:\n" +
                        "         {\n" +
                        "            from: \"developers\",\n" +
                        "            localField: \"fixedBy\",\n" +
                        "            foreignField: \"id\",\n" +
                        "            as: \"dever_docs\"\n" +
                        "        }\n" +
                        "   },\n" +
                        "   { $match: {\"dever_docs.type\": \"" + devs[i%4] + "\", \"dever_docs.age\": {$gte: " + lowBound + ", $lte: " + highBound + "} }},\n" +
                        "   { $project: { dever_docs: 1, _id:0}},\n" +
                        "   { $limit: " + limitNum + "}\n" +
                        "])\n" +
                        "while ( result.hasNext() ) {\n" +
                        "   printjson( result.next() );\n" +
                        "}\n\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void Create_JS_File_Mix(){
        Faker faker = new Faker();
        int query_Num = 50;
        try {
           FileWriter writer=new FileWriter("mix.js");
           writer.write("conn = new Mongo();\n" + "db = conn.getDB(\"IndexAnalysis\");\n");
           for (int i = 0; i < query_Num; ++i){
               String[] priority = {"S", "A", "B", "C"};
               String[] status = {"to do", "doing", "done"};
               int lowBound = faker.number().numberBetween(100, 9700);
               int highBound = lowBound + 200;
               int limitNum = faker.number().numberBetween(4, 12);
               writer.write("result = db.tasks.aggregate([\n" +
                       "   {\n" +
                       "      $unwind: \"$hasBugs\"\n" +
                       "   },\n" +
                       "   {\n" +
                       "      $lookup:\n" +
                       "         {\n" +
                       "            from: \"bugs\",\n" +
                       "            localField: \"hasBugs\",\n" +
                       "            foreignField: \"id\",\n" +
                       "            as: \"bugs_docs\"\n" +
                       "        }\n" +
                       "   },\n" +
                       "   { $match: {\"bugs_docs.priority\": \"" + priority[i%4] + "\" }},\n" +
                       "   { $project: { bugs_docs: 1, _id:0}},\n" +
                       "   { $limit: " + limitNum + "}\n" +
                       "])\n" +
                       "while ( result.hasNext() ) {\n" +
                       "   printjson( result.next() );\n" +
                       "}\n\n");
               writer.write("db.tasks.update({\"manager\": " + managerList.get(i % managerList.size()) + "}, { $set : {'hasBugs': [], \"status\": " + status[i % 3] + "}})\n\n");
               writer.write("result = db.tasks.find({ budget: {$gte: " + lowBound + ", $lte: " + highBound + "}}).limit(" + limitNum + ");\n" +
                       "while ( result.hasNext() ) {\n" +
                       "   printjson( result.next() );\n" +
                       "}\n\n");
           }
           writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

