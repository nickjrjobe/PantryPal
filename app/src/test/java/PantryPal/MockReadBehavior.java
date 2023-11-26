package PantryPal;

import java.util.ArrayList;
import java.util.List;
import utils.Recipe;

class MockReadBehavior implements ReadBehavior {
  public MockReadBehavior() {
    this.recipes = new ArrayList<Recipe>();
  }

  public List<Recipe> recipes;

  public List<Recipe> read() {
    return recipes;
  }
}
