package PantryPal;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import utils.Account;

public class AuthorizationModelTest {
    private MockHttpModel httpModel;
    private AuthorizationModel recipeListModel;

    @Before
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
}
