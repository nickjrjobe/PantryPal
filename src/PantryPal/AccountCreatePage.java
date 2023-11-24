package PantryPal;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * UI page that contains and reads from the textfield for account and password
 *  */
class AccountCreateUI extends VBox {
  private TextField accountField;
  private PasswordField passwordField;
  private Label errorLabel;

  AccountCreateUI() {
    accountField = new TextField();
    accountField.setPromptText("Account");
    accountField.setPrefWidth(20);
    passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.setPrefWidth(20);

    errorLabel = new Label();
    errorLabel.setTextFill(javafx.scene.paint.Color.RED);

    this.getChildren().addAll(accountField, passwordField,errorLabel);
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

  void setErrorText(String text) {
    errorLabel.setText(text);
  }
}

/**
  * Account creation page that reads from user inputs to UI and checks if the input credentials is valid
 */
public class AccountCreatePage extends ScrollablePage {
  private AccountCreateUI accountCreateUI;

  AccountCreatePage(AccountCreateUI accountCreateUI) {
    super("Create New Account", accountCreateUI);
    this.accountCreateUI = accountCreateUI;

    // this.footer.addButton("Create", e -> handleCreateButtonAction());
    // this.footer.addButton("Create Account", e -> handleCreateAccountButtonAction());
  }

  public boolean isValidCredential() {

    String account = accountCreateUI.getAccountText();
    String password = accountCreateUI.getPasswordText();
    boolean isValidUser = false;
    if (account.isEmpty()) {
      accountCreateUI.setErrorText("Please enter a valid account");
      return isValidUser;
    }
    if (false) {
      // TODO: change the condition  -> check if account already exists
      accountCreateUI.setErrorText("Account already exists");
      return isValidUser;
    }
    accountCreateUI.setErrorText("");
    return true;
  }
}
