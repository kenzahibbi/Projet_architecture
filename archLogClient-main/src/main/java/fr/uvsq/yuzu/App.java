package fr.uvsq.yuzu;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App
 * 
 * Starting point of the application
 */
public class App extends Application {

	private static Scene scene;
	
	/**
	 * Object to perform operations on the Database
	 */
	//public static Database database;

	/**
	 * The window title
	 */
	private final String APP_TITLE = "Gestion de ressources";

	/**
	 * The path of the icon; note the /
	 */
	private final String LOGO_PATH = "/icon.png";

	/**
	 * The first view to display on startup
	 */
	private final String STARTING_POINT = "dashboard/index";

	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML(STARTING_POINT), 640, 480);

		// Icon made by Freepik from www.flaticon.com
		stage.getIcons().add(new Image(App.class.getResourceAsStream(LOGO_PATH)));
		stage.setTitle(APP_TITLE);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Sets the current view (and thus the controller)
	 * 
	 * @param fxml The path of the view, from src/main/resources; you must omit the
	 *             file extension .fxml
	 * @throws IOException
	 */
	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	/**
	 * Loads the view with the FXML file given
	 * 
	 * @param fxml The path of the XML file; omit the file extension
	 * @return
	 * @throws IOException
	 */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	/**
	 * Starts the app
	 * @param args
	 * @param database
	 */
	public static void start(String[] args) {
		//App.database = database;
		launch();
	}
}