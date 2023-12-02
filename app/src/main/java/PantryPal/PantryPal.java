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
import utils.Account;
import utils.Recipe;

interface HomeTracker {
  public ScrollablePage getHome();
}

class AppController implements HomeTracker {
  private Account account;
  private PageTracker pt;
  private final String CREDENTIALS = "Secure_Credentials.txt";

  public AppController(PageTracker pt) {
    this.pt = pt;
  }

  public ScrollablePage getHome() {
    // US 11: Check if autologin exists and if so autoswap to home page
    try {
      File file = new File(CREDENTIALS);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String username = br.readLine();
      String password = br.readLine();
      br.close();
      fr.close();
      // Set the text fields of the UI
      account = new Account(username, password);
    } catch (IOException ex) {
      System.out.println("Error reading from file");
    }
    // Check if account exists
    if (account == null) {
      return makeLoginPage();
    } else {
      return makeRecipeListPage();
    }
  }

  public boolean makeAccount(AccountCreateUI accountCreateUI, AccountModel accountModel) {
    Account account = accountCreateUI.getAccount();
    if (!Account.isValidUsername(account.getUsername())) {
      accountCreateUI.setErrorText("Please enter a valid username");
      return false;
    }
    if (!accountModel.create(account)) {
      accountCreateUI.setErrorText("Username already exists");
      return false;
    }
    return true;
  }

  /**
   * Validates the account and sets the error text if the account is invalid
   *
   * @param accountLoginUI the UI to validate
   * @param authorizationModel the model to validate against
   * @return true if the account is valid, false otherwise
   */
  public boolean validateAccount(
      AccountLoginUI accountLoginUI, AuthorizationModel authorizationModel) {
    Account account = accountLoginUI.getAccount();
    if (!Account.isValidUsername(account.getUsername())) {
      accountLoginUI.setErrorText("Please enter a valid username");
      return false;
    }
    if (!authorizationModel.authenticate(account)) {
      accountLoginUI.setErrorText("Invalid username/password");
      return false;
    }
    return true;
  }

  public AccountCreatePage makeAccountCreatePage() {
    AccountCreateUI accountCreateUI = new AccountCreateUI();
    AccountCreatePage accountCreatePage = new AccountCreatePage(accountCreateUI);
    accountCreatePage.footer.addButton(
        "Create Account",
        e -> {
          boolean madeAccount =
              makeAccount(accountCreateUI, new AccountModel(new HttpRequestModel()));
          if (madeAccount) {
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
          Account account =
              new Account(accountLoginUI.getUserNameText(), accountLoginUI.getPasswordText());
          boolean loggedIn =
              validateAccount(accountLoginUI, new AuthorizationModel(new HttpRequestModel()));

          if (loggedIn) {
            this.account = accountLoginUI.getAccount();
            // Check if loginValid && autoLogin selected
            if (accountLoginUI.isAutoLoginSelected()) {
              // Save credentials to file DontLookHere.txt
              // Next time when opening app, we will check for this file
              try {
                File file = new File(CREDENTIALS);
                FileWriter fr = new FileWriter(file, false);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(account.getUsername() + "\n");
                br.write(account.getPassword());
                br.close();
                fr.close();
              } catch (IOException ex) {
                System.out.println("Error writing to file");
              }
            }
            pt.swapToPage(makeRecipeListPage()); // Swap to recipe list page
          }
        });

    accountLoginPage.footer.addButton(
        "Create Account",
        e -> {
          pt.swapToPage(makeAccountCreatePage());
        });
    return accountLoginPage;
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
    RecipeListModel model = new RecipeListModel(new HttpRequestModel(), account);
    ArrayList<RecipeEntryUI> entries = new ArrayList<>();
    ArrayList<Recipe> recipes = new ArrayList<>();

    for (Recipe recipe : model.getRecipeList()) {
      entries.add(makeRecipeEntryUI(recipe));
    }

    return entries;
  }

  public RecipeEntryUI makeRecipeEntryUI(Recipe recipe) {
    RecipeEntryUI entry = new RecipeEntryUI(recipe);
    entry.addButton(
        "details",
        e -> {
          pt.swapToPage(makeRecipeDetailsPage(recipe.getTitle()));
        });
    return entry;
  }

  public RecipeDetailPage makeRecipeDetailsPage(String title) {
    RecipeDetailModel rc = new RecipeDetailModel(new HttpRequestModel(), account);
    RecipeDetailPage drp = new RecipeDetailPage(new RecipeDetailUI(rc.read(title), rc));
    drp.footer.addButton(
        "home",
        e -> {
          pt.swapToPage(makeRecipeListPage());
        });
    return drp;
  }

  public NewRecipeController makeNewRecipeController() {
    NewRecipePage newRecipePage = new NewRecipePage(new NewRecipeUI());
    NewRecipeModel newRecipeModel = new NewRecipeModel(new HttpRequestModel(), account);
    VoiceToText voiceToText = new WhisperBot();
    return new NewRecipeController(newRecipePage, newRecipeModel, pt, voiceToText, account);
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
