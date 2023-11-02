package PantryPal;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileReadBehaviorTest {
	@Test
	public void testRead() {
		ReadBehavior reader = new FileReadBehavior("test/PantryPal/filereadtest.txt");

		List<Recipe> recipes = reader.read();
		assertEquals(2, recipes.size());
		assertEquals("Sandwich", recipes.get(0).getTitle());
		assertEquals("contains mayo", recipes.get(0).getDescription());
		assertEquals("Salami", recipes.get(1).getTitle());
		assertEquals("from beef", recipes.get(1).getDescription());
	}
}
