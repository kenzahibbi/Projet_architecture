package fr.uvsq.yuzu.controllers.rooms;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.HashMap;

import com.google.gson.Gson;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.models.Person;
import fr.uvsq.yuzu.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for creating a new room in the database
 * 
 * @author Repain
 *
 */
public class RoomsNewController {

	/**
	 * Input for the room name
	 */
	@FXML
	TextField roomName;

	/**
	 * Displays any errors
	 */
	@FXML
	Label error;

	/**
	 * Creates a room in the database
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@FXML
	private void createRoom() throws IOException, InterruptedException {
		if (roomName.getText() == null || roomName.getText().isBlank() || roomName.getText().isEmpty()) {
			error.setText("Error: room name can't be empty");
			return;
		}

		Room room = new Room(roomName.getText());
		System.out.println(roomName.getText());
		
		
		String body = new Gson().toJson(room,Room.class);
		
		System.out.println(body);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.uri(URI.create("http://localhost:8080/setRoom"))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") 
                .header("Content-Type", "application/json")
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		System.out.print(response.statusCode());

		/*try {
			App.database.saveObject("Room", room.toHashMap());
			room.id = App.database.getSQLID("Room", room.toHashMap());
		} catch (SQLException e) {
			e.printStackTrace();
			error.setText(e.getLocalizedMessage());
			return;
		}*/

		goToRoomIndex();
	}

	// Routes

	public static void go() throws IOException {
		App.setRoot("rooms/new");
	}

	@FXML
	private void goToRoomIndex() throws IOException {
		RoomsIndexController.go();
	}
}
