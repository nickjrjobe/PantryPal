package PantryPal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import utils.Account;

public class AccountModelTest {
  private MockHttpModel httpModel;
  private AccountModel accountModel;
  Account john = new Account("john", "password");

  @Before
  public void setUp() {
    httpModel = new MockHttpModel();
    accountModel = new AccountModel(httpModel);
  }

  @Test
  public void testCreate() {
    httpModel.setMockResponse("200 OK");
    assertEquals(true, accountModel.create(john));
    assertEquals("POST", httpModel.method);
    assertEquals(john.toJSON().toString(), httpModel.request);
    httpModel.setMockResponse("201 something else");
    assertEquals(false, accountModel.create(john));
    assertEquals("POST", httpModel.method);
    assertEquals(john.toJSON().toString(), httpModel.request);
  }
}
