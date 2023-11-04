// used for testing RecipeDetailPage (with RecipeDetailUI, EditableRecipeUI, or NewDetailedRecipeUI)
package PantryPal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

// Mock implementation of ReadBehavior to return a static list of Recipes
class MockReadBehavior implements ReadBehavior {
    @Override
    public List<Recipe> read() {
        // Create a list of mock recipes
        List<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(new Recipe("Spaghetti", "A delicious spaghetti recipe with tomato sauce."));
        mockRecipes.add(new Recipe("Salad", "A healthy green salad with a variety of vegetables."));
        // Add more mock recipes as needed for testing
        return mockRecipes;
    }
}

public class US2Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        ReadBehavior mockReadBehavior = new MockReadBehavior();
        RecipeList recipeList = new RecipeList(mockReadBehavior);
        Recipe recipe = new Recipe("Sample Recipe", "Description of Sample Recipe"); // Replace with actual constructor

        RecipeDetailUI recipeDetailUI = new RecipeDetailUI(recipeList, recipe);
        
        RecipeDetailPage recipeDetailPage = new RecipeDetailPage(recipeDetailUI);

        primaryStage.setTitle("Recipe Detail UI Test");
        primaryStage.setScene(new Scene(recipeDetailPage, 600, 800));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
