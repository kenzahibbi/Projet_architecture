package fr.istycorp.archlog.archlog;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istycorp.archlog.db.Database;
import fr.istycorp.archlog.modele.Person;
import org.apache.commons.lang.StringEscapeUtils;

@RestController
public class PersonControler {

	@GetMapping("/personList")
	public List<Person> fetchPeople() throws SQLException {

		List<Person> people = new ArrayList<Person>();
		Database database = new Database();
		
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			List<Map> peopleMaps = database.getAllRowPerson();
			for (Map personMap : peopleMaps) {
				Person p = Person.from(personMap);
				people.add(p);
			}
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
			
		return people;
	}
	
	@PostMapping("/setPerson")
	public void addPerson(@RequestBody Person pers) {
		
		Database database = new Database();

		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);

			database.saveObject("Person", pers.toHashMap());
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@PostMapping("/setPersonWithQuery")
	public void setPersQuery(@RequestBody String query) {

		Database database = new Database();

		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			System.out.print(StringEscapeUtils.unescapeJava(query) + "\n");

			
			Database.executeQuery(database.conn , StringEscapeUtils.unescapeJava(query));

			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@DeleteMapping("/DeletePerson")
	public void deletePerson(@RequestBody int person) {
		Database database = new Database();

		try {
			database.conn = Database.getConnection();
			//Check if db is create
			Database.fillDB(database.conn);
			try {
				database.deleteObject("Person", person);
			} catch (SQLException e) {
				System.err.println("Supprimer la reservatio qui utilise cette objet");

			}
			
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
