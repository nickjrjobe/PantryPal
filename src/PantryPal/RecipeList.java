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
interface ReadBehavior {
	public List<Recipe> read();
}

class RecipeList extends VBox {
	private ReadBehavior readbehavior;
	RecipeList(ReadBehavior readbehavior) {
		this.readbehavior = readbehavior;
		format();
	}
	private void format() {
		this.setSpacing(5); // sets spacing between tasks
		this.setPrefSize(500, 560);
		this.setStyle("-fx-background-color: #F0F8FF;");
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

	private void addRecipe(Recipe recipe) {
		this.getChildren().add(recipe);
	}
}
class RecipeListPage extends ScrollablePage {
	private RecipeList recipelist;
	RecipeListPage() {
		super("Recipe List", new RecipeList(null));
		this.recipelist = (RecipeList) this.center;
		footer.addButton("Read", e -> { recipelist.read(); });
	}
}
