package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private  long currenttime =0;
	private int serialNumber;

	public M() {
		super("M");
	}


	public M(int serialNum) {
		super("M"+ serialNum);
		serialNumber=serialNum;
	}

	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);
		this.subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent x)-> {
			Diary.getInstance().incrementTotal();
			AgentsAvailableEvent AgentsEvent = new AgentsAvailableEvent(x.getAgentsSerials());
			Future AgentsFuture = MessageBrokerImpl.getInstance().sendEvent(AgentsEvent);
			//Pair< Pair<Boolean,Integer>, List<String>> b =(Pair< Pair<Boolean,Integer>, List<String>>)(AgentsFuture.get());
			Result resultMP=null;
			if(AgentsFuture!=null) {
				resultMP = (Result) AgentsFuture.get();
				if (resultMP==null)
					return;
		//		System.out.println("M " + serialNumber + " moneypenny " + resultMP.getSerialM() + " did " + x.getName() + " result " + resultMP.isAgentAvailable());

			if(resultMP.isAgentAvailable() == true)
			{
				GadgetAvailableEvent GadgetEvent = new GadgetAvailableEvent(x.getGadget());
				Future GadgetFuture = MessageBrokerImpl.getInstance().sendEvent(GadgetEvent);
				//TODO CHECK IF WE HAVE TO ADD IF ABOUT FUTURE==NULL
			//	Pair<Boolean,Integer> a =(Pair<Boolean,Integer>)(GadgetFuture.get());
				Result resultQ=null;
				if(GadgetFuture!=null) {
					resultQ = (Result) GadgetFuture.get();
					//	System.out.println("qtime  " + resultQ.getQtime());
//					System.out.println("M " + serialNumber + "checked gadget " + x.getGadget() + " and its: " + resultQ.isGadgetAvailable() + " for mission " + x.getName());
				}
				SendEvent send = new SendEvent();
				if(resultQ !=null && resultQ.isGadgetAvailable()==true) {
					if(resultQ.getQtime()<x.getTimeExpired())
					{
						send.setSend(true);
					//	System.out.println("M " + serialNumber + "sent mission succses " + " mission " +x.getName() );
						send.setDuration(x.getDuration());
						send.setAgentsSerialNambers(x.getAgentsSerials());
						MessageBrokerImpl.getInstance().sendEvent(send); //check about Future
						Report R = new Report();
						R.setMissionName(x.getName());
						R.setM(serialNumber);
						R.setMoneypenny(resultMP.getSerialM());
						R.setAgentsSerialNumbers(x.getAgentsSerials());
						R.setAgentsNames(resultMP.getAgentsNames());
						R.setGadgetName(x.getGadget());
						R.setQTime(resultQ.getQtime());
						R.setTimeIssued(x.getTimeIssued());
						R.setTimeCreated((int)currenttime);
						Diary.getInstance().addReport(R);
					}
					else
					{
						send.setSend(false);
						send.setAgentsSerialNambers(x.getAgentsSerials());
						MessageBrokerImpl.getInstance().sendEvent(send); //check about Future
						//System.out.println("M " + serialNumber + "sent mission failed Time Expired " + " mission " +x.getName() );
					}
				}
				else{
					send.setSend(false);
					send.setAgentsSerialNambers(x.getAgentsSerials());
					MessageBrokerImpl.getInstance().sendEvent(send); //check about Future
					//System.out.println("M " + serialNumber + "sent mission failed No Gadget " + " mission " +x.getName() );
				}
			}

		}});
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast x)-> {
			currenttime=x.getTime();
		});
		this.subscribeBroadcast(TerminatedEventBroadcast.class, (TerminatedEventBroadcast x)-> {terminate();
	///		System.out.println("terminate M "+ serialNumber);
		}); //TODO:ADD THIS
	}

}
