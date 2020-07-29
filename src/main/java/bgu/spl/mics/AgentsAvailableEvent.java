package bgu.spl.mics;

import java.util.List;

public class AgentsAvailableEvent implements Event {
    private List<String> AgentsSerials;

    public AgentsAvailableEvent(List<String> AgentsSerials){
        this.AgentsSerials = AgentsSerials;
    }

    public List<String> getAgentsSerials(){
        return AgentsSerials;
    }
}
