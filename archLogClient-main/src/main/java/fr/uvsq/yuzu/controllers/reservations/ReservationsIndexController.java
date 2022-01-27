package fr.uvsq.yuzu.controllers.reservations;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.controllers.DashboardController;
import fr.uvsq.yuzu.misc.TableUtils;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Reservation;
import fr.uvsq.yuzu.models.Room;
import fr.uvsq.yuzu.models.Timeslot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for showing all reservations
 * 
 * @author Repain
 *
 */
public class ReservationsIndexController implements Initializable {

	/**
	 * Table containing all reservations
	 */
	@FXML
	TableView<Reservation> reservationsTable;
	@FXML
	TableColumn<Reservation, String> personColumn;
	@FXML
	TableColumn<Reservation, String> roomColumn;
	@FXML
	TableColumn<Reservation, String> timeslotColumn;

	/**
	 * Label to display any error
	 */
	@FXML
	Label error;

	List<Reservation> reservations = new ArrayList<Reservation>();
	private Timeslot timeslot;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		personColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("person"));

		roomColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("room"));

		timeslotColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("timeslot"));

		// Adding buttons to the table
		Consumer<Reservation> editAction = reservation -> {
			try {
				this.editReservation(reservation);
			} catch (IOException e) {
				e.printStackTrace();
				error.setText(e.getLocalizedMessage());
			}
		};
		Consumer<Reservation> deleteAction = reservation -> {
			try {
				this.deleteReservation(reservation);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				error.setText(e.getLocalizedMessage());
			}
		};

		TableUtils.addButtonToTable(reservationsTable, "Modifier", "", editAction);
		TableUtils.addButtonToTable(reservationsTable, "Supprimer", "", deleteAction);

		// Fetching the data and put it in the table
		try {
			fetchReservations();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reservationsTable.getItems().setAll(reservations);
	}

	/**
	 * Deletes a reservation object
	 * 
	 * @param reservation
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void deleteReservation(Reservation reservation) throws IOException, InterruptedException {
		String url = "http://localhost:8080/DeleteResa" ;
		String body = new Gson().toJson(reservation.id);		
		System.out.println(body);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.method("DELETE", HttpRequest.BodyPublishers.ofString(body))
				.uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") 
                .header("Content-Type", "application/json")
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		System.out.print(response.statusCode());
		System.out.println(response.body());
		ReservationsIndexController.go();
	}

	/**
	 * Go to the reservation edit controller TODO rename goToEditReservation ?
	 * 
	 * @param reservation
	 * @throws IOException
	 */
	private void editReservation(Reservation reservation) throws IOException {
		ReservationsEditController.reservation = reservation;
		ReservationsEditController.go();
	}

	/**
	 * Fetch all reservations from the database
	 */
	private static final String Reservation_URL = "http://localhost:8080/getReservation";
	private void fetchReservations() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(Reservation_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String s = response.body();
        System.out.println(s);
        ObjectMapper mapper = new ObjectMapper();
        List<Map> reservation = Arrays.asList(mapper.readValue(s, Map[].class));
        for (Map personMap : reservation) {
           	Person person = (Person) personMap.get("person");;
			Room room = (Room) personMap.get("room");;
			Timeslot timeslot = (Timeslot) personMap.get("timeslot");;
			Reservation p = new Reservation(person, room, timeslot);
			//Person p = Person.from(personMap);
        	reservations.add(p);
		}
		
		
	}

	// Routes

	public static void go() throws IOException {
		App.setRoot("reservations/index");
	}

	@FXML
	public void goToNewReservation() throws IOException {
		ReservationsNewController.go();
	}

	@FXML
	private void goToDashboardIndex() throws IOException {
		DashboardController.go();
	}

	@FXML
	private void goToEditReservation() throws IOException {
		ReservationsEditController.go();
	}
}
