package server;

import java.io.IOException;
import java.util.List;

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

  /** regenerates recipe if valid */
  public Recipe regenerateRecipe();
}

class NewRecipeAPIFactory implements HttpUserAPIFactory {
  private RecipeCreator creator;

  NewRecipeAPIFactory(RecipeCreator creator) {
    this.creator = creator;
  }

  public HttpAPI makeAPI(String username) {
    return new NewRecipeAPI(new NewRecipeCreator(creator));
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

  public JSONObject makeRegenerateResponse() {
    JSONObject json = new JSONObject();
    /* if recipe is valid include it in response, then reset */
    if (creator.regenerateRecipe() != null) {
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
    String response = "400 Bad Request";
    System.err.println("Query: "+ query);
    if(query.equals("?prompts")){
      response = makeResponseFromPrompts().toString();
    } else if(query.equals("?regenerate")){
      response = makeRegenerateResponse().toString();

    }
    return response;
  }
}
