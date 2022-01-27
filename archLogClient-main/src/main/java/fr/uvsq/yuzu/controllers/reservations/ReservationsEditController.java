package fr.uvsq.yuzu.controllers.reservations;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Reservation;
import fr.uvsq.yuzu.models.Room;
import fr.uvsq.yuzu.models.Timeslot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import fr.uvsq.yuzu.controllers.reservations.ReservationsIndexController;

/**
 * Controller for modifying a reservation, the timeslot to be precise (not the
 * person or the room)
 * 
 * @author Repain
 *
 */
public class ReservationsEditController implements Initializable {

	/**
	 * Label displaying the person of the reservation to be modified
	 */
	@FXML
	Label reservationPerson;

	/**
	 * Label displaying the classroom of the reservation
	 */
	@FXML
	Label reservationRoom;

	/**
	 * The combobox to modify the timeslot of the reservation
	 */
	@FXML
	ComboBox<Timeslot> timeslotsCombobox = new ComboBox<Timeslot>();

	/**
	 * Displays errors related to the form
	 */
	@FXML
	Label error;

	List<Timeslot> timeslots = new ArrayList<Timeslot>();

	/**
	 * The reservation to be modified
	 */
	static Reservation reservation;

	/**
	 * Initialize javafx components: label for the reservation person, label for the
	 * reservation room and a combobox to modify the reservation timeslot
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reservationPerson.setText(reservation.getPerson().toString());
		reservationRoom.setText(reservation.getRoom().toString());
		try {
			fetchTimeslots();
		} catch (IOException | InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timeslotsCombobox.getItems().addAll(timeslots);
	}

	/**
	 * Updates a reservation in the database
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@FXML
	private void update() throws IOException, InterruptedException {
		Timeslot timeslot = timeslotsCombobox.getValue();

		if (timeslot == null) {
			error.setText("Erreur: il faut sélectionner un créneau.");
			return;
		}
		Person personSelected = reservation.getPerson();
		Room roomSelected = reservation.getRoom();
		Reservation reservation = new Reservation(personSelected, roomSelected, timeslot);
		String body = new Gson().toJson(timeslot,Timeslot.class);
		System.out.println(body);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.uri(URI.create("http://localhost:8080/updateResa"))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") 
                .header("Content-Type", "application/json")
                .build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		System.out.print(response.statusCode());

		reservation.setTimeslot(timeslot);
		ReservationsIndexController.go();
	}

	/**
	 * Fetch the timeslots from the database
	 */
	private static final String Timeslot_URL = "http://localhost:8080/getTimeSlot";
	private void fetchTimeslots() throws IOException,  InterruptedException, ParseException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(Timeslot_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String t = response.body();
        System.out.println(t);
        ObjectMapper mapper = new ObjectMapper();
        List<Map> time = Arrays.asList(mapper.readValue(t, Map[].class));
        for (Map personMap : time) {
        	System.out.println(personMap);
        	String Tbegin =  (String) personMap.get("begining");
        	String Tend =  (String) personMap.get("ending");
        	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");  	
      	    Date date_begin = (Date) dateParser.parse(Tbegin);
        	Date date_end = (Date) dateParser.parse(Tend);
        	       	
        	Timeslot p = new Timeslot(date_begin, date_end);
        	timeslots.add(p);
		}
        	
		
		
	}

	// Routes

	/**
	 * Go to the view of this controller
	 * 
	 * @throws IOException
	 */
	public static void go() throws IOException {
		App.setRoot("reservations/edit");
	}

	@FXML
	public void goToIndexReservation() throws IOException {
		ReservationsIndexController.go();
	}
}
