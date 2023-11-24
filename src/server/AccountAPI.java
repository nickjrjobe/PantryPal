package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;
import utils.Account;

class AccountAPI extends HttpAPI {
  private AccountData data;

  DetailedAccountAPI(AccountData map) {
    this.data = map;
  }

  /** Create a new account */
  String handlePost(String query, String request) throws IOException {
    /* interpret request as json */
    String response = "406 Not Acceptable";
    JSONObject json = getJSONRequest(request);
    Account account;
    try {
      account = new Account(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
    if (data.get(account.getUsername()) == null) {
      data.put(account.getUsername(), account);
      response = "200 Ok";
    }
    return response;
  }

  /** Update account */
  String handlePut(String query, String request) throws IOException {
    return handlePost(query, request);
  }

  /** Delete account */
  String handleDelete(String query, String request) throws IOException {
    String response = "404 Not Found";
    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      Account account = data.remove(value); // Retrieve data from hashmap
      if (account != null) {
        response = "200 OK";
      }
    }
    return response;
  }
}
