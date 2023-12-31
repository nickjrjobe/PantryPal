/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import utils.Account;

/** Model to interact with Server's AccountAPI */
public class AccountModel {
  HttpModel httpModel;

  AccountModel(HttpModel httpModel) {
    this.httpModel = httpModel;
    httpModel.setPath("account");
  }

  public boolean create(Account r) {
    return httpModel.performRequest("POST", null, r.toJSON().toString()).equals("200 OK");
  }

  public void delete(String username) {
    httpModel.performRequest("DELETE", username, null);
  }
}
