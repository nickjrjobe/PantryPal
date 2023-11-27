/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import utils.Account;

/** Model to interact with Server's AuthorizationAPI * */
public class AuthorizationModel {
  HttpModel httpModel;

  AuthorizationModel(HttpModel httpModel) {
    this.httpModel = httpModel;
    httpModel.setPath("authorization");
  }

  public boolean authenticate(Account account) {
    String res = httpModel.performRequest("POST", null, account.toJSON().toString());
    if (res.equals("200 OK")) {
      return true;
    } else if (res.equals("401 Unauthorized")) {
      return false;
    } else if (res.equals("404 Not Found")) {
      return false;
    } else {
      System.err.println("unexpected response from AuthorizationAPI: " + res);
      return false;
    }
  }
}
