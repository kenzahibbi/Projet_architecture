package fr.istycorp.archlog.archlog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.istycorp.archlog.db.Database;
import fr.istycorp.archlog.modele.Room;

@RestController
public class RoomControler {

	@GetMapping("/getRoomList")
	public List<Room> fetchRoom() throws SQLException {
		Database database = new Database();
		List<Room> rooms;
		rooms = new ArrayList<Room>();
		
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			
			List<Map> roomsMaps = database.getAllRowRoom();
			for (Map roomMap : roomsMaps) {
				Room r = new Room((String) roomMap.get("roomName"));
				r.id = (int) roomMap.get("id");
				rooms.add(r);
			}
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rooms;
	}
	
	
	@PostMapping("/setRoom")
	public void addRoom(@RequestBody Room room) {
		Database database = new Database();
		
		try {
			database.conn = Database.getConnection();
			Database.fillDB(database.conn);
			database.saveObject("Room", room.toHashMap());
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@DeleteMapping("/DeleteRoom")
	public void deleteRoom(@RequestBody int room) {
		Database database = new Database();
		
		try {
			database.conn = Database.getConnection();
			try {
				database.deleteObject("Room", room);
			} catch (SQLException e) {
				
			}
			
			// Database Disconnection
			database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
