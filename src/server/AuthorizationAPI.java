package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;
import utils.Account;

class AuthorizationAPI extends HttpAPI {
  private AccountData data;

  AuthorizationAPI(AccountData map) {
    this.data = map;
  }

  /** Authorize account */
  String handlePost(String query, String request) throws IOException {
    /* interpret request as json */
    JSONObject json = getJSONRequest(request);
    Account account;
    Account existingAccount;
    try {
      account = new Account(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
    existingAccount = data.get(account.getUsername());
    if (existingAccount == null) {
      return "404 Not Found";
    } else if (!existingAccount.equals(account)) {
      System.out.println(
          "\n\n\n"
              + existingAccount.toJSON().toString()
              + " did not equal "
              + account.toJSON().toString()
              + "\n\n\n");
      return "401 Unauthorized";
    } else {
      System.out.println("ok");
      return "200 OK";
    }
  }
}
