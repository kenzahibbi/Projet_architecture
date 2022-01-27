package fr.istycorp.archlog.archlog;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.istycorp.archlog.db.Database;
import fr.istycorp.archlog.modele.Timeslot;


@RestController
public class timeSlotControler {

	@GetMapping("/getTimeSlot")
	public List<Timeslot> fetchTimeSlot() throws SQLException {
		Database database = new Database();
		List<Timeslot> timeslots = new ArrayList<Timeslot>();
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			List<Map> timeslotsMaps = database.getAllRowTimeslot();
			for (Map timeslotMap : timeslotsMaps) {
				Timeslot timeslot = new Timeslot((Timestamp) timeslotMap.get("beginTime"),
						(Timestamp) timeslotMap.get("endingTime"));
				timeslot.id = (int) timeslotMap.get("id");
				timeslots.add(timeslot);
			}
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return timeslots;
	}
	
	@PostMapping("/addTimeSlot")
	public void addTimeSlot(@RequestBody HashMap timeSlot) {
		
		Database database = new Database();
		
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			System.out.print(timeSlot.get("begin"));
			
			database.saveObject("Timeslot", timeSlot);
			
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@DeleteMapping("/DeleteTimeSlot")
	public void deleteTimeSlot(@RequestBody int timeslot) {
		Database database = new Database();
		
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			try {
				database.deleteObject("timeSlot", timeslot);
			} catch (SQLException e) {
				
			}
			
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
