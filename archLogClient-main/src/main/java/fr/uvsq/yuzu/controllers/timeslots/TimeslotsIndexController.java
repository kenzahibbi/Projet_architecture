package fr.uvsq.yuzu.controllers.timeslots;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.controllers.DashboardController;
import fr.uvsq.yuzu.misc.TableUtils;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Room;
import fr.uvsq.yuzu.models.Timeslot;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for displaying all timeslots and managing them
 * 
 * @author Repain
 *
 */
public class TimeslotsIndexController implements Initializable {

	@FXML
	TableView<Timeslot> timeslotsTable;
	@FXML
	TableColumn<Timeslot, String> beginDatetimeColumn;
	@FXML
	TableColumn<Timeslot, String> endDatetimeColumn;

	@FXML
	Label error;

	List<Timeslot> timeslots = new ArrayList<Timeslot>();

	/**
	 * Initialize javafx components
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			fetchTimeslots();
		} catch (IOException | InterruptedException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// here we need to format the date, as there's no property for the Date class
		beginDatetimeColumn.setCellValueFactory(t -> {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM");
			return new SimpleStringProperty(sdf.format(t.getValue().getBegining()));
		});

		endDatetimeColumn.setCellValueFactory(t -> {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM");
			return new SimpleStringProperty(sdf.format(t.getValue().getEnding()));
		});

		// adding a callback after pressing the delete button
		Consumer<Timeslot> deleteAction = timeslot -> {
			try {
				this.deleteTimeslot(timeslot);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				error.setText(e.getLocalizedMessage());
			}
		};
		TableUtils.addButtonToTable(timeslotsTable, "Supprimer", "", deleteAction);

		timeslotsTable.getItems().setAll(timeslots);
	}

	/**
	 * Deletes a given timeslot. Shows a popup if a timeslot is already used by a
	 * reservation
	 * 
	 * @param timeslot The timeslot to delete
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private void deleteTimeslot(Timeslot timeslot) throws IOException, InterruptedException {
		String url = "http://localhost:8080/DeleteTimeSlot" ;
		String body = new Gson().toJson(timeslot.id);
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

		TimeslotsIndexController.go();
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

	public static void go() throws IOException {
		App.setRoot("timeslots/index");
	}

	@FXML
	private void goToNewTimeslot() throws IOException {
		TimeslotsNewController.go();
	}

	@FXML
	private void goToDashboardIndex() throws IOException {
		DashboardController.go();
	}
}
