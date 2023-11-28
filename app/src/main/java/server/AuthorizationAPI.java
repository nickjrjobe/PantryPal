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
      return "401 Unauthorized";
    } else {
      return "200 OK";
    }
  }
}
