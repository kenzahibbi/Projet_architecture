package fr.uvsq.yuzu.models;

import java.util.HashMap;

/**
 * The Reservation class represents a reservation of a classroom by a responsable
 * for a given time
 * 
 * @author Repain
 *
 */
public class Reservation {

	/**
	 * The person responsible for the reservation
	 */
	Person person;
	
	/**
	 * The classroom reserved
	 */
	Room room;
	
	/**
	 * The time the room is reserved
	 */
	Timeslot timeslot;
	public int id;

	public Reservation(Person person, Room room, Timeslot timeslot) {
		this.person = person;
		this.room = room;
		this.timeslot = timeslot;
	}
	public Reservation(Person person, Room room, Timeslot timeslot, int id) {
		this.person = person;
		this.room = room;
		this.timeslot = timeslot;
		this.id	= id;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	// Geters
	public Person getPerson() {
		return this.person;
	}

	public Room getRoom() {
		return this.room;
	}

	public Timeslot getTimeslot() {
		return this.timeslot;
	}

	// seters
	public void setPerson(Person person) {
		this.person = person;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setTimeslot(Timeslot timeslot) {
		this.timeslot = timeslot;
	}

	/**
	 * Returns a HashMap with the instance's attributes
	 * 
	 * @return HashMap
	 */
	public HashMap toHashMap() {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		hashmap.put("person", this.person.id);
		hashmap.put("room", this.room.id);
		hashmap.put("timeslot", this.timeslot.id);

		return hashmap;
	}
}
