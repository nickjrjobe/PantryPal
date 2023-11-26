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

    // return makeRecipeListPage();
    return makeLoginPage();
  }

  public AccountCreatePage makeAccountCreatePage() {
    AccountCreateUI accountCreateUI = new AccountCreateUI();
    AccountCreatePage accountCreatePage = new AccountCreatePage(accountCreateUI);
    accountCreatePage.footer.addButton(
        "Create Account",
        e -> {
          LoginCredentials credentials =
              new LoginCredentials(
                  accountCreateUI.getUserNameText(), accountCreateUI.getPasswordText());
          boolean isValidUser = accountCreatePage.isValidCredential();

          if (isValidUser) {
            pt.swapToPage(makeLoginPage());
          }
        });

    return accountCreatePage;
  }

  public AccountLoginPage makeLoginPage() {
    AccountLoginUI accountLoginUI = new AccountLoginUI();
    AccountLoginPage accountLoginPage = new AccountLoginPage(accountLoginUI);

    accountLoginPage.footer.addButton(
        "Login",
        e -> {
          LoginCredentials credentials =
              new LoginCredentials(
                  accountLoginUI.getUserNameText(), accountLoginUI.getPasswordText());
          boolean isValidUser = accountLoginPage.isValidCredential();

          if (isValidUser) {
            pt.swapToPage(makeRecipeListPage(credentials)); // Swap to recipe list page
          }
          accountLoginPage.writeAutoLoginStatus(isValidUser);
        });

    accountLoginPage.footer.addButton(
        "Create Account",
        e -> {
          pt.swapToPage(makeAccountCreatePage());
        });
    return accountLoginPage;
  }

  // public AccountCreationPage makeAccountCreationPage() {
  //   System.out.println("Redirecting to Account Creation Page");
  // }

  public RecipeListPage makeRecipeListPage(LoginCredentials credentials) {
    String userName = credentials.getUserName();
    String password = credentials.getPassword();
    System.out.println(
        "Redirecting to RecipeListPage for user: " 
        + userName + " and password: " + password);

    System.out.println("Redirecting to RecipeListPage for user: " + userName + " and password: " + password);

    // TODO: need to pass in user to RecipeListPage
    RecipeListPage recipeList = new RecipeListPage(getRecipeListEntries(credentials));
    recipeList.footer.addButton(
        "New Recipe",
        e -> {
          pt.swapToPage(makeNewRecipeController().getPage());
        });
    return recipeList;
  }

  public List<RecipeEntryUI> getRecipeListEntries(LoginCredentials credentials) {
    RecipeListModel model = new RecipeListModel(new HttpRequestModel());
    ArrayList<RecipeEntryUI> entries = new ArrayList<>();
    for (String title : model.getRecipeList()) {
      entries.add(makeRecipeEntryUI(title, credentials));
    }
    return entries;
  }

  public RecipeEntryUI makeRecipeEntryUI(String title, LoginCredentials credentials) {
    RecipeEntryUI entry = new RecipeEntryUI(title);
    entry.addButton(
        "details",
        e -> {
          pt.swapToPage(makeRecipeDetailsPage(title, credentials));
        });
    return entry;
  }

  public RecipeDetailPage makeRecipeDetailsPage(String title, LoginCredentials credentials) {
    RecipeDetailModel rc = new RecipeDetailModel(new HttpRequestModel());
    RecipeDetailPage drp = new RecipeDetailPage(new RecipeDetailUI(rc.read(title)));
    drp.footer.addButton(
        "home",
        e -> {
          pt.swapToPage(makeRecipeListPage(credentials));
        });
    return drp;
  }

  public NewRecipeController makeNewRecipeController() {
    NewRecipePage newRecipePage = new NewRecipePage(new NewRecipeUI());
    NewRecipeModel newRecipeModel = new NewRecipeModel(new HttpRequestModel());
    VoiceToText voiceToText = new WhisperBot();
    return new NewRecipeController(newRecipePage, newRecipeModel, pt, voiceToText);
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
