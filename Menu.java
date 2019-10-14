
public class Menu {

    //First level menu
    public void mainMenu() {
        System.out.println();
        System.out.println("Welcome to FanDB !");
        System.out.println("------------------");
        System.out.println("1. Show Databases");
        System.out.println("2. Use Database");
        System.out.println("3. Create Database");
        System.out.println("4. Exit");
        System.out.println();
    }
    
    //Second level menu
    public void secondMenu(Database b) {
        System.out.println();
        System.out.println("What do you want to do with \"" + b.getName() + "\"?");
        System.out.println("1. Show Tables");
        System.out.println("2. Create Table");
        System.out.println("3. Drop Table");
        System.out.println("4. Use Table");
        System.out.println("5. Create table from file");
        System.out.println("6. Go Back to Main Menu");
    }
    
    //Third level menu
    public void thirdMenu(Table t) {
        System.out.println();
        System.out.println("What do you want to do with  \"" + t.getName() + "\"?");
        System.out.println("1. Show Data");
        System.out.println("2. Insert Data");
        System.out.println("3. Delete Data by Index");
        System.out.println("4. Delete Data by PK");
        System.out.println("5. Modify Data");
        System.out.println("6. Set PK");
        System.out.println("7. Set FK");
        System.out.println("8. Remove a column");
        System.out.println("9. Add a column");
        System.out.println("10. Select a tuple");
        System.out.println("11. Store Table to a file'");
        System.out.println("12. Back");
    }
    
    public static void main(String[] args) {
        
    }
}
