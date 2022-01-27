module fr.uvsq.yuzu {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires java.net.http;
	requires com.fasterxml.jackson.databind;
	requires gson;
    
    
    opens fr.uvsq.yuzu to javafx.fxml;
    exports fr.uvsq.yuzu;
    opens fr.uvsq.yuzu.controllers to javafx.fxml;
    exports fr.uvsq.yuzu.controllers;
    opens fr.uvsq.yuzu.controllers.rooms to javafx.fxml;
    exports fr.uvsq.yuzu.controllers.rooms;
    opens fr.uvsq.yuzu.controllers.timeslots to javafx.fxml;
    exports fr.uvsq.yuzu.controllers.timeslots;
    opens fr.uvsq.yuzu.controllers.people to javafx.fxml;
    exports fr.uvsq.yuzu.controllers.people;
    opens fr.uvsq.yuzu.controllers.reservations to javafx.fxml;
    exports fr.uvsq.yuzu.controllers.reservations;
    
    opens fr.uvsq.yuzu.models to javafx.base,gson;
    exports fr.uvsq.yuzu.models;
}
