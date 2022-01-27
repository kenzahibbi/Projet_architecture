package fr.uvsq.yuzu.misc;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Utilitary class for javafx table view
 * 
 * @author Repain
 *
 */
public abstract class TableUtils {
	
	/**
	 * Ugly code to add a button to a table view
	 * Taken from: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
	 * 
	 * Example: 
	 * 
	 * <pre>
	 *   Consumer<String> printHello = name -> {
	 *	   try {
	 *       System.out.println("Hello " + name);
	 *	   } catch (IOException e) {
	 *	     e.printStackTrace();
	 *	   }
	 *   };
	 *   
	 *   TableUtils.addButtonToTable(tableView, "Say hello", "", printHello);
	 * </pre>
	 * 
	 * @param <T> The object displayed in the table view row
	 * @param tableView The table of the button
	 * @param buttonText The text of the button
	 * @param columnText The header text of the column
	 * @param callbackFunction The callback function to execute when the button is clicked
	 */
    public static <T> void addButtonToTable(TableView<T> tableView, String buttonText, String columnText, Consumer<T> callbackFunction) {
        TableColumn<T, Void> colBtn = new TableColumn<T, Void>(columnText);

        Callback<TableColumn<T, Void>, TableCell<T, Void>> cellFactory = new Callback<TableColumn<T, Void>, TableCell<T, Void>>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                final TableCell<T, Void> cell = new TableCell<T, Void>() {

                    private final Button btn = new Button(buttonText);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                        	// Get the object selected
                        	T t = getTableView().getItems().get(getIndex());
                        	// Call the callback function with the object selected
							callbackFunction.accept(t);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);
    }
}
