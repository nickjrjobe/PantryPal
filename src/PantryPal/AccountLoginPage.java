package PantryPal;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * UI page that contains and reads from the textfield for account and password, and the checkbox for
 * auto login
 */
class AccountLoginUI extends VBox {
  private TextField accountField;
  private PasswordField passwordField;
  private CheckBox autoLoginCheckbox;
  private Label errorLabel;

  AccountLoginUI() {
    accountField = new TextField();
    accountField.setPromptText("Account");
    accountField.setPrefWidth(20);
    passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.setPrefWidth(20);

    autoLoginCheckbox = new CheckBox("Automatic login");

    errorLabel = new Label();
    errorLabel.setTextFill(javafx.scene.paint.Color.RED);

    this.getChildren().addAll(accountField, passwordField, autoLoginCheckbox, errorLabel);
    format();
  }

  private void format() {
    this.setSpacing(10);
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  String getAccountText() {
    return accountField.getText();
  }

  String getPasswordText() {
    return passwordField.getText();
  }

  boolean isAutoLoginSelected() {
    return autoLoginCheckbox.isSelected();
  }

  void setErrorText(String text) {
    errorLabel.setText(text);
  }
}

/**
 * Login Page that delegate credentials validity checks, updates the autologin choice, acquire
 * credentials from the Ui components.
 */
public class AccountLoginPage extends ScrollablePage {
  private AccountLoginUI accountLoginUI;

  AccountLoginPage(AccountLoginUI accountLoginUI) {
    super("Login", accountLoginUI);
    this.accountLoginUI = accountLoginUI;

    // this.footer.addButton("Login", e -> handleLoginButtonAction());
    // this.footer.addButton("Create Account", e -> handleCreateAccountButtonAction());
  }

  public String getAccount() {
    return accountLoginUI.getAccountText();
  }

  public String getPassword() {
    return accountLoginUI.getPasswordText();
  }

  public boolean isValidCredential() {

    String account = accountLoginUI.getAccountText();
    String password = accountLoginUI.getPasswordText();
    // boolean isValidUser = false;
    LoginCredentials credentials = new LoginCredentials(account, password);
    boolean isValidUser = credentials.isValidUser();
    if (account.isEmpty()) {
      accountLoginUI.setErrorText("Please enter a valid account");
      return isValidUser;
    }
    if (!isValidUser) {
      accountLoginUI.setErrorText("Incorrect account or password");
    } else {
      accountLoginUI.setErrorText("");
    }
    return isValidUser;
  }

  public void writeAutoLoginStatus(boolean isSelected) {
    // US11 TODO: write the saved account and password to a file instead
    System.out.println("Auto login is " + (isSelected ? "selected" : "not selected"));
  }
}
