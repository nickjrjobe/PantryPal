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
 * UI element template 
 * 
 * Their shared properties:
 * all accept a recipe object in their constructor
 * a title field that is not editable
 * a description field that is either editable or not editable (there should be a toggle)
 * Extended by RecipeDetailUI (editButton), EditableRecipeUI (saveEditButton), NewDetailedRecipeUI (saveRecipeButton)
 * 
 * for DRY purposes
 */


public abstract class RecipeDetailUiTemplate extends VBox{
	private Recipe recipe;
	List<String> buttonNames = new ArrayList<String>();
	private Label titleField;
	private Label descriptionField;
	RecipeDetailUiTemplate(Recipe recipe){
		this.recipe = recipe;
		titleField = new Label();
		titleField.setPrefSize(380, 20);
		titleField.setStyle("-fx-background-color: #dae5ea; -fx-border-width: 0;");
		titleField.setTextAlignment(TextAlignment.LEFT);
		titleField.setText(recipe.getTitle());
			
	}

	//add buttonNames
	public void addButtonNames(String buttonName){
		buttonNames.add(buttonName);
	}

	//get buttonNames
	public List<String> getFooterButtonsNames(){
		return buttonNames;
	};
	public String getRecipeDescription() {
        return recipe.getDescription();
    }
	public String getRecipeTitle() {
        return recipe.getTitle();
    }
	public void exit(){
		pass;
	}
	public void edit(){

	}
	public void saveEdits(){

	}

	public void saveRecipe(){
		
	}
	public void deleteRecipe(){

	}
}

	

/**
 * extended by RecipeDetailPage,EditableRecipePage,NewDetailedRecipePage
 */
public abstract class RecipeDetailPage extends ScrollablePage{
	 
	RecipeDetailPage(RecipeDetailUiTemplate recipeDetailUi){
		super("Recipe Detail", recipeDetailUi);
		this.recipeDetailUi = recipeDetailUi;
		header = new Header(recipeDetailUi.getRecipeTitle());
		
		header.addButton("Exit", e -> { recipeDetailUi.exit(); }); // whose method is exit
		
		//receive a list of Button
		List<String> footerButtonNames = recipeDetailUi.getFooterButtonsNames();
        for (String buttonName : footerButtonNames) {
            // Define a generic callback of recipeDetailUi called buttonBehavior
            EventHandler<ActionEvent> buttonBehavior;

            switch (buttonName) {
                case "Edit":
                    buttonBehavior = e -> recipeDetailUi.edit();
                    break;
                case "Save Edits":
                    buttonBehavior = e -> recipeDetailUi.saveEdits();
                    break;
                case "Save Recipe":
                    buttonBehavior = e -> recipeDetailUi.saveRecipe();
                    break;
                case "Delete Recipe":
                    buttonBehavior = e -> recipeDetailUi.deleteRecipe();
                    break;
                default:
                    buttonBehavior = e -> {}; // Do nothing for unrecognized buttons
                    break;
            }
            
            footer.addButton(buttonName,buttonBehavior);
        }

	}
	
}
// ----------------- delete below later-----------------
// /**
//  * Internal representation of recipes
//  */
// class RecipeList {
// 	private ArrayList<Recipe> recipes;
// 	private ReadBehavior readbehavior;
// 	RecipeList(ReadBehavior readbehavior) {
// 		this.readbehavior = readbehavior;
// 		this.recipes = new ArrayList<Recipe>();
// 	}
// 	public void read() {
// 		if (readbehavior == null) {
// 			System.err.println("Read behavior not provided");
// 			return;
// 		}
// 		List<Recipe> recipes = readbehavior.read();
// 		for (Recipe recipe : recipes) {
// 			addRecipe(recipe);
// 		}
// 	}

// 	private void addRecipe(Recipe recipe) {
// 		recipes.add(recipe);
// 	}
// 	public List<Recipe> getRecipes() {
// 		return recipes;
// 	}
// }
/**
 * UI element which displays list of recipes
 */
// class RecipeListUI extends VBox {
// 	private RecipeList recipelist;
// 	RecipeListUI(RecipeList recipelist) {
// 		this.recipelist = recipelist;
// 		format();
// 	}
// 	private void format() {
// 		this.setSpacing(5); // sets spacing between tasks
// 		this.setPrefSize(500, 560);
// 		this.setStyle("-fx-background-color: #F0F8FF;");
// 	}
// 	/**
// 	 * Synchronize recipe List UI element with application's internal
// 	 * recipe list
// 	 */
// 	private void update() {
// 		this.getChildren().clear();
// 		for (Recipe recipe : this.recipelist.getRecipes()) {
// 			this.getChildren().add(new RecipeEntryUI(recipe));
// 		}
// 	}
// 	public void read() {
// 		recipelist.read();
// 		update();
// 	}
// }
// /**
//  * UI Page containing recipe list, and accompanying header and footer
//  */
// class RecipeListPage extends ScrollablePage {
// 	private RecipeListUI recipelist;
// 	RecipeListPage() {
// 		super("Recipe List",
// 		    new RecipeListUI(new RecipeList(new FileReadBehavior("recipes.txt"))));
// 		this.recipelist = (RecipeListUI) this.center;
// 		footer.addButton("Read", e -> { recipelist.read(); });
// 	}
// }
