package fr.uvsq.yuzu.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Person {

	String lastName;
	String firstName;
	int studentNumber;
	Date birthdate;
	public int id;

	/**
	 * Example: var person = new Person("Poutou", "Philippe", 1, new Date(1950, 1, 12));
	 * 
	 * @param lastName
	 * @param firstName
	 * @param studentNumber
	 * @param birthdate
	 */
	public Person(String lastName, String firstName, int studentNumber, Date birthdate) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.studentNumber = studentNumber;
		this.birthdate = birthdate;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Builds a Person object from a hash containing the fields values
	 * 
	 * @param fields
	 * @return
	 */
	public static Person from(Map<String, Comparable> fields) {
		Person p = new Person((String) fields.get("lastName"), (String) fields.get("firstName"),
				(int) fields.get("studentNumber"), (Date) fields.get("birthdate"));
		
		if (fields.containsKey("id")) {			
			p.id = (int) fields.get("id");
		}

		return p;
	}

	public String toString() {
		return fullName();
	}

	/**
	 * Used for debug purposes, returns a string containing all fields except id
	 * @return
	 */
	public String debug() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return String.format("last name: %s, first name: %s, student number: %s, birthdate: %s", lastName, firstName,
				studentNumber, formatter.format(birthdate));
	}

	/**
	 * Returns a string containing the last and first name
	 * 
	 * @return
	 */
	public String fullName() {
		return firstName + " " + lastName;
	}

	// Geters
	public String getLastName() {
		return this.lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public int getStudentNumber() {
		return this.studentNumber;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	// seters
	public void setName(String name) {
		this.lastName = name;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * Returns a HashMap with the instance's attributes
	 * 
	 * @return HashMap
	 */
	public HashMap toHashMap() {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		hashmap.put("lastName", this.lastName);
		hashmap.put("firstName", this.firstName);
		hashmap.put("studentNumber", this.studentNumber);
		hashmap.put("birthdate", this.birthdate);

		return hashmap;
	}
}
