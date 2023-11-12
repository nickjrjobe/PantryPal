/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

interface HomeTracker {
  public ScrollablePage getHome();
}

class AppController implements HomeTracker {
  private PageTracker pt;

  public AppController(PageTracker pt) {
    this.pt = pt;
  }

  public ScrollablePage getHome() {
    return makeRecipeListPage();
  }

  public RecipeListPage makeRecipeListPage() {
    RecipeListPage recipeList = new RecipeListPage(getRecipeListEntries());
    recipeList.footer.addButton(
        "New Recipe",
        e -> {
          pt.swapToPage(makeNewRecipeController().getPage());
        });
    return recipeList;
  }

  public List<RecipeEntryUI> getRecipeListEntries() {
    RecipeListModel model = new RecipeListModel(new HttpRequestModel());
    ArrayList<RecipeEntryUI> entries = new ArrayList<>();
    for (String title : model.getRecipeList()) {
      entries.add(makeRecipeEntryUI(title));
    }
    return entries;
  }

  public RecipeEntryUI makeRecipeEntryUI(String title) {
    RecipeEntryUI entry = new RecipeEntryUI(title);
    entry.addButton(
        "details",
        e -> {
          pt.swapToPage(makeRecipeDetailsPage(title));
        });
    return entry;
  }

  public RecipeDetailPage makeRecipeDetailsPage(String title) {
    RecipeDetailModel rc = new RecipeDetailModel(new HttpRequestModel());
    RecipeDetailPage drp = new RecipeDetailPage(new RecipeDetailUI(rc.read(title)));
    drp.footer.addButton(
        "home",
        e -> {
          pt.swapToPage(makeRecipeListPage());
        });
    return drp;
  }

  public NewRecipeController makeNewRecipeController() {
    NewRecipeUI newRecipeUI = new NewRecipeUI();
    NewRecipePage newRecipePage = new NewRecipePage(newRecipeUI);
    NewRecipeModel newRecipeModel = new NewRecipeModel(new HttpRequestModel());
    VoiceToText voiceToText = new WhisperBot();
    return new NewRecipeController(newRecipeUI, newRecipePage, newRecipeModel, pt, voiceToText);
  }
}

/*
 * Object which handles which Page is currently displayed
 */
class PageTracker {
  private Stage primaryStage;
  private HomeTracker homeTracker;

  PageTracker(Stage primaryStage) {
    // Set the title of the app
    primaryStage.setTitle("PantryPal");
    this.primaryStage = primaryStage;
  }

  void setHomeTracker(HomeTracker tracker) {
    this.homeTracker = tracker;
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
    swapToPage(homeTracker.getHome());
  }
}

/** Runner class which starts the UI */
public class PantryPal extends Application {
  RecipeListPage recipelist;
  PageTracker pageTracker;

  @Override
  public void start(Stage primaryStage) throws Exception {
    PageTracker pt = new PageTracker(primaryStage);
    AppController appController = new AppController(pt);
    pt.setHomeTracker(appController);
    pt.goHome();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
