package bgu.spl.mics;

import java.util.List;

public class SendEvent implements Event {
    private boolean send;
    private List<String> AgentsSerialNambers;
    private int duration;

    public void setSend(boolean send) {
        this.send = send;
    }

    public void setAgentsSerialNambers(List<String> agentsSerialNambers) {
        AgentsSerialNambers = agentsSerialNambers;
    }

    public List<String> getAgentsSerialNambers() {
        return AgentsSerialNambers;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public boolean getSend(){
        return send;
    }
}
