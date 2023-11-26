package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Recipe;

interface InteractiveRecipeMaker {
  /** get recipe if created, returns null otherwise */
  public Recipe getRecipe();

  /** Reset recipe maker to initial state */
  public void reset();

  /** gets interactive prompts to be displayed to user */
  public List<String> getPrompts();

  /** provide user-created prompt for processing */
  public void readResponse(String response);
}
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
    if (fields.length != 2) {
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
    return api.handlePut(realQuery, request);
  }
  String handleGet(String query, String request) throws IOException {
    String[] fields = makeFields(query);
    String username = fields[0];
    String realQuery = fields[1];
    HttpAPI api = addAPI(username);
    return api.handlePut(realQuery, request);
  }
}

class NewRecipeAPI extends HttpAPI {
  private InteractiveRecipeMaker creator;

  NewRecipeAPI(InteractiveRecipeMaker creator) {
    this.creator = creator;
  }

  public JSONObject makeResponseFromPrompts() {
    JSONObject json = new JSONObject();
    json.put("transcript", new JSONArray(creator.getPrompts()));

    /* if recipe is valid include it in response, then reset */
    if (creator.getRecipe() != null) {
      json.put("recipe", creator.getRecipe().toJSON());
      creator.reset();
    }
    return json;
  }

  /** Write responses */
  String handlePost(String query, String request) throws IOException {
    /* interpret request as JSON */
    JSONObject json = getJSONRequest(request);

    /* get client's prompt response from request's JSON */
    String requestResponse;
    try {
      requestResponse = json.getString("response");
    } catch (Exception e) {
      throw new IOException("Response was invalid");
    }
    /* interpret prompt response(ALLOW errors!) */
    try {
      creator.readResponse(requestResponse);
    } catch (Exception e) {
      System.err.println("response invalid with error " + e.getMessage());
    }

    /* responsd with prompts */
    String response = makeResponseFromPrompts().toString();

    return response;
  }

  /** Reset InteractiveRecipeMaker */
  String handleDelete(String query, String request) throws IOException {
    String response = "200 OK";
    creator.reset();
    return response;
  }

  /** get current prompts */
  String handleGet(String query, String request) throws IOException {
    String response = makeResponseFromPrompts().toString();
    return response;
  }
}
