package fr.uvsq.yuzu.models;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a range in time
 * 
 * @author Repain
 *
 */
public class Timeslot {

	/**
	 * The start date of the timeslot
	 */
	Timestamp beginDatetime;

	/**
	 * The end date of the timeslot
	 */
	Timestamp endDatetime;
	public int id;

	public Timeslot(Date begin, Date end) {
		this.beginDatetime = new Timestamp(begin.getTime());
		this.endDatetime = new Timestamp(end.getTime());
	}
	public Timeslot(Date begin, Date end, int id) {
		this.beginDatetime = new Timestamp(begin.getTime());
		this.endDatetime = new Timestamp(end.getTime());
		this.id = id;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timeslot(Timestamp begin, Timestamp end) {
		beginDatetime = begin;
		endDatetime = end;
	}

	/**
	 * Builds a timeslot from date strings and a format
	 * 
	 * Example: new Timeslot("16/04/2021 16:00", "16/04/2021 18:00", "dd/MM/yyyy
	 * HH:mm");
	 * 
	 * @param beginString
	 * @param endString
	 * @param dateFormat
	 * @throws ParseException
	 */
	public Timeslot(String beginString, String endString, String dateFormat) throws ParseException {
		Date begin = new SimpleDateFormat(dateFormat).parse(beginString);
		Date end = new SimpleDateFormat(dateFormat).parse(endString);
		beginDatetime = new Timestamp(begin.getTime());
		endDatetime = new Timestamp(end.getTime());
	}

	/**
	 * Returns an array of timeslots as strings, from 08:00 to 18:45, with 15
	 * minutes in between (08:00, 08:15, etc.)
	 * 
	 * @return
	 */
	public static List<String> generateTimeSlotsStrings() {
		List<String> timeslots = new ArrayList<String>();

		for (int h = 8; h != 19; h++) {
			for (int m = 0; m != 60; m += 15) {
				timeslots.add(String.format("%02d:%02d", h, m));
			}
		}

		return timeslots;
	}

	/**
	 * Assumption: date is the same for begin and end, but time no
	 */
	public String toString() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		return dateFormatter.format(beginDatetime) + " " + timeFormatter.format(beginDatetime) + " à "
				+ timeFormatter.format(endDatetime);
	}

	// Geters
	public Date getBegining() {
		return this.beginDatetime;
	}

	public Date getEnding() {
		return this.endDatetime;
	}

	// seters
	public void setBeginning(Timestamp begin) {
		this.beginDatetime = begin;
	}

	public void setEnding(Timestamp end) {
		this.endDatetime = end;
	}

	/**
	 * Returns a HashMap with the instance's attributes
	 * 
	 * @return HashMap
	 */
	public HashMap toHashMap() {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		hashmap.put("begin", (Comparable) beginDatetime);
		hashmap.put("end", (Comparable) endDatetime);

		return hashmap;
	}

}
