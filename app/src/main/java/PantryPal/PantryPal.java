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
import utils.VoiceToText;

interface HomeTracker {
  public ScrollablePage getHome();

  public ScrollablePage getError();
}

class AppController implements HomeTracker {
  private Account account;
  private PageTracker pt;
  private LinkMaker linkMaker;
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
      System.out.println("Error reading from file");
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
    RecipeListPage recipeList = new RecipeListPage(getRecipeListEntries());
    recipeList.footer.addButton(
        "New Recipe",
        e -> {
          pt.swapToPage(makeNewRecipeController().getPage());
        });
    if (checkIfAutoLoginExists(CREDENTIALS) == false) {
      recipeList.footer.addButton(
          "logout",
          e -> {
            logout();
          });
    }
    return recipeList;
  }

  public List<RecipeEntryUI> getRecipeListEntries() {
    HttpRequestModel httpModel = new HttpRequestModel();
    httpModel.registerObserver(pt);
    RecipeListModel model = new RecipeListModel(httpModel, account);
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
    pt.goHome();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
