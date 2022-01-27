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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Reservation;
import fr.uvsq.yuzu.models.Room;
import fr.uvsq.yuzu.models.Timeslot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Controller for creating a new reservation
 * 
 * @author Repain
 *
 */
public class ReservationsNewController implements Initializable {

	/**
	 * For choosing a person to choose for the new reservation
	 */
	@FXML
	ComboBox<Person> reservationPerson = new ComboBox<Person>();

	/**
	 * For choosing a room for the new reservation 
	 */
	@FXML
	ComboBox<Room> reservationRoom = new ComboBox<Room>();
	
	/**
	 * For choosing a timeslot for the new reservation
	 */
	@FXML
	ComboBox<Timeslot> reservationTimeslot = new ComboBox<Timeslot>();

	/**
	 * Displays errors related to the form
	 */
	@FXML
	Label error;
	
	List<Person> people = new ArrayList<Person>();
	List<Room> rooms = new ArrayList<Room>();
	List<Timeslot> timeslots = new ArrayList<Timeslot>();

	/**
	 * Initialize javafx components: comboboxes
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			fetchPeople();
		} catch (IOException | InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fetchRooms();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fetchTimeslots();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// By default, the combobox will display the toString() result of the object
		ObservableList<Timeslot> timeslotSamples = FXCollections.observableArrayList(timeslots);
		ObservableList<Person> peopleSamples = FXCollections.observableArrayList(people);
		ObservableList<Room> roomSamples = FXCollections.observableArrayList(rooms);
		
		reservationTimeslot.setItems(timeslotSamples);
		reservationPerson.setItems(peopleSamples);
		reservationRoom.setItems(roomSamples);
	}

	/**
	 * Creates a reservation in the database
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void createReservation() throws IOException, InterruptedException {
		Person personSelected = reservationPerson.getValue();
		Room roomSelected = reservationRoom.getValue();
		Timeslot timeslotSelected = reservationTimeslot.getValue();
		
		if (personSelected == null || roomSelected == null || timeslotSelected == null) {
			error.setText("Une erreur est survenue lors de la validation du formulaire: un champ est vide");
			return;
		}
		
		Reservation reservation = new Reservation(personSelected, roomSelected, timeslotSelected);
		String body = new Gson().toJson(reservation,Reservation.class);
		System.out.println(body);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.uri(URI.create("http://localhost:8080/addResa"))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") 
                .header("Content-Type", "application/json")
                .build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		System.out.print(response.statusCode());
		ReservationsIndexController.go();
	}
	private static final String Person_URL = "http://localhost:8080/personList";

	private void fetchPeople() throws IOException, InterruptedException, ParseException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(Person_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(peopleMaps);
        String s = response.body();
        System.out.println(s);
        
        ObjectMapper mapper = new ObjectMapper();
        // 1. convert JSON array to Array objects
        List<Map> ppl2 = Arrays.asList(mapper.readValue(s, Map[].class));
        for (Map personMap : ppl2) {
        	System.out.println(personMap);
        	int Id = (int)personMap.get("id");

        	String name_tmp = (String) personMap.get("lastName");
        	String lname_tmp = (String) personMap.get("firstName");
        	int id_tmp = (int) personMap.get("studentNumber");
        	String date_tmp =  (String) personMap.get("birthdate");
        	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");  	
        	Date date_bir = (Date) dateParser.parse(date_tmp);
       
        	Person p = new Person(name_tmp, lname_tmp, id_tmp, date_bir);
        	p.setId(Id);
			people.add(p);
		}
	}
	private static final String Room_URL = "http://localhost:8080/getRoomList";

	private void fetchRooms() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
          .GET()
          .header("accept", "application/json")
          .uri(URI.create(Room_URL))
          .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tmp = response.body();
        ObjectMapper mapper = new ObjectMapper();
        rooms = new ArrayList<Room>();
        // 1. convert JSON array to Array objects
        List<Map> roomsMaps = Arrays.asList(mapper.readValue(tmp, Map[].class));
        for (Map roomMap : roomsMaps) {
        	int Id = (int)roomMap.get("id");
			String name_tmp = (String) roomMap.get("name");
			Room ro = new Room(name_tmp,Id);
			rooms.add(ro);
		}
	}
	private static final String Timeslot_URL = "http://localhost:8080/getTimeSlot";

	private void fetchTimeslots() throws IOException, InterruptedException, ParseException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(Timeslot_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(peopleMaps);
        String t = response.body();
        System.out.println(t);
        ObjectMapper mapper = new ObjectMapper();
        List<Map> time = Arrays.asList(mapper.readValue(t, Map[].class));
        for (Map personMap : time) {
        	System.out.println(personMap);
        	int Id = (int)personMap.get("id");
        	String Tbegin =  (String) personMap.get("begining");
        	String Tend =  (String) personMap.get("ending");
        	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");  	
      	    Date date_begin = (Date) dateParser.parse(Tbegin);
        	Date date_end = (Date) dateParser.parse(Tend);
        	       	
        	Timeslot p = new Timeslot(date_begin, date_end,Id);
        	timeslots.add(p);
		}
	}

	// Routes

	/**
	 * Go to the view of the current controller
	 * @throws IOException
	 */
	public static void go() throws IOException {
		App.setRoot("reservations/new");
	}

	/**
	 * Got to the view of reservation index
	 * @throws IOException
	 */
	public void goToReservationIndex() throws IOException {
		ReservationsIndexController.go();
	}
}
