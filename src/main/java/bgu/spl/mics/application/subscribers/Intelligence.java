package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {


	private int serialNumber;
	private int currenttime=0;
    private LinkedList<MissionInfo> Missions;

	public Intelligence() {
		super("Intelligence");
	}


	public Intelligence(int serialNum) {
		super("Intelligence"+ serialNum);
		serialNumber=serialNum;
		Missions=new LinkedList<>();
	}

	public void addMissions( LinkedList<MissionInfo> missions){
		for (MissionInfo m : missions){
			Missions.addLast(m);
		}
	}


	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast x)-> {
			currenttime=x.getTime();
			while(!Missions.isEmpty() && Missions.get(0).getTimeIssued()==currenttime){
					MissionInfo m = Missions.remove(0);
					MissionReceivedEvent event=new MissionReceivedEvent(m.getMissionName(),m.getSerialAgentsNumbers(), m.getGadget(),m.getTimeIssued(),m.getTimeExpired(),m.getDuration());
					MessageBrokerImpl.getInstance().sendEvent(event);
				///	System.out.println(this.serialNumber + " intel: send event" + m.getMissionName() );

		}});
		this.subscribeBroadcast(TerminatedEventBroadcast.class, (TerminatedEventBroadcast x)-> {terminate();
		///	System.out.println("intel "+serialNumber +" terminated");

		}); //TODO:ADD THIS



	}
}
