
package PantryPal;

import java.io.*;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

class AccountCreationUI extends Vbox{
    // input fields account, password
    String account;
    String password;
    AccountCreationUI(String account, String password) {
        this.account = account;
        this.password = password;
        format();
    }
    private void format() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

}
public class AccountCreationPage extends ScrollablePage{
    AccountCreationPage(String account, String password) {
        super("Account Creation", new AccountCreationUI(account, password));
        System.out.println("Account: " + account + " Password: " + password);
    }

}

// /** UI Page containing recipe list, and accompanying header and footer */
// public class RecipeListPage extends ScrollablePage {
//   RecipeListPage(List<RecipeEntryUI> entries) {
//     super("Recipe List", new RecipeListUI(entries));
//   }
// }
