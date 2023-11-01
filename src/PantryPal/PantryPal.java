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

public class PantryPal extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		RecipeListPage recipelist = new RecipeListPage();

		// Set the title of the app
		primaryStage.setTitle("PantryPal");
		// Create scene of mentioned size with the border pane
		primaryStage.setScene(new Scene(recipelist, 600, 800));
		// Make window non-resizable
		primaryStage.setResizable(false);
		// Show the app
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
