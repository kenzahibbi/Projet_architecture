package fr.uvsq.yuzu.controllers;

import java.io.IOException;

import fr.uvsq.yuzu.App;
import fr.uvsq.yuzu.controllers.people.PeopleIndexController;
import fr.uvsq.yuzu.controllers.reservations.ReservationsIndexController;
import fr.uvsq.yuzu.controllers.rooms.RoomsIndexController;
import fr.uvsq.yuzu.controllers.timeslots.TimeslotsIndexController;
import javafx.fxml.FXML;

/**
 * The "home" controller. Gives access to : - people management - timeslots
 * management - room management - reservation management
 * 
 * @author Repain
 *
 */
public class DashboardController {

	/**
	 * Sets the current view to the index
	 * 
	 * @throws IOException
	 */
	public static void go() throws IOException {
		App.setRoot("dashboard/index");
	}

	/**
	 * Go to index of rooms
	 * @throws IOException
	 */
	@FXML
	private void goToRoomIndex() throws IOException {
		RoomsIndexController.go();
	}

	/**
	 * Go to people index
	 * @throws IOException
	 */
	@FXML
	private void goToPersonIndex() throws IOException {
		PeopleIndexController.go();
	}

	/**
	 * Go to timeslots index
	 * @throws IOException
	 */
	@FXML
	private void goToTimeslotIndex() throws IOException {
		TimeslotsIndexController.go();
	}

	/**
	 * Go to reservations index
	 * @throws IOException
	 */
	@FXML
	private void goToReservationIndex() throws IOException {
		ReservationsIndexController.go();
	}
}
