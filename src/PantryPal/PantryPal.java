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
	private ScrollablePage home;
	PageTracker(Stage primaryStage) {
		// Set the title of the app
		primaryStage.setTitle("PantryPal");
		this.primaryStage = primaryStage;
	}
	void setHome(ScrollablePage page) {
		this.home = home;
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
	void goHome() {
		swapToPage(home);
	}
}

public class PantryPal extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		PageTracker pt = new PageTracker(primaryStage);
		RecipeListPage recipelist = new RecipeListPage(pt);
		pt.setHome(recipelist);
		pt.goHome();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
