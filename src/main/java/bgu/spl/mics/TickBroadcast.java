package bgu.spl.mics;

public class TickBroadcast implements Broadcast {

    int time=0;

    public TickBroadcast(int t){
        time=t;
    }


    public int getTime() {
        return time;
    }


}
