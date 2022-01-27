package fr.uvsq.yuzu.controllers.timeslots;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Timeslot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class TimeslotsNewController implements Initializable {

	/**
	 * Combobox to choose the start hour of the timeslot
	 * TODO replace it with a custom TimePicker
	 */
	@FXML
	ComboBox<String> beginTime = new ComboBox<String>();

	/**
	 * Combobox to choose the end hour of the timeslot
	 * TODO replace it with a custom TimePicker
	 */
	@FXML
	ComboBox<String> endTime = new ComboBox<String>();
	
	/**
	 * Component to choose the date (day, month, year) of the timeslot
	 */
	@FXML
	DatePicker date = new DatePicker();

	/**
	 * Shows any form errors
	 */
	@FXML
	Label error;

	/**
	 * Initialize javafx components
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		beginTime.getItems().addAll(Timeslot.generateTimeSlotsStrings());
		endTime.getItems().addAll(Timeslot.generateTimeSlotsStrings());
	}

	/**
	 * Creates a timeslot and save it in the database
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@FXML
	private void createTimeslot() throws IOException, InterruptedException {
		if (beginTime.getValue() == null || endTime.getValue() == null || date.getValue() == null) {
			error.setText("Erreur: un champ est vide.");
			return;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date beginDatetime = formatter.parse(date.getValue() + " " + beginTime.getValue());
			Date endDatetime = formatter.parse(date.getValue()  + " " +  endTime.getValue());

			// TODO handle with exception
			if (!beginDatetime.before(endDatetime)) {
				error.setText("Erreur: l'heure de début ne peut pas être après l'heure de fin.");
				return;
			}
			
				
				System.out.print(beginDatetime);
				System.out.print(endDatetime);
				Timeslot timeslot = new Timeslot(beginDatetime, endDatetime);
				
				HashMap hashmap = timeslot.toHashMap();
				
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
				String body = gson.toJson(hashmap,HashMap.class);
				System.out.println(body);
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder()
						.POST(HttpRequest.BodyPublishers.ofString(body))
						.uri(URI.create("http://localhost:8080/addTimeSlot"))
						.setHeader("User-Agent", "Java 11 HttpClient Bot") 
		                .header("Content-Type", "application/json")
		                .build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				
				System.out.print(response.statusCode());
				
					

			//App.database.saveObject("Timeslot", timeslot.toHashMap());
		} catch (ParseException e) {
			error.setText("Erreur: le format de date est incorrect.");
			return;
		} /*catch (SQLException e) {
			e.printStackTrace();
			error.setText(e.getLocalizedMessage());
			return;
		}*/

		TimeslotsIndexController.go();
	}

	// Routes

	public static void go() throws IOException {
		App.setRoot("timeslots/new");
	}

	@FXML
	private void goToTimeslotIndex() throws IOException {
		TimeslotsIndexController.go();
	}
}
