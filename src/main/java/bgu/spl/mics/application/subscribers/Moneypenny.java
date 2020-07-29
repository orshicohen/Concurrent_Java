package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Boolean terminated=false;
	private Integer serialNumber;

	public Moneypenny() {
		super("Moneypenny");

	}
	public Moneypenny(int serialNumber) {
		super("Moneypenny" + serialNumber);
		this.serialNumber = serialNumber;
	}

	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);
		if(serialNumber%2==0) {
			this.subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent x) -> {
				///System.out.println("MoneyPenny " +serialNumber+ " got mission");

				Boolean result = Squad.getInstance().getAgents(x.getAgentsSerials());
				//Pair<Pair<Boolean, Integer>, List<String>> a = new Pair(new Pair(result, serialNumber), L);
				Result r = new Result();
				r.setAgentAvailable(result);
				if(result==true) {
					List L = Squad.getInstance().getAgentsNames(x.getAgentsSerials());
					r.setAgentsNames(L);
				}
				r.setSerialM(serialNumber);
		///		System.out.println("MoneyPeeny " + serialNumber + "checked agants" + result);
				MessageBrokerImpl.getInstance().complete(x, r);


			});
		}else{
			this.subscribeEvent(SendEvent.class, (SendEvent x)->{
				///List L = Squad.getInstance().getAgentsNames(x.getAgentsSerialNambers());
			Squad.getInstance().sendAgents(x.getAgentsSerialNambers(),x.getDuration());
		//		System.out.println("MP "+serialNumber + " sent agants " +x.getSend());
			});
		}

		this.subscribeBroadcast(TerminatedEventBroadcast.class, (TerminatedEventBroadcast x)-> {terminate();
		Map<String, Agent> agentsMap = Squad.getInstance().getAgents();
        List<String> agentsList = new LinkedList<>();
        for (Map.Entry<String , Agent> entry : agentsMap.entrySet()) {
			agentsList.add(entry.getKey());
        }
        Squad.getInstance().setTerminated(true);
        Squad.getInstance().releaseAgents(agentsList);
       // System.out.println("MoneyPenny " +serialNumber+ " terminated");
		});

		
	}

	public Boolean getTerminated() {
		return terminated;
	}

	public void setTerminated(Boolean terminated) {
		this.terminated = terminated;
	}
}
