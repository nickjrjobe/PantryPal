package PantryPal;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class RecipeListTest {
	@Test
	public void testRead() {
		MockReadBehavior rb = new MockReadBehavior();

		/* create and add recipes r1 then r2 */
		Recipe r1 = new Recipe();
		rb.recipes.add(r1);
		Recipe r2 = new Recipe();
		rb.recipes.add(r2);

		/* create recipelist */
		RecipeList rlist = new RecipeList(rb);

		/* ensure reading is not done till explicitly requested */
		assertEquals(0, rlist.getRecipes().size());

		rlist.read();

		/* ensure top element is r1, and below that is r2 */
		assertEquals(2, rlist.getRecipes().size());
		assertEquals(rlist.getRecipes().get(0),r1);
		assertEquals(rlist.getRecipes().get(1),r2);
	}
}
