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

/**
 * UI representation of Recipe on RecipeList page
 */
class RecipeEntry extends HBox {
	private Recipe recipe;
	private Label titleField;
	public Recipe getRecipe() {
		return recipe;
	}
	private void format() {
		this.setPrefSize(500, 20); // sets size of task
		this.setStyle(
		    "-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
	}
	/**
	 * Updates data in Recipe UI element based on data in owned recipe
	 */
private void update() {
	titleField.setText(recipe.getTitle());
	}
	RecipeEntry(Recipe recipe) {
		this.recipe = recipe;
		titleField = new Label();
		titleField.setPrefSize(380, 20);
		titleField.setStyle("-fx-background-color: #dae5ea; -fx-border-width: 0;");
		titleField.setTextAlignment(TextAlignment.LEFT);
		this.getChildren().add(titleField);
		update();
		format();
	}
}
/**
 * internal representation of recipe
 */
class Recipe {
	private String title;
	private String description;
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	Recipe(String title, String description) {
		this.title = title;
		this.description = description;
	}
	Recipe() {
		this("default title", "default description");
	}
}
