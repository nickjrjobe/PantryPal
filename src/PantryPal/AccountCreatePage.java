package PantryPal;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/** UI page that contains and reads from the textfield for account and password */
class AccountCreateUI extends VBox {
  private TextField userNameField;
  private PasswordField passwordField;
  private Label errorLabel;

  AccountCreateUI() {
    userNameField = new TextField();
    userNameField.setPromptText("Username");
    userNameField.setPrefWidth(20);
    passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.setPrefWidth(20);

    errorLabel = new Label();
    errorLabel.setTextFill(javafx.scene.paint.Color.RED);

    this.getChildren().addAll(userNameField, passwordField, errorLabel);
    format();
  }

  private void format() {
    this.setSpacing(10);
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  String getUserNameText() {
    return userNameField.getText();
  }

  String getPasswordText() {
    return passwordField.getText();
  }

  void setErrorText(String text) {
    errorLabel.setText(text);
  }
}

/**
 * Account creation page that reads from user inputs to UI and checks if the input credentials is
 * valid
 */
public class AccountCreatePage extends ScrollablePage {
  private AccountCreateUI accountCreateUI;

  AccountCreatePage(AccountCreateUI accountCreateUI) {
    super("Create New Account", accountCreateUI);
    this.accountCreateUI = accountCreateUI;
  }

  public boolean isValidCredential() {

    String account = accountCreateUI.getUserNameText();
    String password = accountCreateUI.getPasswordText();
    boolean isValidInput = false;
    if (account.isEmpty()) {
      accountCreateUI.setErrorText("Please enter a valid username");
      return isValidInput;
    }
    if (account.equals("existingUser")) {
      // TODO: change the condition  -> check if account already exists
      accountCreateUI.setErrorText("Username already exists");
      return isValidInput;
    }
    accountCreateUI.setErrorText("");
    return true;
  }

  String getUserName() {
    return accountCreateUI.getUserNameText();
  }

  String getPassword() {
    return accountCreateUI.getPasswordText();
  }
}
