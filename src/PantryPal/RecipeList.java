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
 * Object which specifies a specific way to read recipe list in
 * from persistent storage
 */
interface ReadBehavior {
	public List<Recipe> read();
}

/**
 * Internal representation of recipes
 */
class RecipeList {
	private ArrayList<Recipe> recipeList;
	private ReadBehavior readbehavior;
	RecipeList(ReadBehavior readbehavior) {
		this.readbehavior = readbehavior;
		this.recipeList = new ArrayList<Recipe>();
	}
	public void read() {
		if (readbehavior == null) {
			System.err.println("Read behavior not provided");
			return;
		}
		List<Recipe> recipes = readbehavior.read();
		for (Recipe recipe : recipes) {
			addRecipe(recipe);
		}
	}

	public void addRecipe(Recipe recipe) {
		recipeList.add(recipe);
	}
	public List<Recipe> getRecipes() {
		return recipeList;
	}
	//delete a recipe from the list
	public void deleteRecipe(Recipe recipe) {
		recipeList.remove(recipe);
	}
}
/**
 * UI element which displays list of recipes
 */
class RecipeListUI extends VBox {
	private RecipeList recipeList;
	RecipeListUI(RecipeList recipelist) {
		this.recipeList = recipelist;
		format();
	}
	private void format() {
		this.setSpacing(5); // sets spacing between tasks
		this.setPrefSize(500, 560);
		this.setStyle("-fx-background-color: #F0F8FF;");
	}
	/**
	 * Synchronize recipe List UI element with application's internal
	 * recipe list
	 */
	private void update() {
		this.getChildren().clear();
		for (Recipe recipe : this.recipeList.getRecipes()) {
			this.getChildren().add(new RecipeEntryUI(recipe));
		}
	}
	public void read() {
		recipeList.read();
		update();
	}
}
/**
 * UI Page containing recipe list, and accompanying header and footer
 */
class RecipeListPage extends ScrollablePage {
	private RecipeListUI recipeList;
	RecipeListPage() {
		super("Recipe List",
		    new RecipeListUI(new RecipeList(new FileReadBehavior("recipes.txt"))));
		this.recipeList = (RecipeListUI) this.center;
		footer.addButton("Read", e -> { recipeList.read(); });
	}
}
