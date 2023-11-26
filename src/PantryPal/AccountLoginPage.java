package PantryPal;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import utils.Account;

/**
 * UI page that contains and reads from the textfield for account and password, and the checkbox for
 * auto login
 */
class AccountLoginUI extends VBox {
  private TextField userNameField;
  private PasswordField passwordField;
  private CheckBox autoLoginCheckbox;
  private Label errorLabel;

  AccountLoginUI() {
    userNameField = new TextField();
    userNameField.setPromptText("User Name");
    userNameField.setPrefWidth(20);
    passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.setPrefWidth(20);

    autoLoginCheckbox = new CheckBox("Automatic login");

    errorLabel = new Label();
    errorLabel.setTextFill(javafx.scene.paint.Color.RED);

    this.getChildren().addAll(userNameField, passwordField, autoLoginCheckbox, errorLabel);
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
  Account getAccount() {
    return new Account(userNameField.getText(), passwordField.getText());
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
    return accountLoginUI.getUserNameText();
  }

  public String getPassword() {
    return accountLoginUI.getPasswordText();
  }
}
