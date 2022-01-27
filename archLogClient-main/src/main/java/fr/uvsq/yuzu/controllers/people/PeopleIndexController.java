package fr.uvsq.yuzu.controllers.people;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.controllers.DashboardController;
import fr.uvsq.yuzu.misc.TableUtils;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;



/**
 * Controller for showing all people
 * 
 * @author Repain
 *
 */
public class PeopleIndexController implements Initializable {

	/**
	 * Table filled with people
	 */
	@FXML
	TableView<Person> peopleTable;

	/**
	 * Column containing the person's lastname
	 */
	@FXML
	TableColumn<Person, String> lastNameColumn;
	@FXML
	TableColumn<Person, String> firstnameColumn;
	@FXML
	TableColumn<Person, String> studentNumberColumn;
	@FXML
	TableColumn<Person, String> birthdateColumn;

	/**
	 * Label used to display errors
	 */
	@FXML
	Label error;

	/**
	 * The list of people to display
	 */
	List<Person> people = new ArrayList<Person>();

	/**
	 * Initialize javafx components (can't do it elsewhere)
	 */
	@Override
	
	public void initialize(URL url, ResourceBundle rb) {
		/*
		 * setting the cell values: for each column we give a field from the object
		 * Person (lastName, firstName, etc), the cell value will be set accordingly to
		 * the property's value
		 */
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
		firstnameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
		studentNumberColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("studentNumber"));
		birthdateColumn.setCellValueFactory(t -> {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return new SimpleStringProperty(sdf.format(t.getValue().getBirthdate()));
		});

		// Note: Consumer is a function with 1 argument
		// Building the event when the user clicks on the DELETE button
		Consumer<Person> deleteAction = person -> {
			try {
				this.deletePerson(person);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				error.setText(e.getLocalizedMessage());
			}
		};
		TableUtils.addButtonToTable(peopleTable, "Supprimer", "", deleteAction);

		try {
			fetchPeople();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		peopleTable.getItems().setAll(people);
	}

	/**
	 * Deletes a person from the database
	 * If we can't, show a warning modal/pop up
	 * 
	 * @param person The person to delete
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private void deletePerson(Person person) throws IOException, InterruptedException {
		String url = "http://localhost:8080/DeletePerson";
		System.out.println(url);
		
		String body = new Gson().toJson(person.id);
		//System.out.println(body);
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
		
		
		PeopleIndexController.go();
	}

	/**
	 * TODO make an interface, ORM, whatever
	 * Ugly & redundant code
	 */
	private static final String Person_URL = "http://localhost:8080/personList";

	private void fetchPeople() throws IOException, InterruptedException, ParseException {
		// ====================================================
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
        //String json = "[{\"lastName\":\"kenza\",\"firstName\":\"hibbi\",\"studentNumber\":1234,\"birthdate\":\"1993-09-21\",\"id\":1},{\"lastName\":\"AAA\",\"firstName\":\"BBB\",\"studentNumber\":1235,\"birthdate\":\"1993-06-21\",\"id\":2}]";

        List<Map> ppl2 = Arrays.asList(mapper.readValue(s, Map[].class));
        for (Map personMap : ppl2) {
        	int Id = (int)personMap.get("id");
        	String name_tmp = (String) personMap.get("lastName");
        	String lname_tmp = (String) personMap.get("firstName");
        	int id_tmp = (int) personMap.get("studentNumber");
        	String date_tmp =  (String) personMap.get("birthdate");
        	//====================
        	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");  	
      	       	
	        //String date_time = "2020-11-06";
	        //SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        	Date date_bir = (Date) dateParser.parse(date_tmp);

	       

  
        	
        	Person p = new Person(name_tmp, lname_tmp, id_tmp, date_bir);
        	p.setId(Id);
			//Person p = Person.from(personMap);
			people.add(p);
		}
        
	
	}
	
	// Routes

	/**
	 * Sets the current view to the view of THIS controller, people's index
	 * 
	 * @throws IOException
	 */
	public static void go() throws IOException {
		App.setRoot("people/index");
	}

	@FXML
	private void goToNewPerson() throws IOException {
		PeopleNewController.go();
	}
	
	@FXML
	private void goToDashboardIndex() throws IOException {
		DashboardController.go();
	}
}
