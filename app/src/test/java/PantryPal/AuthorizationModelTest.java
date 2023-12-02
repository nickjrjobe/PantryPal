package PantryPal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Account;

public class AuthorizationModelTest {
  private MockHttpModel httpModel;
  private AuthorizationModel recipeListModel;

  @BeforeEach
  public void setUp() {
    httpModel = new MockHttpModel();
    recipeListModel = new AuthorizationModel(httpModel);
  }

  @Test
  public void testAuthenticate() {
    httpModel.setMockResponse("200 OK");
    assertEquals(true, recipeListModel.authenticate(new Account("", "")));
    httpModel.setMockResponse("401 Unauthorized");
    assertEquals(false, recipeListModel.authenticate(new Account("", "")));
    httpModel.setMockResponse("404 Not Found");
    assertEquals(false, recipeListModel.authenticate(new Account("", "")));
    httpModel.setMockResponse("999 Not A Real Code");
    assertEquals(false, recipeListModel.authenticate(new Account("", "")));
  }

  @Test
  public void testTryConnect() {
    assertEquals(false, recipeListModel.ifConnected(new Account("", "")));
  }
}
