package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
class NewRecipeAPI extends HttpAPI {
  private NewRecipeCreator creator;

  NewRecipeAPI(NewRecipeCreator creator) {
    this.creator = creator;
  }

  public JSONObject makeResponse() {
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
  String handlePost(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    String postData = scanner.nextLine();
    JSONObject json;
    Recipe recipe;
    System.out.println("request: " + postData);
    /* interpret request as json */
    try {
      json = new JSONObject(postData);
    } catch (Exception e) {
      throw new IOException("Response was not JSON");
    }
    String requestResponse;
    /* get response from json */
    try {
      requestResponse = json.getString("response");
    } catch (Exception e) {
      throw new IOException("Response was invalid");
    }
    /* interpret response (ALLOW errors!) */
    try {
      creator.readResponse(requestResponse);
    } catch (Exception e) {
      System.err.println("response invalid with error " + e.getMessage());
    }
    String response = makeResponse().toString();
    scanner.close();

    return response;
  }

  /** Reset NewRecipeCreator */
  String handleDelete(HttpExchange httpExchange) throws IOException {
    String response = "200 OK";
    creator.reset();
    return response;
  }

  /** get current prompts */
  String handleGet(HttpExchange httpExchange) throws IOException {
    String response = makeResponse().toString();
    return response;
  }
}
