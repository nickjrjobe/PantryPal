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
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * RecipeDetailPage provides a user interface to display the details of a recipe.
 * It integrates with RecipeDetailUITemplate to present
 * recipe details that can include both non-editable and editable fields, along with
 * associated actions as footer buttons. (depends on which variant UI out of the 3 is used)
 *
 * Responsibilities:
 * - Displays the title of the recipe using the Header component.
 * - Adds an "Exit" button to the header for navigation purposes.
 * - Aggregates and displays footer buttons provided by RecipeDetailUITemplate.
 *
 * Usage:
 * - An instance of this class should be created by passing a RecipeDetailUITemplate
 *  which should be a specific variant of UI among RecipeDetailUI, EditableRecipeUI, or NewDetailedRecipeUI.
 * - exit method is placeholder for tab switch to RecipeListPage.
 */
public class RecipeDetailPage extends ScrollablePage {
	RecipeDetailUITemplate recipeDetailUI;
	RecipeDetailPage(RecipeDetailUITemplate recipeDetailUI){
		super("Recipe Detail", recipeDetailUI); // This initializes and adds the header.
		this.recipeDetailUI = recipeDetailUI;
		
	}

}

/**
 * RecipeDetailUITemplate serves as the abstract base for UI components
 * that represent the details of a recipe within the user interface.
 *
 * Common Properties:
 * - All derived classes accept a RecipeList and a Recipe object in their constructor.
 * - Title field is always present but not user-editable.
 * - Description field's editability can be toggled.
 * - Supports adding different buttons to a footer area. 
 *
 * Derived Classes:
 * - RecipeDetailUI: Includes functionality to delete and edit a recipe.
 * - EditableRecipeUI: Includes functionality to delete a recipe and save changes.
 * - NewDetailedRecipeUI: Includes functionality to save a new recipe.
 */

abstract class RecipeDetailUITemplate extends VBox {
	protected Recipe recipe;
	protected Label titleField;
	protected TextArea descriptionField; // Changed from Label to TextField

	RecipeDetailUITemplate(Recipe recipe) {
		this.recipe = recipe;
		titleField = new Label();
		titleField.setPrefSize(600, 20);
		titleField.setStyle("-fx-background-color: #dae5ea; -fx-border-width: 0;");
		titleField.setTextAlignment(TextAlignment.LEFT);
		titleField.setText(recipe.getTitle());

		// Initialize the descriptionField as a text area
		descriptionField = new TextArea();
		descriptionField.setPrefSize(600, 600);
		descriptionField.setEditable(false); // Start as non-editable
		descriptionField.setText(recipe.getDescription());
		descriptionField.setWrapText(true);
		
		this.getChildren().add(titleField);
		this.getChildren().add(descriptionField);
	}

	// to set whether the description is editable
	public void setDescriptionEditable(boolean editable) {
		descriptionField.setEditable(editable);
	}

	public String getRecipeDescription() {
		return recipe.getDescription();
	}
	public String getRecipeDescriptionFieldText() {
		return descriptionField.getText();
	}

	public String getRecipeTitle() {
		return recipe.getTitle();
	}

}
/**
 * UI element which displays recipe detail
 * 2 footer buttons: deleteButton & editButton
 */

class RecipeDetailUI extends RecipeDetailUITemplate {
	public RecipeDetailUI(Recipe recipe) {
		super(recipe);
		super.setDescriptionEditable(false);
	}
}

/**
 * UI element which displays recipe detail
 * 1 footer button: saveRecipeButton
 */

class NewDetailedRecipeUI extends RecipeDetailUITemplate {
	public NewDetailedRecipeUI(Recipe recipe) {
		super(recipe);
		super.setDescriptionEditable(false);
	}
}
