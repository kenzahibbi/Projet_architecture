package fr.uvsq.yuzu.controllers.people;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.models.Person;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for creating a person
 * 
 * @author Repain
 *
 */
public class PeopleNewController {

	/**
	 * input for the person's last name
	 */
	@FXML
	TextField personLastname;
	
	/**
	 * Input for the person's firstname
	 */
	@FXML
	TextField personFirstname;
	
	/**
	 * Input for the person's student number
	 */
	@FXML
	TextField personStudentNumber;
	
	/**
	 * Input for the person's birthdate
	 */
	@FXML
	DatePicker personBirthdate;

	/**
	 * Label to display any validation error
	 */
	@FXML
	Label error;

	/**
	 * Creates a person, from the form fields. Checks if the fields are empty and
	 * notifies the user if so
	 * 
	 * TODO check if user already exists ? TODO show exactly which field is
	 * missing/empty ?
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@FXML
	private void createPerson() throws IOException, InterruptedException {
		// Checking fields are not empty
		// TODO put in constructor ? it's ugly
		
		
		if (personLastname.getText() == null || personFirstname.getText() == null
				|| personStudentNumber.getText() == null || personBirthdate.getValue() == null
				|| personLastname.getText().isBlank() || personFirstname.getText().isBlank()
				|| personStudentNumber.getText().isBlank()) {

			error.setText("Error: a field is empty");
			return;
		}

		try {
			Integer.valueOf(personStudentNumber.getText());
		} catch (NumberFormatException e) {
			error.setText("Erreur : le numéro étudiant doit être un chiffre.");
			return;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date birthdate = formatter.parse(personBirthdate.getValue().toString());
			java.sql.Date BirDate = new java.sql.Date(birthdate.getTime());
			System.out.print(BirDate);
			Person person = new Person(personLastname.getText(), personFirstname.getText(),
					Integer.valueOf(personStudentNumber.getText()), BirDate);

			person.getBirthdate();
			//String body = new Gson().toJson(person, Person.class);
			HashMap hashmap = person.toHashMap();
			
			
			
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String body = gson.toJson(person,Person.class);
			System.out.println(body);
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create("http://localhost:8080/setPerson"))
					.setHeader("User-Agent", "Java 11 HttpClient Bot") 
	                .header("Content-Type", "application/json")
	                .build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			System.out.print(response.statusCode());

		} catch (ParseException e) {
			error.setText("Erreur: le format de date est incorrect.");
			return;
		} /*catch (SQLException e) {
			e.printStackTrace();
			error.setText(e.getLocalizedMessage());
			return;
		}*/

		PeopleIndexController.go();
	}

	// Routes

	public static void go() throws IOException {
		App.setRoot("people/new");
	}

	@FXML
	private void goToPersonIndex() throws IOException {
		PeopleIndexController.go();
	}
}
