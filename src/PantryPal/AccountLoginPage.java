/** UI Page containing login form, and accompanying header and footer */
package PantryPal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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

public class AccountLoginPage extends ScrollablePage {
  private AccountLoginUI AccountLoginUI;

  AccountLoginPage(AccountLoginUI AccountLoginUI) {
    super("Login", AccountLoginUI);
    this.AccountLoginUI = AccountLoginUI;

    // this.footer.addButton("Login", e -> handleLoginButtonAction());
    // this.footer.addButton("Create Account", e -> handleCreateAccountButtonAction());
  }

  public String getAccount() {
    return AccountLoginUI.getAccountText();
  }

  public String getPassword() {
    return AccountLoginUI.getPasswordText();
  }

  public boolean isValidCredential() {

    String account = AccountLoginUI.getAccountText();
    String password = AccountLoginUI.getPasswordText();
    // boolean isValidUser = false;
    LoginCredentials credentials = new LoginCredentials(account, password);
    boolean isValidUser = credentials.isValidUser();
    if (account.isEmpty()) {
      AccountLoginUI.setErrorText("Please enter a valid account");
      return isValidUser;
    }
    if (!isValidUser) {
      AccountLoginUI.setErrorText("Incorrect account or password");
    } else {
      AccountLoginUI.setErrorText("");
    }
    return isValidUser;
  }

  public void writeAutoLoginStatus(boolean isSelected) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("autologin.txt"))) {
      writer.write(isSelected ? "1" : "0");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
