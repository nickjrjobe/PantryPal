package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Recipe;

interface HttpUserAPIFactory {
  public HttpAPI makeAPI(String username);
}

class UserHandler extends HttpAPI {
  private HttpUserAPIFactory factory;
  private HashMap<String, HttpAPI> apis;

  UserHandler(HttpUserAPIFactory factory) {
    this.factory = factory;
    this.apis = new HashMap<>();
  }

  HttpAPI addAPI(String username) {
    if (!this.apis.containsKey(username)) {
      this.apis.put(username, factory.makeAPI(username));
    }
    return this.apis.get(username);
  }

  String[] makeFields(String query) throws IOException {
    String[] fields = query.split("/");
    if (fields.length == 1) {
      fields = new String[] {fields[0], ""};
    } else if (fields.length != 2) {
      throw new IOException("query did not follow format user/endpoint: " + query);
    }
    return fields;
  }

  String handlePost(String query, String request) throws IOException {
    String[] fields = makeFields(query);
    String username = fields[0];
    String realQuery = fields[1];
    HttpAPI api = addAPI(username);
    return api.handlePost(realQuery, request);
  }

  String handlePut(String query, String request) throws IOException {
    String[] fields = makeFields(query);
    String username = fields[0];
    String realQuery = fields[1];
    HttpAPI api = addAPI(username);
    return api.handlePut(realQuery, request);
  }

  String handleDelete(String query, String request) throws IOException {
    String[] fields = makeFields(query);
    String username = fields[0];
    String realQuery = fields[1];
    HttpAPI api = addAPI(username);
    return api.handleDelete(realQuery, request);
  }

  String handleGet(String query, String request) throws IOException {
    String[] fields = makeFields(query);
    String username = fields[0];
    String realQuery = fields[1];
    HttpAPI api = addAPI(username);
    return api.handleGet(realQuery, request);
  }
}
