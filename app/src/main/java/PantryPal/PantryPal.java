/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import utils.Account;
import utils.Recipe;
import utils.VoiceToText;

interface HomeTracker {
  public ScrollablePage getHome();

  public ScrollablePage getError();
}

class AppController implements HomeTracker {
  private Account account;
  private PageTracker pt;
  private LinkMaker linkMaker;
  private String filterSelection; // TODO can we delete these
  private String sortSelection;
  private static final String CREDENTIALS = "Secure_Credentials.txt";

  public AppController(PageTracker pt, LinkMaker linkMaker) {
    this.pt = pt;
    this.linkMaker = linkMaker;
    this.account = checkForAutoLogin(CREDENTIALS);
  }

  public ScrollablePage getError() {
    return makeErrorPage();
  }

  public ScrollablePage getHome() {
    // Check if account exists
    if (account == null) {
      return makeLoginPage();
    } else {
      return makeRecipeListPage();
    }
  }

  /** Checks if autologin exists, then sets up account */
  public static Account checkForAutoLogin(String credentials_file_path) {
    try {
      File file = new File(credentials_file_path);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String username = br.readLine();
      String password = br.readLine();
      br.close();
      fr.close();
      // Set the text fields of the UI
      Account account = new Account(username, password);
      return account;
    } catch (IOException ex) {
      System.out.println("Error reading from file");
      return null;
    }
  }

  public static boolean checkIfAutoLoginExists(String credentials_file_path) {
    try {
      File file = new File(credentials_file_path);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String username = br.readLine();
      String password = br.readLine();
      br.close();
      fr.close();
      return true;
    } catch (IOException ex) {
      return false;
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
      System.err.println("Empty username");
      return false;
    }
    if (!authorizationModel.authenticate(account)) {
      accountLoginUI.setErrorText("Invalid username/password");
      System.err.println("Invalid username/password");
      return false;
    }
    return true;
  }

  public ServerErrorPage makeErrorPage() {
    ServerErrorUI serverErrorUI = new ServerErrorUI();
    ServerErrorPage serverErrorPage = new ServerErrorPage(serverErrorUI);

    serverErrorPage.footer.addButton(
        "Refresh",
        e -> {
          if (serverErrorUI.tryConnect()) {
            System.err.println("Server is on");
            pt.goHome();
          } else {
            System.err.println("Server still down");
            pt.goError();
          }
        });

    return serverErrorPage;
  }

  public void logout() {
    this.account = null;
    pt.swapToPage(makeLoginPage());
    if (checkIfAutoLoginExists(CREDENTIALS) == true) {
      File file = new File(CREDENTIALS);
      file.delete();
    }
  }

  public AccountCreatePage makeAccountCreatePage() {
    AccountCreateUI accountCreateUI = new AccountCreateUI();
    AccountCreatePage accountCreatePage = new AccountCreatePage(accountCreateUI);
    accountCreatePage.footer.addButton(
        "Create Account",
        e -> {
          HttpRequestModel httpModel = new HttpRequestModel();
          httpModel.registerObserver(pt);
          boolean madeAccount = makeAccount(accountCreateUI, new AccountModel(httpModel));
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
          HttpRequestModel httpModel = new HttpRequestModel();
          httpModel.registerObserver(pt);
          boolean loggedIn = validateAccount(accountLoginUI, new AuthorizationModel(httpModel));

          if (loggedIn) {
            this.account = accountLoginUI.getAccount();
            login(accountLoginUI);
          }
        });

    accountLoginPage.footer.addButton(
        "Create Account",
        e -> {
          pt.swapToPage(makeAccountCreatePage());
        });
    return accountLoginPage;
  }

  public void login(AccountLoginUI accountLoginUI) {
    if (accountLoginUI.isAutoLoginSelected()) {
      saveAutoLoginDetails(account, CREDENTIALS);
    }
    pt.swapToPage(makeRecipeListPage()); // Swap to recipe list page
  }

  public void saveAutoLoginDetails(Account account, String credentials_file_path) {
    // Saves given account credentials to specified file
    try {
      File file = new File(credentials_file_path);
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

  public SharePage makeSharePage(String title) {
    String link = linkMaker.makeLink(title, account);
    SharePage sharePage = new SharePage(link);
    sharePage.footer.addButton(
        "exit",
        e -> {
          pt.swapToPage(makeRecipeDetailsPage(title));
        });
    return sharePage;
  }

  public RecipeListPage makeRecipeListPage() {
    return makeRecipeListPage("No Filters", "Newest");
  }

  /**
   * Make a RecipeListPage with the specified filter and sorting mode
   *
   * @param filterSelection the filter to use
   * @param sortSelection the sorting mode to use
   * @return the RecipeListPage
   */
  public RecipeListPage makeRecipeListPage(
      final String filterSelection, final String sortSelection) {
    // Define the filter and sorting options
    List<String> mealTypes = Arrays.asList("No Filters", "breakfast", "lunch", "dinner");
    List<String> sorts = Arrays.asList("A-Z", "Z-A", "Oldest", "Newest");
    // Fetch the list of recipes using the options
    RecipeListPage recipeList =
        new RecipeListPage(getRecipeListEntries(filterSelection, sortSelection));
    // New Recipe Button, click sends to new recipe page
    recipeList.footer.addButton(
        "New Recipe",
        e -> {
          pt.swapToPage(makeNewRecipeController().getPage());
        });
    recipeList.footer.addButton(
        "logout",
        e -> {
          logout();
        });

    // Filter Dropdown, click sends to this same page with updated filters
    EventHandler<ActionEvent> filterEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            // Get the new specified filter
            ChoiceBox<String> combo_box = (ChoiceBox<String>) e.getSource();
            String filterSelection = combo_box.getValue();
            System.out.println("Filter: " + filterSelection + " selected");
            // Swap to the new page with the new filter
            pt.swapToPage(makeRecipeListPage(filterSelection, sortSelection));
          }
        };
    recipeList.footer.addDropDown(mealTypes, filterSelection, filterEventHandler);

    // Sort Dropdown, click sends to this same page with updated sorting mode
    EventHandler<ActionEvent> sortEventHandler =
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            // Get the specified sorting mode
            ChoiceBox<String> combo_box = (ChoiceBox<String>) e.getSource();
            String sortSelection = combo_box.getValue();
            System.out.println("Sorting mode: " + sortSelection + " selected");
            // Swap to the new page with the new sorting mode
            pt.swapToPage(makeRecipeListPage(filterSelection, sortSelection));
          }
        };
    recipeList.footer.addDropDown(sorts, sortSelection, sortEventHandler);

    return recipeList;
  }

  public String getChoiceBoxSelection(ChoiceBox dropDown) {
    return dropDown.getSelectionModel().getSelectedItem().toString();
  }

  public List<RecipeEntryUI> convertRecipeListToUIList(List<Recipe> recipes) {
    ArrayList<RecipeEntryUI> entries = new ArrayList<>();
    for (Recipe recipe : recipes) {
      entries.add(makeRecipeEntryUI(recipe));
    }
    return entries;
  }

  public void sortRecipesInPlace(List<Recipe> recipes, String sortSelection) {
    // Apply sorting
    if (sortSelection.equals("A-Z")) {
      recipes.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
    } else if (sortSelection.equals("Z-A")) {
      recipes.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));
    } else if (sortSelection.equals("Oldest")) {
      recipes.sort((a, b) -> Integer.compare(a.getCreationTimestamp(), b.getCreationTimestamp()));
    } else if (sortSelection.equals("Newest")) {
      recipes.sort((a, b) -> Integer.compare(b.getCreationTimestamp(), a.getCreationTimestamp()));
    }
  }

  public List<RecipeEntryUI> getRecipeListEntries(String filterSelection, String sortSelection) {
    HttpRequestModel httpModel = new HttpRequestModel();
    httpModel.registerObserver(pt);
    RecipeListModel model = new RecipeListModel(httpModel, account);
    List<Recipe> recipes;
    // Get filtered recipes
    if (filterSelection.equals("No Filters")) {
      recipes = model.getRecipeList(sortSelection);
    } else {
      recipes = model.getMealTypeRecipeList(filterSelection.toLowerCase(), sortSelection);
    }
    // Debugging
    System.out.println("Recipe list length: " + recipes.size());
    for (Recipe recipe : recipes) {
      System.out.println("    " + recipe.getTitle());
    }
    // Convert to UI list for the UI
    return convertRecipeListToUIList(recipes);
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
    HttpRequestModel httpModelRc = new HttpRequestModel();
    httpModelRc.registerObserver(pt);
    RecipeDetailModel rc = new RecipeDetailModel(httpModelRc, account);
    HttpRequestModel httpModelDrp = new HttpRequestModel();
    httpModelDrp.registerObserver(pt);
    RecipeDetailPage drp =
        new RecipeDetailPage(
            new RecipeDetailUI(rc.read(title), rc, new ImageModel(httpModelDrp, account)));
    drp.footer.addButton(
        "home",
        e -> {
          pt.swapToPage(makeRecipeListPage());
        });
    drp.footer.addButton(
        "share",
        e -> {
          pt.swapToPage(makeSharePage(title));
        });
    return drp;
  }

  public NewRecipeController makeNewRecipeController() {
    NewRecipePage newRecipePage = new NewRecipePage(new NewRecipeUI());

    HttpRequestModel httpModelNr = new HttpRequestModel();
    httpModelNr.registerObserver(pt);
    NewRecipeModel newRecipeModel = new NewRecipeModel(httpModelNr, account);

    HttpRequestModel httpModelVtt = new HttpRequestModel();
    httpModelVtt.registerObserver(pt);
    VoiceToText voiceToText = new WhisperModel(httpModelVtt, account);
    return new NewRecipeController(newRecipePage, newRecipeModel, pt, voiceToText, account);
  }
}

interface ServerObserver {
  void updateServer(boolean connected);
}

/*
 * Object which handles which Page is currently displayed
 */
class PageTracker implements ServerObserver {
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

  @Override
  public void updateServer(boolean connected) {
    if (connected) this.goHome();
    else this.goError();
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

  void goError() {
    swapToPage(homeTracker.getError());
  }
}

/** Runner class which starts the UI */
public class PantryPal extends Application {
  RecipeListPage recipelist;
  PageTracker pageTracker;

  @Override
  public void start(Stage primaryStage) throws Exception {
    PageTracker pt = new PageTracker(primaryStage);
    AppController appController = new AppController(pt, new ShareLinkMaker());
    pt.setHomeTracker(appController);
    // Check if server is on
    HttpModel httpModel = new HttpRequestModel();
    if (!httpModel.tryConnect()) {
      pt.goError();
    } else {
      pt.goHome();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
