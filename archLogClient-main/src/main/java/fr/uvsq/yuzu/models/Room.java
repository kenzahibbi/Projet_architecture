package fr.uvsq.yuzu.models;

import java.util.HashMap;
import java.util.Map;


/**
 * A classroom
 * 
 * @author Repain
 *
 */
public class Room {

	String name;
	public int id;

	public Room(String name) {
		this.name = name;
	}


	public Room(String name, int id) {
		this.name = name;
		this.id = id;
	}
	/**
	 * Creates a room from a hash, containing the attributes TODO doesn't check if
	 * hash contains keys (id, roomName)
	 * 
	 * @param roomMap
	 * @return
	 */
	public static Room from(Map roomMap) {
		var room = new Room((String) roomMap.get("roomName"));
		room.id = (int) roomMap.get("id");
		return room;
	}

	public String toString() {
		return name;
	}

	// geters
	public String getName() {
		return this.name;
	}

	// seters
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a HashMap with the instance's attributes
	 * 
	 * @return HashMap
	 */
	public HashMap toHashMap() {
		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		hashmap.put("name", this.name);

		return hashmap;
	}
}