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
import fr.istycorp.archlog.modele.*;
import fr.istycorp.archlog.modele.Timeslot;

@RestController
public class ReservationControler {
	
	@GetMapping("/getReservation")
	public List<Reservation> fetchReservations(){
		
		Database database = new Database();
		List<Reservation> reservations = new ArrayList<Reservation>();


		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			 
					List<Map> reservationsMaps = database.getAllRowReservation();

					// We got the reservations, which has the person's id, the room's id, and the
					// reservation's id
					// We have to get the objects to build the reservation object
					for (Map reservationMap : reservationsMaps) {
						Person person = Person.from(database.getPersonFromID((int) reservationMap.get("personID")));

						var timeslotMap = database.getTimeslotFromID((int) reservationMap.get("timeSlotID"));
						Timeslot timeslot = new Timeslot((Timestamp) timeslotMap.get("beginTime"),
								(Timestamp) timeslotMap.get("endingTime"));
						timeslot.id = (int) timeslotMap.get("id");

						var room = Room.from(database.getRoomFromID((int) reservationMap.get("roomID")));

						Reservation reservation = new Reservation(person, room, timeslot);
						reservation.id = (int) reservationMap.get("id");
						reservations.add(reservation);
					}
			
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reservations;
			
	}
	
	
	
	@PostMapping("/addResa")
	public void addReservation(@RequestBody Reservation resa) {
		Database database = new Database();


		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			
			database.saveObject("Reservation", resa.toHashMap());
			

			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	@PostMapping("/updateResa")
	public void updateReservation(@RequestBody Reservation resa) {
		
	}
	
	@DeleteMapping("/DeleteResa")
	public void deleteReservation(@RequestBody int reservation) {
		Database database = new Database();


		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			try {
				database.deleteObject("Reservation", reservation);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
