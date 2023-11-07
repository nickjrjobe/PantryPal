// used for testing RecipeDetailPage (with RecipeDetailUI, EditableRecipeUI, or NewDetailedRecipeUI)
package PantryPal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class RecipeDetailPageCheck extends Application {


    @Override
    public void start(Stage primaryStage) {
	    RecipeList recipeList = new RecipeList(null);
	    recipeList.addRecipe(new Recipe("Spaghetti",
		"A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce.A delicious spaghetti recipe with tomato sauce."));
	    recipeList.addRecipe(
		new Recipe("Salad", "A healthy green salad with a variety of vegetables."));
	    Recipe recipe = recipeList.getRecipe(0);

	    // the 3 variants of UI
	    RecipeDetailUI recipeDetailUI = new RecipeDetailUI(recipeList, recipe);
	    EditableRecipeUI editableRecipeUI = new EditableRecipeUI(recipeList, recipe);
	    NewDetailedRecipeUI newDetailedRecipeUI = new NewDetailedRecipeUI(recipeList, recipe);

	    // RecipeDetailPage recipeDetailPage = new RecipeDetailPage(recipeDetailUI);
	    RecipeDetailPage recipeDetailPage = new RecipeDetailPage(editableRecipeUI);
	    // RecipeDetailPage recipeDetailPage = new RecipeDetailPage(newDetailedRecipeUI);

	    primaryStage.setTitle("Recipe Detail UI Test");
	    primaryStage.setScene(new Scene(recipeDetailPage, 600, 800));
	    primaryStage.setResizable(false);
	    primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
