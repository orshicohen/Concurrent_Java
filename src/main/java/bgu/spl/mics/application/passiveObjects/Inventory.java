package bgu.spl.mics.application.passiveObjects;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.pair;
import com.google.gson.*;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static class InventoryHolder {
		private static Inventory instance = new Inventory();
	}
	/**
     * Retrieves the single instance of this class.
     */
	private Inventory(){
		gadgets = new LinkedList<>();

	}
	public static Inventory getInstance() {
		return Inventory.InventoryHolder.instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for (String s : inventory)
			this.gadgets.add(s);
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		return gadgets.remove(gadget);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
//		List<pair<Integer, String>> inventoryList = new LinkedList<>();
//		int i = 0;
//		for (String Gadget : gadgets){
//			pair<Integer, String> pair = new pair(i,Gadget);
//			inventoryList.add(pair);
//			i++;
//		}
//			try	(FileOutputStream file = new FileOutputStream(filename);
//				ObjectOutput output = new ObjectOutputStream(file);) {
//				output.writeObject(inventoryList);
//			}catch (IOException e) { e.printStackTrace();
//			}
//		Map<Integer,String> inventoryMap = new HashMap<>();
//		int i = 0;
//		for (String Gadget : gadgets){
//			//pair<Integer, String> pair = new pair(i,Gadget);
//			inventoryMap.put(new Integer(i) , Gadget);
//			i++;
//		}
//			try	{
//				FileOutputStream file = new FileOutputStream(filename);
//				ObjectOutput output = new ObjectOutputStream(file); {
//				output.writeObject(inventoryMap);
//				output.close();
//				file.close();
//			}}catch (IOException e) { e.printStackTrace();
//			}
		Map<Integer,String> inventoryMap = new HashMap<>();
		int i = 0;
		for (String Gadget : gadgets){
			//pair<Integer, String> pair = new pair(i,Gadget);
			inventoryMap.put(new Integer(i) , Gadget);
			i++;
		}
		Gson gson = new Gson();
		String s = gson.toJson(inventoryMap);
		FileWriter writer = new FileWriter(filename);
		writer.write(s);
		writer.close();
		}

}
