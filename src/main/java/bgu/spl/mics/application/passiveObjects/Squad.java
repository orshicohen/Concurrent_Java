package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Subscriber;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private boolean terminated =false;
	private Map<String, Agent> agents;
	private static class SingletonHolder {
		private static Squad instance = new Squad();
	}

	private Squad(){
		agents  = new HashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingletonHolder.instance;
	}

	public Map<String, Agent> getAgents() {
		return agents;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (int i=0 ; i<agents.length ; i++) {
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	/**
	 * Releases agents.
	 */

	public void releaseAgents(List<String> serials){
		for (String s : serials) {
			Agent a;
			synchronized (agents) {
				a = agents.get(s);
			}
			if (a != null){
				synchronized (a)  {
					a.release();
					a.notify();
				}
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {///check
//		if(serials==null&&time==-1) {
//			terminated = true;
//			synchronized (agents) {
//			}
//			for (Map.Entry<String, Agent> entry : agents.entrySet()) {
//				synchronized (entry.getValue()) {
//					entry.getValue().notifyAll();
//				}
//			}
//		}
//		else {
			Thread.sleep((long) time * 100);
			releaseAgents(serials);

	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) throws InterruptedException {
		for (String s : serials) {
			Agent a;
			synchronized (agents) {
				a = agents.get(s);
			}

			//synchronized (a) {
				if (a != null) {
					synchronized (a) {
						while (!a.isAvailable()&&terminated==false)
							a.wait();
						if(terminated)
							return false;
						a.acquire();


					}
				} else {
					releaseAgents(serials);
					return false;
				}
			//}
		}
		return true;
	}
    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> names = new LinkedList<>();
        for (String s : serials){
        	names.add(agents.get(s).getName());
		}
	    return names;
    }
}