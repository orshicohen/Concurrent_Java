package bgu.spl.mics.application.passiveObjects;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	private String missionName;
	private List<String> serialAgentsNumbers;
	private String gadget;
	private int timeIssued;
	private int timeExpired;
	private int duration;

    /**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		return missionName;
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
	//	this.serialAgentsNumbers = new LinkedList<>();
		this.serialAgentsNumbers = serialAgentsNumbers;
		this.serialAgentsNumbers.sort(Comparator.naturalOrder());

		//List L = new LinkedList<String>;
		//List L=serialAgentsNumbers.sort(String::compareTo);
//		for (String s : serialAgentsNumbers) {
//			boolean flag = true;
//			if(this.serialAgentsNumbers.isEmpty())
//				this.serialAgentsNumbers.add(s);
//			else{
//				for (int i = 0 ; i<this.serialAgentsNumbers.size() && flag; i++) {
//					if (s.compareTo(serialAgentsNumbers.get(i) ) < 0) {
//						this.serialAgentsNumbers.li
//						flag = false;
//					}
//			}
//			}
//		}
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		return serialAgentsNumbers;
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
	this.gadget = gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		return gadget;
	}

    /**
     * Sets the time the mission was issued in time ticks.
     */
    public void setTimeIssued(int timeIssued) {
        this.timeIssued = timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in time ticks.
     */
	public int getTimeIssued() {
		return timeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
        this.timeExpired = timeExpired;
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return timeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return duration;
	}
}
