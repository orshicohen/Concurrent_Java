package bgu.spl.mics.application;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    private static JsonParser parser;
    private static Reader reader;
    private static JsonElement rootElement;
    private static JsonObject rootObjects;
    private static JsonArray inventoryArray;
    private static JsonArray intelligenceArray;
    private static JsonObject servicesObjects;
    private static JsonArray squadArray;
    private static LinkedList<Thread> threadList;
    private static int duration;
    private static Thread timeServiceThread;
    private static Inventory inventory;
    private static Squad squad;
    private static LinkedList<Intelligence> intelligences = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        InputStream inputStream;
        parser = new JsonParser();
        try {
            inputStream = new FileInputStream(args[0]);
            reader = new InputStreamReader(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rootElement = parser.parse(reader);
        rootObjects = rootElement.getAsJsonObject();
        inventoryArray = rootObjects.getAsJsonArray("inventory");
        threadList = new LinkedList<>();
        CreateInventory();
        servicesObjects = rootObjects.getAsJsonObject("services");
        int NumOfM = servicesObjects.get("M").getAsInt();
        for (int i=0 ; i<NumOfM; i++){
            M m = new M(i);
            Thread thread = new Thread(m);
            threadList.addLast(thread);
            thread.start();
        }
        int NumOfMoneyPenny =servicesObjects.get("Moneypenny").getAsInt();
        for (int i=0 ; i<NumOfMoneyPenny; i++){
            Moneypenny m = new Moneypenny(i);
            Thread thread = new Thread(m);
            threadList.addLast(thread);
            thread.start();
        }
        intelligenceArray = servicesObjects.getAsJsonArray("intelligence");
        //int NumOfIntelligence =intelligenceArray.size();
        CreateIntelligenceSubscribers();
        squadArray = rootObjects.getAsJsonArray("squad");
        CreateSquad();
        CreateTimeService();
        timeServiceThread.start();
        for (Thread thread : threadList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        inventory.printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);

    }

    private static void CreateInventory() {
        inventory = Inventory.getInstance();
        String[] Gadgets = new String[inventoryArray.size()];
        int i = 0;
        for (JsonElement gadget : inventoryArray) {
            Gadgets[i]=gadget.getAsString();
            i++;
        }
        inventory.load(Gadgets);
        Q q = new Q();
        Thread thread = new Thread(q);
        threadList.addLast(thread);
        thread.start();
    }

    private static void CreateTimeService(){
        int duration = servicesObjects.get("time").getAsInt();
        TimeService timeService = new TimeService(duration);
        timeServiceThread = new Thread(timeService);
        threadList.addFirst(timeServiceThread);
    }

    private static void CreateIntelligenceSubscribers(){
        Thread[] IntelligenceThread = new Thread[intelligenceArray.size()];
        for (int i = 0 ; i<intelligenceArray.size();i++) {
            LinkedList<MissionInfo> ListMissions = new LinkedList<>();
            JsonObject Missionsjason = intelligenceArray.get(i).getAsJsonObject();
            JsonArray MissionsJason = Missionsjason.get("missions").getAsJsonArray();
            for(int j = 0;j<MissionsJason.size();j++) {
            MissionInfo MI = new MissionInfo();
                JsonObject m = MissionsJason.get(j).getAsJsonObject();
                JsonArray k = m.getAsJsonArray("serialAgentsNumbers");
                List<String> serial = new LinkedList<>();
                for (JsonElement elem : k){
                    serial.add(elem.getAsString());
                }
//                for(int m=0; m<k.size() ; m++){
//                    serial.add(k[m])
//                }
                MI.setSerialAgentsNumbers(serial);
                MI.setDuration(m.get("duration").getAsInt());
                MI.setGadget(m.get("gadget").getAsString());
                if(m.get("name")!=null)
                    MI.setMissionName(m.get("name").getAsString());
                else
                    MI.setMissionName(m.get("missionName").getAsString());
                MI.setTimeExpired(m.get("timeExpired").getAsInt());
                MI.setTimeIssued(m.get("timeIssued").getAsInt());
                ListMissions.addLast(MI);
        }
            Intelligence intelligence = new Intelligence(i);
            IntelligenceThread[i] = new Thread(intelligence);
            intelligence.addMissions(ListMissions);
            intelligences.addLast(intelligence);
            threadList.addLast(IntelligenceThread[i]);
            IntelligenceThread[i].start();
        }
        }

    private static void CreateSquad(){
        squad = Squad.getInstance();
        Agent [] agents = new Agent[squadArray.size()];
        for(int i=0 ; i<squadArray.size() ; i++){
            JsonObject agent = squadArray.get(i).getAsJsonObject();
            Agent a = new Agent();
            a.setName(agent.get("name").getAsString());
            a.setSerialNumber(agent.get("serialNumber").getAsString());
            agents[i] = a;
        }
        squad.getInstance().load(agents);
    }
    }
