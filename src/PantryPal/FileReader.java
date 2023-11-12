package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File based reader to read in RecipeList
 *
 * <p>Format: one recipe represented by two lines, where first line is title and second line is
 * description. Every two lines in a file represent one recipe.
 */
class FileReadBehavior implements ReadBehavior {
  private String filename;

  /**
   * Instantiates FileReadBehavior
   *
   * @param filename file from which to read to, must have even number of lines
   */
  public FileReadBehavior(String filename) {
    this.filename = filename;
  }

  /**
   * Reads Recipelist from file provided in constructor
   *
   * @return recipe list read from file, null if file I/O fails
   */
  public List<Recipe> read() {
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    FileReader f;
    BufferedReader reader;
    try {
      f = new FileReader(this.filename);
      reader = new BufferedReader(f);
      /* read lines in batches of two */
      for (String line1 = reader.readLine(), line2 = reader.readLine();
          line1 != null && line2 != null;
          line1 = reader.readLine(), line2 = reader.readLine()) {
        /*
         *  read a recipe in where first line is title and second line is
         * description
         */
        recipes.add(new Recipe(line1, line2));
      }
    } catch (IOException e) {
      return null;
    }
    try {
      reader.close();
      f.close();
    } catch (IOException e) {
      System.err.println("file not properly closed");
    }
    return recipes;
  }
}
