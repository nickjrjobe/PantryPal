package PantryPal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.*;
import org.junit.jupiter.api.Test;
import utils.Account;

public class AutoLoginTest {

  @Test
  public void testCheckForAutoLogin_Success() {
    // Create a temporary credentials file
    String credentialsFilePath = "src/test/temp_credentials.txt";
    try {
      FileWriter fw = new FileWriter(credentialsFilePath);
      fw.write("testusername\n");
      fw.write("testpassword\n");
      fw.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    // Call the checkForAutoLogin method
    Account account = AppController.checkForAutoLogin(credentialsFilePath);

    // Check that the username and password are correct
    assertEquals("testusername", account.getUsername());
    assertEquals("testpassword", account.getPassword());
  }

  @Test
  public void testCheckForAutoLogin_FileNotFound() {
    // Call the checkForAutoLogin method with a non-existent file path
    Account account = AppController.checkForAutoLogin("src/test/nonexistent_file.txt");
    assertNull(account);
  }

  @Test
  public void testCheckIfAutoLoginExists() {
    String credentialsFilePath = "src/test/temp_credentials.txt";
    try {
      FileWriter fw = new FileWriter(credentialsFilePath);
      fw.write("testusername\n");
      fw.write("testpassword\n");
      fw.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    assertEquals(true, AppController.checkIfAutoLoginExists(credentialsFilePath));
    assertEquals(false, AppController.checkIfAutoLoginExists("src/test/nonexistent_file.txt"));
  }
}
