import java.util.*;

public class Database {
    private String name;
    private ArrayList<Table> tables;
    
    //trivial constructor
    public Database() {
        this.name = null;
        this.tables = new ArrayList<Table>();
    }
    
    //constructor to initialise a Database object with given name
    public Database(String name) {
        this.name = name;
        this.tables = new ArrayList<Table>();
    }
    
    //setter method for property name
    public void setName(String name) {
        this.name = name;
    }
    
    //getter method for property name
    public String getName() {
        return this.name;
    }
    
    //getter method for property tables;
    public ArrayList<Table> getTables(){
        return this.tables;
    }
    
    //add a table with given name and attributes
    public boolean addTable(String name, String[] fields) {
        int cnt = 0;
        
        for(int i = 0; i < this.tables.size(); i++) {
            if(this.tables.get(i).getName().equals(name)) {
                cnt++;
            }
        }
        if(cnt == 0) {
            Table t = new Table(fields);
            t.setName(name);
            this.tables.add(t);
            return true;
        }else {
            System.out.println("Naming conflict !");
            return false;
        }
    }
    
    //drop a table (FK constraint is checked)
    public boolean dropTable(String name) {
        Table tForRemove = this.getTableByName(name);
        
        if(tForRemove.getReferencedBy().isEmpty()) {
            Iterator<Table> it = tForRemove.getForeignKey().keySet().iterator();
            while(it.hasNext()) {
                it.next().getReferencedBy().remove(tForRemove);
            }
            tForRemove.getForeignKey().clear();
            tForRemove.getLink().clear();
            this.tables.remove(tForRemove);
            return true;
        }else {
            System.out.println("This table is refered by other tables, "
                    + "you cannot drop this table !");
            return false;
        }
        
    }
    
    //get a Table object by its name
    public Table getTableByName(String s) {
        for(int i = 0; i < this.tables.size(); i++) {
            if(this.tables.get(i).getName().equals(s)) {
                return this.tables.get(i);
            }
        }
        System.out.println("Table " + s + " not found !");
        return null;
    }
    
    //printing tables in this databse neatly
    public void showTables() {
        if(this.tables == null || this.tables.size() == 0) {
            System.out.println("There is no tables !");
            return;
        }
        int length = this.getName().length();
        for(int i = 0; i < this.tables.size(); i++) {
            if(this.tables.get(i).getName().length() >= length) {
                length = this.tables.get(i).getName().length();
            }
        }
        length = length + 4;
        for(int i = 0; i < (length + 2); i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.print("|");
        for(int i = 0; i < (length - this.getName().length()) / 2; i++) {
            System.out.print(" ");
        }
        System.out.print(this.name);
        for(int i = this.getName().length() + 
                (length - this.getName().length()) / 2; i < length; i++) {
            System.out.print(" ");
        }
        System.out.println("|");
        for(int i = 0; i < (length + 2); i++) {
            System.out.print("-");
        }
        System.out.println();
        for(int i = 0; i < this.tables.size(); i++) {
            System.out.print("|");
            for(int j = 0; j < (length - 
                    this.tables.get(i).getName().length()) / 2; j++) {
                System.out.print(" ");
            }
            System.out.print(this.tables.get(i).getName());
            for(int j = this.tables.get(i).getName().length() + 
                    (length - this.tables.get(i).getName().length()) / 2; 
                    j < length; j++) {
                System.out.print(" ");
            }
            System.out.println("|");
        }
        for(int i = 0; i < (length + 2); i++) {
            System.out.print("-");
        }
        System.out.println();
    }
    
    
    
    
    
    
    //------testing------
    
    //Combined testing of method "addTable" and "showTables"
    private void testAddTable() {
        Database db = new Database("Uni");
        
        db.addTable("Pet", new String[] {"Id", "Name", "Kind", "Owner"});
        db.addTable("Owner", new String[] {"Username", "Name"});
        assert(db.tables.get(0).getName().equals("Pet"));
        assert(db.tables.get(1).getName().equals("Owner"));
        db.showTables();
        System.out.println("Testing of method \"addTable\" passed, "
                + "see above for \"showTable\" method !");
    }
    
    //testing of method "getTableByName"
    private void testGetTableByName() {
        Database db = new Database("Uni");
        
        db.addTable("Pet", new String[] {"Id", "Name", "Kind", "Owner"});
        db.addTable("Owner", new String[] {"Username", "Name"});
        assert(db.getTableByName("Pet").getName().equals("Pet"));
        assert(db.getTableByName("Owner").getName().equals("Owner"));
        System.out.println("Testing of method \"getTableByName\" passed !");
    }
    
    public static void main(String[] args) {
        Database testDb = new Database();
        
        testDb.testAddTable();
        testDb.testGetTableByName();
    }
}
