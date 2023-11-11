/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
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
    this.home = page;
  }

  /**
   * "Swaps" to displaying a new page
   *
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
  RecipeListPage recipelist;
  PageTracker pageTracker;

  @Override
  public void start(Stage primaryStage) throws Exception {
    pageTracker = new PageTracker(primaryStage);
    this.recipelist = new RecipeListPage(pageTracker);
    pageTracker.setHome(recipelist);
    pageTracker.goHome();
    recipelist.footer.addButton(
        "New Recipe",
        e -> {
          createNewRecipePage();
        });
  }

  private void createNewRecipePage() {
    NewRecipeUI newRecipeUI = new NewRecipeUI();
    NewRecipePage newRecipePage = new NewRecipePage(newRecipeUI);
    newRecipePage.footer.addButton(
        "Cancel",
        e -> {
          pageTracker.swapToPage(recipelist);
        });
    newRecipePage.footer.addButton(
        "next",
        e -> {
          recipelist.saveNewRecipe(newRecipeUI);
        });
    pageTracker.swapToPage(newRecipePage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
