package PantryPal;

/** Class that delegate is Valid User check, given the account and password */
public class LoginCredentials {
  // temporarily used for loginpage
  private String account;
  private String password;
  private boolean isValidUser;

  public LoginCredentials(String account, String password) {
    this.account = account;
    this.password = password;
    this.isValidUser = false;
  }

  public boolean isValidUser() {
    if (account.equals("test") && password.equals("test")) {
      isValidUser = true;
    }
    System.out.println(
        "Checking if user is valid for account: " + account + " and password: " + password);
    return isValidUser;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }
}
