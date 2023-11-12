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
  String handlePost(HttpExchange httpExchange) throws IOException {
    /* interpret request as JSON */
    JSONObject json = getJSONRequest(httpExchange);

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

  /** Reset NewRecipeCreator */
  String handleDelete(HttpExchange httpExchange) throws IOException {
    String response = "200 OK";
    creator.reset();
    return response;
  }

  /** get current prompts */
  String handleGet(HttpExchange httpExchange) throws IOException {
    String response = makeResponseFromPrompts().toString();
    return response;
  }
}
