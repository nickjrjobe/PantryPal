package PantryPal;

/** Class that delegate is Valid User check, given the userName and password */
public class LoginCredentials {
  // temporarily used for loginpage
  private String userName;
  private String password;
  private boolean isValidUser;

  public LoginCredentials(String userName, String password) {
    this.userName = userName;
    this.password = password;
    this.isValidUser = false;
  }

  public boolean isValidUser() {
    if (userName.equals("test") && password.equals("test")) {
      isValidUser = true;
    }
    System.out.println(
        "Checking if user is valid for userName: " + userName + " and password: " + password);
    return isValidUser;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }
}
