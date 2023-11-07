/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
/*
 * Object which handles which Page is currently displayed
 */
class PageTracker {
	private Stage primaryStage;
	PageTracker(Stage primaryStage) {
		// Set the title of the app
		primaryStage.setTitle("PantryPal");
		this.primaryStage = primaryStage;
	}
	/**
	 * "Swaps" to displaying a new page
	 * @param page page to display
	 */
	void swapToPage(ScrollablePage page) {
		primaryStage.setScene(page.getWrapperScene());
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}

public class PantryPal extends Application {
	RecipeListPage recipelist;
	PageTracker pageTracker;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.recipelist = new RecipeListPage();
		this.pageTracker = new PageTracker(primaryStage);
		pageTracker.swapToPage(recipelist);
		recipelist.footer.addButton("New Recipe", e -> {createNewRecipePage();});
	}

	private void createNewRecipePage(){
		NewRecipePage newRecipePage = new NewRecipePage();
		newRecipePage.footer.addButton("Cancel", e -> {pageTracker.swapToPage(recipelist);});
		pageTracker.swapToPage(newRecipePage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
