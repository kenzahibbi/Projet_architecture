package fr.istycorp.archlog.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


public class Database {

	public Connection conn;

	/** FUNCTIONS **/

	/**
	 * Establishes connection to database and returns the connection
	 * 
	 * @return Connection
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {

		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/classRessources";
			String username = "root";
			String password = "qwerty";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");

			return conn;

		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Ends connection with database
	 * 
	 * @throws SQLException
	 */
	public void deconnection() throws SQLException {
		this.conn.close();
		System.out.println("Disconnected from database.");
	}

	/**
	 * Executes SQL query and returns result
	 * 
	 * @param Connection conn
	 * @param String     query
	 * @throws SQLException
	 * @return ResultSet rs
	 */
	public static ResultSet getQueryResult(Connection conn, String query) throws SQLException {
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(query);

		return rs;
	}

	/**
	 * Executes SQL query without returning result
	 * 
	 * @param Connection conn
	 * @param String     query
	 */
	public static void executeQuery(Connection conn, String query) throws SQLException {
		Statement stm = conn.createStatement();
		boolean res = ((java.sql.Statement) stm).execute(query);
	}

	/**
     * Creates tables in database
     * 
     * @param Connection conn
     * @throws Exception
     */
    public static void fillDB(Connection conn) throws Exception {

        String[] queryArray = new String[5];

        queryArray[0] = "USE classRessources;";

        queryArray[1] = "CREATE TABLE IF NOT EXISTS classRessources.Person" + "(id int(5) PRIMARY KEY AUTO_INCREMENT, "
                + "lastName char(50)," + "firstName char(50)," + "studentNumber int(8),"
                + "birthDate DATE NOT NULL DEFAULT (DATE(CURRENT_TIMESTAMP)))" + "ENGINE=InnoDB;";

        queryArray[2] = "CREATE TABLE IF NOT EXISTS classRessources.Room" + "(id int(5) PRIMARY KEY AUTO_INCREMENT, "
                + "roomName char(20))" + "ENGINE=InnoDB;";

        queryArray[3] = "CREATE TABLE IF NOT EXISTS classRessources.timeSlot" + "(id int(5) PRIMARY KEY AUTO_INCREMENT, "
                + "beginTime DATETIME," + "endingTime DATETIME)" + "ENGINE=InnoDB;";

        queryArray[4] = "CREATE TABLE IF NOT EXISTS classRessources.Reservation" + "(id int(5) PRIMARY KEY AUTO_INCREMENT,"
                + "personID int(5)," + "roomID int(5) ," + "timeSlotID int(5),"
                + "constraint fk_p foreign key (personID) references Person(id),"
                + "constraint fk_r foreign key (roomID) references Room(id),"
                + "constraint fk_t foreign key (timeSlotID) references timeSlot(id))" + "ENGINE=InnoDB;";
        try {
            for (int i = 0; i < queryArray.length; i++) {
                executeQuery(conn, queryArray[i]);
            }
            System.out.println("Database Initialized");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

	/**
	 * Saves given object into database
	 * 
	 * @param String  type
	 * @param HashMap hashmap
	 * @throws SQLException
	 */
	public void saveObject(String type, HashMap hashmap) throws SQLException {

		String query = null;
		

		switch (type) {

		case "Person":
			java.util.Date utilDate = (Date) hashmap.get("birthdate");
		    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			query = "INSERT INTO Person (lastName ,firstName,studentNumber,birthDate) VALUES ('"
					+ hashmap.get("lastName") + "','" + hashmap.get("firstName") + "','" + hashmap.get("studentNumber")
					+ "','" + sqlDate + "');";
			executeQuery(this.conn, query);
			break;

		case "Reservation":
			query = "INSERT INTO Reservation (personID, roomID, timeSlotID) " + "VALUES ('" + hashmap.get("person")
					+ "'," + "'" + hashmap.get("room") + "'," + "'" + hashmap.get("timeslot") + "');";
			executeQuery(this.conn, query);
			break;

		case "Room":
			query = "INSERT INTO Room (roomName) VALUES ('" + hashmap.get("name") + "');";
			executeQuery(this.conn, query);
			break;

		case "Timeslot":
			query = "INSERT INTO timeSlot (beginTime,endingTime) VALUES (?, ?)";
			
			PreparedStatement ps = conn.prepareStatement(query);

            ps.setTimestamp(1, Timestamp.valueOf(hashmap.get("begin").toString() + ":00"));
            ps.setTimestamp(2, Timestamp.valueOf(hashmap.get("end").toString() + ":00"));
            ps.executeUpdate();

			break;

		default:
			;

		}
		System.out.println("Object saved Successfully.");
		return;

	}

	/**
	 * Returns object's id in database
	 * 
	 * @param String  type
	 * @param HashMap hashmap
	 * @throws SQLException
	 * @return int id
	 */
	public int getSQLID(String type, HashMap hashmap) throws SQLException {

		String query = null;
		ResultSet id;

		switch (type) {

		case "Person":
			java.util.Date utilDate = (Date) hashmap.get("birthdate");
		    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			query = "SELECT id FROM Person WHERE lastName = '" + hashmap.get("lastName") + "' AND firstName = '"
					+ hashmap.get("firstName") + "'AND studentNumber = '" + hashmap.get("studentNumber")
					+ "'AND birthDate = '" + sqlDate + "';";
			break;

		case "Reservation":
			query = "SELECT id FROM Reservation WHERE personID='" + hashmap.get("person") + "' AND roomID= '"
					+ hashmap.get("room") + "' AND timeSlotID='" + hashmap.get("timeslot") + "';";
			break;

		case "Room":
			query = "SELECT id FROM Room WHERE roomName ='" + hashmap.get("name") + "';";
			break;

		case "Timeslot":
			java.util.Date slotStartUtil = (Date) hashmap.get("begin");
		    java.sql.Date slotStartSql = new java.sql.Date(slotStartUtil.getTime());
			
		    java.util.Date slotEndUtil = (Date) hashmap.get("begin");
		    java.sql.Date slotEndSql = new java.sql.Date(slotEndUtil.getTime());
			query = "SELECT id FROM timeSlot WHERE beginTime = '" + slotStartSql + "' AND "
					+ "endingTime = '" + slotEndSql + "';";
			break;

		default:
			;

		}
		id = getQueryResult(this.conn, query);
		if (id.next()) {
			int res = ((Number) id.getObject(1)).intValue();
			id.close();
			return res;
		} else
			return 0;

	}

	/**
	 * Deletes object in database
	 * 
	 * @param String type
	 * @param int    id
	 * @throws SQLException
	 */
	public void deleteObject(String type, int id) throws SQLException {
		
		if(type == "Timeslot") {
			type = "timeSlot";
		}

		String query = "DELETE FROM " + type + " WHERE id=" + id + ";";
		System.out.print(query);
		executeQuery(this.conn, query);
		System.out.println("Object deleted Successfully");
	}

	/**
	 * Edits given object in database
	 * 
	 * @param String  type
	 * @param int     id
	 * @param HashMap hashmap
	 * @throws SQLException
	 */
	public void editObject(String type, int id, HashMap hashmap) throws SQLException {

		String query = null;
		switch (type) {
		case "Person":
			
			java.util.Date utilDate = (Date) hashmap.get("birthdate");
		    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			query = "UPDATE Person SET lastName = '" + hashmap.get("lastName") + "',firstName = '"
					+ hashmap.get("firstName") + "',studentNumber = '" + hashmap.get("studentNumber")
					+ "',birthDate = '" + sqlDate + "' WHERE id=" + id + ";";
			break;
		case "Reservation":
			query = "UPDATE Reservation SET personID='" + hashmap.get("person") + "', roomID= '" + hashmap.get("room")
					+ "',timeSlotID='" + hashmap.get("timeslot") + "'WHERE id=" + id + ";";
			break;
		case "Room":
			query = "UPDATE Room SET roomName ='" + hashmap.get("name") + "' WHERE id=" + id + ";";
			break;
		case "Timeslot":
			
			java.util.Date slotStartUtil = (Date) hashmap.get("begin");
		    java.sql.Date slotStartSql = new java.sql.Date(slotStartUtil.getTime());
			
		    java.util.Date slotEndUtil = (Date) hashmap.get("begin");
		    java.sql.Date slotEndSql = new java.sql.Date(slotEndUtil.getTime());
		    
			query = "UPDATE timeSlot SET beginTime = '" + slotStartSql + "'," + "endingTime = '"
					+ slotEndSql + "' WHERE id=" + id + ";";
			break;
		default:
			;
		}
		executeQuery(this.conn, query);
		System.out.println("Object edited Successfully.");
	}

	/**
	 * Gets all rows from the 'Room' table
	 * 
	 * @return List<Map> list
	 * @throws SQLException
	 */
	public List<Map> getAllRowRoom() throws SQLException {

		HashMap<String, Comparable> hashmap;
		List<Map> list = new ArrayList();

		String query = "SELECT * FROM Room;";
		ResultSet rs = getQueryResult(this.conn, query);

		while (rs.next()) {
			hashmap = new HashMap<String, Comparable>();

			hashmap.put("id", rs.getInt("id"));
			hashmap.put("roomName", rs.getString("roomName"));

			list.add(hashmap);
		}
		rs.close();

		return list;
	}

	/**
	 * Gets all rows from the 'Person' table
	 * 
	 * @return List<Map> list
	 * @throws SQLException
	 */
	public List<Map> getAllRowPerson() throws SQLException {

		HashMap<String, Comparable> hashmap;
		List<Map> list = new ArrayList();

		String query = "SELECT * FROM Person;";
		ResultSet rs = getQueryResult(this.conn, query);

		while (rs.next()) {
			hashmap = new HashMap<String, Comparable>();

			hashmap.put("id", rs.getInt("id"));
			hashmap.put("lastName", rs.getString("lastName"));
			hashmap.put("firstName", rs.getString("firstName"));
			hashmap.put("studentNumber", rs.getInt("studentNumber"));
			hashmap.put("birthdate", rs.getDate("birthDate"));

			list.add(hashmap);
		}
		rs.close();

		return list;
	}

	/**
	 * Gets all rows from the 'Timeslot' table
	 * 
	 * @return List<Map> list
	 * @throws SQLException
	 */
	public List<Map> getAllRowTimeslot() throws SQLException {

		HashMap<String, Comparable> hashmap;
		List<Map> list = new ArrayList();

		String query = "SELECT * FROM timeSlot;";
		ResultSet rs = getQueryResult(this.conn, query);

		while (rs.next()) {
			hashmap = new HashMap<String, Comparable>();

			hashmap.put("id", rs.getInt("id"));
			hashmap.put("beginTime", rs.getTimestamp("beginTime"));
			hashmap.put("endingTime", rs.getTimestamp("endingTime"));

			list.add(hashmap);
		}
		rs.close();

		return list;
	}

	/**
	 * Gets all rows from the 'Reservation' table
	 * 
	 * @return List<Map> list
	 * @throws SQLException
	 */
	public List<Map> getAllRowReservation() throws SQLException {

		HashMap<String, Comparable> hashmap;
		List<Map> list = new ArrayList();

		String query = "SELECT * FROM Reservation;";
		ResultSet rs = getQueryResult(this.conn, query);

		while (rs.next()) {
			hashmap = new HashMap<String, Comparable>();

			hashmap.put("id", rs.getInt("id"));
			hashmap.put("personID", rs.getInt("personID"));
			hashmap.put("roomID", rs.getInt("roomID"));
			hashmap.put("timeSlotID", rs.getInt("timeSlotID"));

			list.add(hashmap);
		}
		rs.close();

		return list;
	}

	/**
	 * Sends back object info for a given id in the 'Reservation' table
	 * 
	 * @return HashMap<String, Comparable> object
	 * @param int id
	 * @throws SQLException
	 */
	public HashMap<String, Comparable> getReservationFromID(int id) throws SQLException {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		String query = "SELECT * FROM Reservation WHERE id=" + id + ";";
		ResultSet rs = getQueryResult(this.conn, query);

		if (rs.next()) {
			hashmap.put("personID", rs.getInt("personID"));
			hashmap.put("roomID", rs.getInt("roomID"));
			hashmap.put("timeSlotID", rs.getInt("timeSlotID"));
			hashmap.put("id", id);
		}
		rs.close();

		return hashmap;
	}

	/**
	 * Sends back object info for a given id in the 'Timesolt' table
	 * 
	 * @return HashMap<String, Comparable> object
	 * @param int id
	 * @throws SQLException
	 */
	public HashMap<String, Comparable> getTimeslotFromID(int id) throws SQLException {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		String query = "SELECT * FROM timeSlot WHERE id=" + id + ";";
		ResultSet rs = getQueryResult(this.conn, query);

		if (rs.next()) {
			hashmap.put("beginTime", rs.getTimestamp("beginTime"));
			hashmap.put("endingTime", rs.getTimestamp("endingTime"));
			hashmap.put("id", id);
		}
		rs.close();

		return hashmap;
	}

	/**
	 * Sends back object info for a given id in the 'Room' table
	 * 
	 * @return HashMap<String, Comparable> object
	 * @param int id
	 * @throws SQLException
	 */
	public HashMap<String, Comparable> getRoomFromID(int id) throws SQLException {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		String query = "SELECT * FROM Room WHERE id=" + id + ";";
		ResultSet rs = getQueryResult(this.conn, query);

		if (rs.next()) {
			hashmap.put("roomName", rs.getString("roomName"));
			hashmap.put("id", id);
		}
		rs.close();

		return hashmap;
	}

	/**
	 * Sends back object info for a given id in the 'Person' table
	 * 
	 * @return HashMap<String, Comparable> object
	 * @param int id
	 * @throws SQLException
	 */
	public HashMap<String, Comparable> getPersonFromID(int id) throws SQLException {

		HashMap<String, Comparable> hashmap = new HashMap<String, Comparable>();

		String query = "SELECT * FROM Person WHERE id=" + id + ";";
		ResultSet rs = getQueryResult(this.conn, query);

		if (rs.next()) {
			hashmap.put("lastName", rs.getString("lastName"));
			hashmap.put("firstName", rs.getString("firstName"));
			hashmap.put("studentNumber", rs.getInt("studentNumber"));
			hashmap.put("birthdate", rs.getDate("birthDate"));
			hashmap.put("id", id);
		}
		rs.close();

		return hashmap;
	}

}
