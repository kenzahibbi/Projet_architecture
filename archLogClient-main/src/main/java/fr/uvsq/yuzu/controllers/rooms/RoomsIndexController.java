package fr.uvsq.yuzu.controllers.rooms;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.controllers.DashboardController;
import fr.uvsq.yuzu.misc.TableUtils;
import fr.uvsq.yuzu.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for displaying rooms
 * 
 * @author Repain
 *
 */
public class RoomsIndexController implements Initializable {

	@FXML
	TableView<Room> roomsTable;
	@FXML
	TableColumn<Room, String> nameColumn;
	@FXML
	Label error;

	List<Room> rooms;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			fetchRooms();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		nameColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));

		// Consumer: function with 1 argument (callback)
		Consumer<Room> deleteAction = room -> {
			try {
				this.deleteRoom(room);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				error.setText(e.getLocalizedMessage());
			}
		};
		TableUtils.addButtonToTable(roomsTable, "Supprimer", "", deleteAction);
		roomsTable.getItems().setAll(rooms);
	}

	/**
	 * Deletes a room from the database
	 * 
	 * @param room
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private void deleteRoom(Room room) throws IOException, InterruptedException {
		String url = "http://localhost:8080/DeleteRoom" ;
		System.out.println(url);
		String body = new Gson().toJson(room.id,int.class);
		
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
		/*	try {
			App.database.deleteObject("Room", room.id);
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Erreur");
			alert.setHeaderText("Suppression impossible");
			alert.setContentText("Supprimez la réservation qui utilise déjà cet objet.");
			alert.showAndWait();

			return;
		}
		goToRoomIndex();*/
	}

	/**
	 * Fetch all rooms from the database
	 */
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

	// Routes

	public static void go() throws IOException {
		App.setRoot("rooms/index");
	}

	@FXML
	private void goToNewRoom() throws IOException {
		RoomsNewController.go();
	}

	@FXML
	private void goToRoomIndex() throws IOException {
		RoomsIndexController.go();
	}

	@FXML
	private void goToDashboardIndex() throws IOException {
		DashboardController.go();
	}
}
