package bgu.spl.mics;

import java.util.List;

public class Result {
    private boolean agentAvailable;
    private boolean gadgetAvailable;
    private int Qtime;
    private int SerialM;
    private List<String> AgentsNames;

    public int getQtime() {
        return Qtime;
    }

    public int getSerialM() {
        return SerialM;
    }

    public List<String> getAgentsNames() {
        return AgentsNames;
    }


    public void setAgentsNames(List<String> agentsNames) {
        AgentsNames = agentsNames;
    }

    public void setQtime(int qtime) {
        Qtime = qtime;
    }

    public void setSerialM(int serialM) {
        SerialM = serialM;
    }

    public boolean isAgentAvailable(){
        return agentAvailable;
    }

    public void setAgentAvailable(boolean agentAvailable) {
        this.agentAvailable = agentAvailable;
    }

    public boolean isGadgetAvailable() {
        return gadgetAvailable;
    }

    public void setGadgetAvailable(boolean gadgetAvailable) {
        this.gadgetAvailable = gadgetAvailable;
    }
}


