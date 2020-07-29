package bgu.spl.mics;

public class GadgetAvailableEvent implements Event {
    private String Gadget;

    public GadgetAvailableEvent(String Gadget){
        this.Gadget = Gadget;
    }

    public String getGadget() {
        return Gadget;
    }
}

