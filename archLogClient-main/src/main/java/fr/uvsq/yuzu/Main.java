package fr.uvsq.yuzu;


/**
 * The starting point. Please note that, for java/maven to know which class
 * is the main class, the main class MUST NOT inherit anything
 * 
 * @author Repain
 *
 */
public class Main {
	public static void main(String[] args) {
		// -->Database connection
		//Database database = new Database();
		
		try {
			//database.conn = Database.getConnection();
			//Database.fillDB(database.conn);
			App.start(args);
			
			// Database Disconnection
			//database.deconnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
