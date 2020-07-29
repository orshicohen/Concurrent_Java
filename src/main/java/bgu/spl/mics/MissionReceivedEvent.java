package bgu.spl.mics;

import java.util.List;

public class MissionReceivedEvent implements Event{
    private String Name;
    private List<String> AgentsSerials;

    private String Gadget;
    private int duration;
    private int timeIssued;
    private int timeExpired;

    public MissionReceivedEvent(String Name, List<String> Agents, String Gadget, int timeIssued,int timeExpired,int duration){
        this.Name = Name;
        this.AgentsSerials = Agents;
        this.Gadget = Gadget;
        this.timeIssued = timeIssued;
        this.timeExpired = timeExpired;
        this. duration=duration;
    }


    public int getDuration() {
        return duration;
    }

    public String getName() {
        return Name;
    }

    public List<String> getAgentsSerials() {
        return AgentsSerials;
    }

    public String getGadget() {
        return Gadget;
    }

    public int getTimeIssued() {
        return timeIssued;
    }

    public int getTimeExpired() {
        return timeExpired;
    }
}
