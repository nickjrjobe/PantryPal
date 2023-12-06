package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;
import utils.Account;

class ConnectAPI extends HttpAPI {
  /** Authorize account */
  String handleGet(String query, String request) throws IOException {
    return "200 OK";
  }
}
