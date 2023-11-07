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
		
		footer.addButton("Exit", e -> {exit(); }); // Add the exit button to footer
		
		// receive a list of Button
		List<Button> footerButtons = recipeDetailUI.getFooterButtons();
		for (Button button : footerButtons) {
			footer.addButton(button);
		}
	}

	private void exit() {
		System.out.println("Exiting Recipe Detail Page");	
		//TODO: switch to RecipeListPage
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
	protected RecipeList recipeList;
	protected Recipe recipe;
	protected List<Button> footerButtons = new ArrayList<Button>();
	protected Label titleField;
	protected TextArea descriptionField; // Changed from Label to TextField

	RecipeDetailUITemplate(RecipeList recipeList, Recipe recipe) {
		this.recipeList = recipeList;
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

	public List<Button> getFooterButtons() {
		return footerButtons;
	}

	// add footer buttons to the list
	public void addButton(Button button) {
		footerButtons.add(button);
	}

	public String getRecipeDescription() {
		return recipe.getDescription();
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
	protected Button deleteButton;
	protected Button editButton;

	public RecipeDetailUI(RecipeList recipeList, Recipe recipe) {
		super(recipeList, recipe);
		super.setDescriptionEditable(false);
		// Initialize buttons and their actions
		deleteButton = new Button("Delete Recipe");
		deleteButton.setOnAction(e -> deleteRecipe());
		this.addButton(deleteButton);
		editButton = new Button("Edit");
		editButton.setOnAction(e -> editRecipe());
		this.addButton(editButton);
	}

	public void deleteRecipe() {
		super.recipeList.deleteRecipe(recipe);
		// TODO: switch to RecipeRecipeUI page with the same recipe detail displayed
		

	}

	public void editRecipe() {
		System.out.println("switching to EditableRecipeUI");	
		// TODO: switch to EditableRecipeUI with the same recipe detail displayed
	}
}

/**
 * UI element which displays recipe detail
 * 2 footer buttons: deleteButton & saveEditButton
 */

class EditableRecipeUI extends RecipeDetailUITemplate {
	protected Button deleteButton;
	protected Button saveEditButton;

	public EditableRecipeUI(RecipeList recipeList, Recipe recipe) {
		super(recipeList, recipe);
		super.setDescriptionEditable(true);
		// Initialize buttons and their actions
        saveEditButton = new Button("Save Edits");
        saveEditButton.setOnAction(e -> saveEdits());
        this.addButton(saveEditButton);
		deleteButton = new Button("Delete Recipe");
		deleteButton.setOnAction(e -> deleteRecipe());
		this.addButton(deleteButton);
	}

	public void deleteRecipe() {
		System.out.println("deleting recipe");	
		super.recipeList.deleteRecipe(recipe);
		System.out.println("current recipeList: " + super.recipeList.getRecipe(0).getTitle());
		// TODO: switch to RecipeRecipeUI page with the same recipe detail displayed

	}

	public void saveEdits() {
		// Delete the current recipe
        deleteRecipe();
        
        // Create a new recipe with the same title and new description
        String newDescription = descriptionField.getText();
        Recipe newRecipe = new Recipe(recipe.getTitle(), newDescription);
        // update recipeList
        super.recipeList.addRecipe(newRecipe);
		System.out.println("Returning to RecipeDetailPage");	
		// TODO: switch to RecipeRecipeUI page with the same recipe detail displayed

	}

}
/**
 * UI element which displays recipe detail
 * 1 footer button: saveRecipeButton
 */

class NewDetailedRecipeUI extends RecipeDetailUITemplate {
	protected Button saveEditButton;

	public NewDetailedRecipeUI(RecipeList recipeList, Recipe recipe) {
		super(recipeList, recipe);
		super.setDescriptionEditable(false);
		// Initialize buttons and their actions
		saveEditButton = new Button("Save Recipe");
		saveEditButton.setOnAction(e -> saveRecipe());
		this.addButton(saveEditButton);
	}

	public void saveRecipe() {
		super.recipeList.addRecipe(recipe);

	}

}
