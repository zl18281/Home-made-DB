import java.io.*;
import java.util.*;

public class Table implements Cloneable {
    
    private String name;
    private Record features;
    private ArrayList<Record> items;
    private ArrayList<StringBuilder> primaryKey;
    private HashMap<Table, StringBuilder> foreignKey;
    private HashMap<Table, StringBuilder> referencedBy;
    private HashMap<StringBuilder, StringBuilder> link;
    
    //trivial constructor.
    public Table() {
        this.name = null;
        this.features = null;
        this.items = null;
        this.primaryKey = null;
        this.foreignKey = null;
        this.referencedBy = null;
        this.link = null;
    }
    
    //Constructor to initialise table columns.
    public Table(String[] fields) {
        if(fields != null && fields.length != 0) {
            features = new Record(fields.length);
            for(int i = 1; i <= fields.length; i++) {
                features.setField(i, fields[i - 1]);
            }
            name = null;
            items = new ArrayList<Record>();
            primaryKey = new ArrayList<StringBuilder>();
            foreignKey = new HashMap<Table, StringBuilder>();
            referencedBy = new HashMap<Table, StringBuilder>();
            link = new HashMap<StringBuilder, StringBuilder>();
        }   
    }
    
    //getter method for property items
    public ArrayList<Record> getItems(){
        return this.items;
    }
    
    //getter method for property primaryKey
    public ArrayList<StringBuilder> getPrimaryKey(){
        return this.primaryKey;
    }
    
    //getter method for property referencedBy
    public HashMap<Table, StringBuilder> getReferencedBy(){
        return this.referencedBy;
    }
    
    //getter method for property foreignKey
    public HashMap<Table, StringBuilder> getForeignKey(){
        return this.foreignKey;
    }
    
    //getter method for property link
    public HashMap<StringBuilder, StringBuilder> getLink(){
        return this.link;
    }
    
    //getter method for property features
    public Record getFields() {
        return this.features;
    }
    
    //setter method for property name
    public void setName(String s) {
        this.name = s;
    }
    
    //getter method for property name
    public String getName() {
        return this.name;
    }
    
    //check if keys provided is valid.
    private boolean checkPrimaryKey(String[] pK) {
        int cnt = 0;
        
        if(!(pK.length <= features.getNumberOfFields())) {
            System.out.println("Primary Key Number not right !");
            return false;
        }
        for(int i = 0; i < pK.length; i++) {
            for(int j = 0; j < this.features.getNumberOfFields(); j++) {
                if(this.features.getRecord().get(j).toString().equals(pK[i])) {
                    cnt++;
                }
            }
        }
        if(!(cnt == pK.length)) {
            System.out.println("Some keys provided not valid !");
            return false;
        }else {
            return true;
        }
    }
    
    //adding provided primary keys to an array list.
    public void assignPrimaryKey(String[] pK) {
        if(!this.checkPrimaryKey(pK)) {
            return;
        }
        for(int i = 0; i < pK.length; i++) {
            for(int j = 0; j < this.features.getNumberOfFields(); j++) {
                if(this.features.getRecord().get(j).toString().equals(pK[i])) {
                    this.primaryKey.add(this.features.getRecord().get(j));
                }
            }
        }
    }
    
    //check to see if pk constriant is met
    private boolean checkPkConstraint(Record r) {
        ArrayList<Integer> index = new ArrayList<Integer>();
        int cnt = 0;
        
        for(int i = 0; i < r.getNumberOfFields(); i++) {
            if(this.primaryKey.contains(this.features.getRecord().get(i))) {
                index.add(i);
            }
        }
        for(int i = 0; i < this.items.size(); i++) {
            for(int j = 0; j < index.size(); j++) {
                if(this.items.get(i).getRecord().get(index.get(j)).toString().
                        equals(r.getRecord().get(index.get(j)).toString())) {
                    cnt++;
                }
            }
            if(cnt == index.size() && (index.size() != 0)) {
                return false;
            }
            cnt = 0;
        }
        return true;
    }
    
    //check if a foreign key exists.
    private boolean checkForeignKey(Table t, String fK) {
        if(t == null || t.features == null || t.features.getNumberOfFields() == 0) {
            return false;
        }
        for(int i = 0; i < t.features.getNumberOfFields(); i++) {
            if(fK.equals(t.features.getRecord().get(i).toString())) {
                return true;
            }
        }
        return false;
    }
    
    //Adding foreign key and building links
    public void assignForeignKey(Table t, String fKHere, String fK) {
        if(!this.checkForeignKey(t, fK) || !this.checkPrimaryKey(new String[] {fKHere})) {
            return;
        }
        int indexOne = 0;
        
        for(int i = 0; i < t.features.getNumberOfFields(); i++) {
            if(fK.equals(this.features.getRecord().get(i).toString())) {
                indexOne = i;
            }
        }
        this.foreignKey.put(t, t.features.getRecord().get(indexOne));
        
        int indexB = 0;
        for(int l = 0; l < this.features.getNumberOfFields(); l++) {
            if(this.features.getField(l + 1).toString().equals(fKHere)) {
                indexB = l;
            }
        }
        t.referencedBy.put(this, this.features.getField(indexB + 1));
        int indexTwo = 0;
        
        for(int j = 0; j < this.features.getNumberOfFields(); j++) {
            if(fKHere.equals(this.features.getRecord().get(j).toString())) {
                indexTwo = j;
            }
        }
        this.link.put(this.features.getRecord().get(indexTwo), 
                t.features.getRecord().get(indexOne));
    }
    
    //check FK constriant (used when inserting data)
    public boolean checkFkConstriant(Record r) {
        int k = 0;
        
        for(int i = 0; i < r.getNumberOfFields(); i++) {
            if(this.link.containsKey(this.features.getRecord().get(i))) {
                Iterator<Table> it = this.foreignKey.keySet().iterator();
                Table temp = null;
                Table referencedTable = null;
                
                while(it.hasNext()) {
                    if(this.link.values().contains(this.foreignKey.get(temp = it.next()))) {
                        referencedTable = temp;
                    }
                }
                boolean[] valueIsHere = new boolean[this.link.size()];
                int index = 0;
                
                for(int s = 0; s < referencedTable.features.getNumberOfFields(); s++) {
                    if(referencedTable.features.getRecord().get(s).toString().
                            equals(this.features.getRecord().get(i).toString())) {
                        index = s;
                    }
                }
                if(referencedTable != null && r != null) {
                    for(int j = 0; j < referencedTable.getNumberOfItems(); j++) {
                        String temp1 = r.getRecord().get(i).toString();
                        String temp2 = referencedTable.selectItem(j + 1).getField(index + 1).toString();
                        if(temp1.equals(temp2)) {
                            valueIsHere[k] = true;
                            k++;
                        }
                    }
                }
            }
        }
        if(k == this.link.size()) {
            return true;
        }
        return false;
    }
    
    //Add a field
    public boolean alterTableAddColumn(String s) {
        for(StringBuilder temp : this.features.getRecord()) {
           if(temp.toString().equals(s)) {
               return false;
           } 
        }
        this.features.getRecord().add(new StringBuilder(s));
        for(int i = 0; i < this.items.size(); i++) {
            this.items.get(i).getRecord().add(new StringBuilder(""));
        }
        return true;
    }
    
    //delete a field
    public boolean alterTableDeleteColumn(String s) {
        for(StringBuilder temp : this.features.getRecord()) {
            if(temp.toString().equals(s)){
                if(!this.primaryKey.contains(temp)) {
                    boolean b = true;
                    
                    Iterator<Table> it = this.referencedBy.keySet().iterator();
                    while(it.hasNext()) {
                        if(it.next().foreignKey.values().contains(temp)) {b = false;}
                    }
                    if(b) {
                        this.features.getRecord().remove(temp);
                        for(int i = 0; i < this.items.size(); i++) {
                            this.items.get(i).getRecord().remove(this.features.getNumberOfFields());
                        }
                        return true;
                    }else {
                        System.out.println("This column is refered by other table, "
                                + "cannot be deleted !");
                        return false;
                    }
                }else {
                    System.out.println("This column is Primary Key, "
                            + "cannot be deleted !");
                    return false;
                } 
            }
        }
        System.out.println("This column does not exist !");
        return false;
    }
    
    //insert an item/tuple (PK and FK constraint are checked)
    public boolean insertItem(Record newRecord) {
        if(newRecord.getNumberOfFields() == 
                this.features.getNumberOfFields()) {
            if(!this.checkPkConstraint(newRecord)) {
                System.out.println("PK Constriant Violated !");
                return false;
            }
            if(!this.checkFkConstriant(newRecord)) {
                System.out.println("FK Constriant Violated !");
                return false;
            }
            if(!isInTable(newRecord) && this.checkPkConstraint(newRecord)) {
                this.items.add(newRecord);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    
    //check if a tuple is already in table
    private boolean isInTable(Record newRecord) {
        for(Record r : this.items) {
            int cntB = 0;
            int i = 0;
            
            for(StringBuilder s : r.getRecord()) {
                if(s.toString().equals(newRecord.getRecord().get(i).toString())) {
                    cntB++;
                }
                i++;
            }
            i = 0;
            if(cntB == r.getNumberOfFields()) {
                return true;
            }
            cntB = 0;
        }
        return false;
    }
    
    //select a tuple by index
    public Record selectItem(int index) {
        if(this.items.size() != 0 && 
                (index - 1) < this.items.size() &&
                index >= 1) {
            return this.items.get(index - 1);
        }else {
            return null;
        }
    }
    
    //delete a tuple by index
    public boolean deleteItem(int index) {
        if(this.items.size() == 0) {
            return false;
        }else {
            if(index >= 1 && index <= this.items.size()) {
                this.items.remove(index - 1);
                return true;
            }else {
                return false;
            }
        }
    }
    
    //delete a tuple by PK (FK constraint is checked)
    public boolean deleteItemByPk(String[] s) {
        if(!(s.length == this.primaryKey.size())) {
            return false;
        }
        boolean checkFk = true;
        Record temp = this.selectItemByPrimaryKey(s);

        Iterator<Table> ik = this.referencedBy.keySet().iterator();
        while(ik.hasNext()) {
            Table referencingTable = ik.next();
            
            for(int q = 0; q < temp.getNumberOfFields(); q++) {
                for(int i = 0; i < referencingTable.features.getNumberOfFields(); i++) {
                    if(this.referencedBy.containsValue(referencingTable.features.getField(i + 1))) {
                        for(int j = 0; j < referencingTable.getNumberOfItems(); j++) {
                            if(referencingTable.items.get(j).getField(i + 1).toString().
                                    equals(temp.getField(q + 1).toString())) {
                                checkFk = false;
                            }
                        }
                    }
                }
            }
        }
        if(checkFk == false) {
            System.out.println("Deleting violating FK constriant !");
            return false;
        }
        int cnt = 0;
        for(int k = 0; k < this.getNumberOfItems(); k++) {
            for(int i = 0; i < this.primaryKey.size(); i++) {
                int index = -1;
                for(int j = 0; j < this.features.getRecord().size(); j++) {
                    if(this.features.getRecord().get(j).toString().
                            equals(this.primaryKey.get(i).toString())) {
                        index = j;
                    }
                }
                if(s[i].equals(this.items.get(k).getRecord().get(index).toString())) {
                    cnt++;
                }
            }
            if(cnt == this.primaryKey.size()) {
                this.items.remove(k);
                return true;
            }
        }
        return false;
    }
    
    //check if provided key matches a row
    private boolean checkItem(String[] fieldValue, Record item) {
        int cnt = 0;
        
        for(int i = 0; i < fieldValue.length; i++) {
            for(int j = 0; j < item.getNumberOfFields(); j++) {
                if(fieldValue[i].equals(item.getRecord().get(j).toString())) {
                    cnt++;
                }
            }
        }
        if(cnt == fieldValue.length) {
            return true;
        }else {
            return false;
        }
    }
    
    //select item by PK.
    public Record selectItemByPrimaryKey(String[] fieldValue) {
        if(!(fieldValue.length == this.primaryKey.size())) {
            System.out.println("Please provide correct value set !");
            return null;
        }else {
            for(int i = 0; i < this.items.size(); i++) {
                if(this.checkItem(fieldValue, this.items.get(i))) {
                    return this.items.get(i);
                }
            }
            return null;
        }
    }
    
    //getter method for the number of tuples in table
    public int getNumberOfItems() {
        return this.items.size();
    }
    
    //calculate the space needed by each column
    private int[] calculateColumnWidth() {
        int[] columnwidth = 
                new int[this.features.getNumberOfFields()];
        
        for(int i = 0; i < columnwidth.length; i++) {
            int temp = this.features.getRecord().get(i).toString().length();
            
            for(int j = 0; j < this.items.size(); j++) {
                if(this.items.get(j).getRecord().get(i).toString().length() > 
                  temp) {
                    temp = this.items.get(j).getRecord().get(i).toString().length();
                }
            }
            columnwidth[i] = temp + 4;
        }
        return columnwidth;
    }
    
    //print table head
    private void printTableHead() {
        int[] columnwidth = this.calculateColumnWidth();
        int sum = 0;
        
        for(int q = 0; q < columnwidth.length; q++) {
            sum += columnwidth[q];
        }
        sum += (columnwidth.length + 1);
        for(int p = 0; p < sum; p++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.print("|");
        for(int i = 0; i < this.features.getNumberOfFields(); i++) {
            for(int j = 0; j < (columnwidth[i] - 
                    this.features.getRecord().get(i).toString().length()) / 2; j++){
                System.out.print(" ");
            }
            System.out.print(this.features.getRecord().get(i).toString());
            for(int j = (columnwidth[i] - 
                    this.features.getRecord().get(i).toString().length()) / 2 + 
                    this.features.getRecord().get(i).toString().length(); 
                    j < columnwidth[i]; j++){
                System.out.print(" ");
            }
            System.out.print("|");
        }
        System.out.println();
        for(int p = 0; p < sum; p++) {
            System.out.print("-");
        }
        System.out.println();
    }
    
    //print the whole table
    public void printTable() {
        this.printTableHead();
        int[] columnwidth = this.calculateColumnWidth();
        
        for(int i = 0; i < this.items.size(); i++) {
            System.out.print("|");
            for(int j = 0; j < this.features.getNumberOfFields(); j++) {
                for(int k = 0; k < (columnwidth[j] - 
                        this.items.get(i).getRecord().get(j).toString().length()) / 2; k++){
                            System.out.print(" ");
                }
                System.out.print(this.items.get(i).getRecord().get(j).toString());
                for(int k = (columnwidth[j] - 
                        this.items.get(i).getRecord().get(j).toString().length()) / 2 +
                        this.items.get(i).getRecord().get(j).toString().length(); 
                        k < columnwidth[j]; k++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            System.out.println();
        }
        int sum = 0;
        
        for(int q = 0; q < columnwidth.length; q++) {
            sum += columnwidth[q];
        }
        sum += (columnwidth.length + 1);
        for(int p = 0; p < sum; p++) {
            System.out.print("-");
        }
        System.out.println("\n");
    }
    
    //compute how many rows are in this file
    private int calculateRowOfFile(File fr) throws FileNotFoundException {
        Scanner input = null;
        try {
            input= new Scanner(fr);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught by method \"calculateRowOfFile\"");
            throw e;
        }
        int row = 0;
        
        while(input.hasNext()) {
            input.nextLine();
            row++;
        }
        input.close();
        return row;
    }
    
    //compute how many columns are in this file
    private int calculateColumnOfFile(File fr) throws FileNotFoundException {
        Scanner input = null;
        try {
            input = new Scanner(fr);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught by method \"calculateColumnOfFile\"");
            throw e;
        }
        int col = 0;
        String s = input.nextLine();
        Scanner in = new Scanner(s);
        
        while(in.hasNext()) {in.next(); col++;}
        input.close();
        in.close();
        return col;
    }
    
    //storing the file tokens into 2D array according to row and column index
    public String[][] fileToTokens(File fr) throws FileNotFoundException {
        Scanner input = null;
        String[][] tokens;
        
        try {
            input = new Scanner(fr);
             tokens = new String[this.calculateRowOfFile(fr)][this.calculateColumnOfFile(fr)];
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught by method \"fileToTokens\"");
            throw e;
        }
        int currentRow = -1;
        int currentColumn = -1;
        
        while(input.hasNext()) {
            StringTokenizer st = new StringTokenizer(input.nextLine());
            currentRow++;
            while(st.hasMoreTokens()) {
                currentColumn++;
                tokens[currentRow][currentColumn] = st.nextToken();
            }
            currentColumn = -1;
        }
        input.close();
        return tokens;
    }
    
    //Read data from a file
    public void fileToTable(File fr) throws FileNotFoundException {
        String[][] tokens;
        
        try {
            tokens = this.fileToTokens(fr);
        } catch (FileNotFoundException e){
            System.out.println("FileNotFoundException caught by method \"fileToTable\"");
            throw e;
        }
        this.features = new Record(tokens[0].length);
        this.items = new ArrayList<Record>();
        for(int i = 0; i < tokens[0].length; i++) {
            this.features.setField((i + 1), tokens[0][i]);
        }
        
        for(int i = 0; i < (tokens.length - 1); i++) {
            this.items.add(new Record(features.getNumberOfFields()));
            for(int j = 0; j < tokens[i].length; j++) {
                this.items.get(i).setField((j + 1), tokens[i + 1][j]);
            }
        }
    }
    
    //Store data to a file
    public void tableToFile(File fw) throws FileNotFoundException {
        PrintWriter output = null;
        try {
            output = new PrintWriter(fw);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught by method \"tableToFile\"");
            throw e;
        }
        for(int i = 0; i < (this.features.getNumberOfFields() - 1); i++) {
            output.print(this.features.getField(i + 1).toString() + " ");
        }
        output.print(this.features.getField(this.features.getNumberOfFields()).toString());
        output.print("\n");
        for(int i = 0; i < this.items.size(); i++) {
            for(int j = 0; j < (this.features.getNumberOfFields() - 1); j++) {
                output.print(this.items.get(i).getField(j + 1).toString() + " ");
            }
            output.print(this.items.get(i).getField(this.features.getNumberOfFields()).toString());
            output.print("\n");
        }
        output.close();
    }
    
    
    
    
    
    //------testing------
    
    //testing of constructor
    private void testConstructor() {
        //test empty constructor.
        Table tableOne = new Table();
        
        assert(tableOne.features == null);
        assert(tableOne.items == null);
        
        //test another constructor
        Table tableTwo = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        assert(tableTwo.features.getNumberOfFields() == 4);
        assert(tableTwo.features.getRecord().get(0).toString().equals("Id"));
        assert(tableTwo.features.getRecord().get(1).toString().equals("Name"));
        assert(tableTwo.features.getRecord().get(2).toString().equals("Kind"));
        assert(tableTwo.features.getRecord().get(3).toString().equals("Owner"));
        assert(tableTwo.items != null);
        assert(tableTwo.items.size() == 0);
        System.out.println("Test of constructor passed !");
    }
    
    //testing of method "alterTableAddColumn"
    private void testAlterTableAddColumn() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        tableOne.alterTableAddColumn("Color");
        assert(tableOne.features.getRecord().get(4).toString().equals("Color"));
        System.out.println("Test of method \"alterTableAddColumn\" passed !");
    }
    
    //testing of method "alterTableDeleteColumn"
    private void testAlterTableDeleteColumn() {
        Table tableOne = new Table(new String[] {"Id"});
        
        tableOne.alterTableDeleteColumn("Id");
        assert(tableOne.features.getRecord().size() == 0);
        
        Table tableTwo = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        StringBuilder temp = tableTwo.features.getRecord().get(1);
        tableTwo.alterTableDeleteColumn("Name");
        assert(!tableTwo.features.getRecord().contains(temp));
        System.out.println("Test of method \"alterTableDeleteColumn\" passed !");
    }
    
    //testing of method "insertItem"
    private void testInsertItem() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecord = new Record(4);
        newRecord.setField(1, "1");
        newRecord.setField(2, "Fido");
        newRecord.setField(3, "dog");
        newRecord.setField(4, "ab123");
        Record duplicateRecord = new Record(4);
        duplicateRecord.setField(1, "1");
        duplicateRecord.setField(2, "Fido");
        duplicateRecord.setField(3, "dog");
        duplicateRecord.setField(4, "ab123");
        Record wrongRecord = new Record(2);
        wrongRecord.setField(1, "Wanda");
        wrongRecord.setField(2, "fish");
        tableOne.insertItem(newRecord);
        assert(tableOne.items.size() == 1);
        assert(tableOne.items.get(0) == newRecord);
        assert(tableOne.insertItem(duplicateRecord) == false);
        assert(tableOne.insertItem(wrongRecord) == false);
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        Record newRecordTestPk = new Record(4);
        newRecordTestPk.setField(1, "1");
        newRecordTestPk.setField(2, "Peter");
        newRecordTestPk.setField(3, "dog");
        newRecordTestPk.setField(4, "ab123");
        tableOne.insertItem(newRecordTestPk);
        
        Table tableTwo = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        Table tableThree = new Table(new String[] {"Username", "Name"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        Record newRecordFour = new Record(4);
        
        newRecordFour.setField(1, "4");
        newRecordFour.setField(2, "Bill");
        newRecordFour.setField(3, "cat");
        newRecordFour.setField(4, "st123");
        
        Record newOwnerRecordOne = new Record(2);
        
        newOwnerRecordOne.setField(1, "ab123");
        newOwnerRecordOne.setField(2, "Jo");
        
        Record newOwnerRecordTwo = new Record(2);
        
        newOwnerRecordTwo.setField(1, "cd456");
        newOwnerRecordTwo.setField(2, "Sam");
        
        Record newOwnerRecordThree = new Record(2);
        
        newOwnerRecordThree.setField(1, "ef789");
        newOwnerRecordThree.setField(2, "Amy");
        
        Record newOwnerRecordFour = new Record(2);
        
        newOwnerRecordFour.setField(1, "gh012");
        newOwnerRecordFour.setField(2, "Pete");
        
        tableTwo.assignForeignKey(tableThree, "Owner", "Username");
        
        tableThree.insertItem(newOwnerRecordOne);
        tableThree.insertItem(newOwnerRecordTwo);
        tableThree.insertItem(newOwnerRecordThree);
        tableThree.insertItem(newOwnerRecordFour);
        tableTwo.insertItem(newRecordFour);        
        System.out.println("Test of method \"insertItem\" passed !");
    }
    
    //testing of method "isInTable"
    private void testIsInTable() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        Record newRecordOneCopy = new Record(4);
        
        newRecordOneCopy.setField(1, "1");
        newRecordOneCopy.setField(2, "Fido");
        newRecordOneCopy.setField(3, "dog");
        newRecordOneCopy.setField(4, "ab123");
        
        Record newRecordNotInTable = new Record(4);
        
        newRecordNotInTable.setField(1, "4");
        newRecordNotInTable.setField(2, "Peter");
        newRecordNotInTable.setField(3, "elephant");
        newRecordNotInTable.setField(4, "xy321");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        
        assert(tableOne.isInTable(newRecordOne));
        assert(tableOne.isInTable(newRecordTwo));
        assert(tableOne.isInTable(newRecordThree));
        assert(tableOne.isInTable(newRecordOneCopy));
        assert(!tableOne.isInTable(newRecordNotInTable));
        System.out.println("Test of method \"isInTable\" passed !");
    }
    
    //test of method "selectItem"
    private void testSelectItem() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        
        assert(tableOne.selectItem(1) == newRecordOne);
        assert(tableOne.selectItem(2) == newRecordTwo);
        assert(tableOne.selectItem(3) == newRecordThree);
        assert(tableOne.selectItem(0) == null);
        assert(tableOne.selectItem(4) == null);
        System.out.println("Test of method \"selectItem\" passed !");
    }
    
    //test of method "deleteItem"
    private void testDeleteItem() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        assert(tableOne.deleteItem(0) == false);
        assert(tableOne.deleteItem(1) == false);
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        
        assert(tableOne.deleteItem(3) == true);
        assert(!tableOne.isInTable(newRecordThree));
        assert(tableOne.deleteItem(2) == true);
        assert(!tableOne.isInTable(newRecordTwo));
        assert(tableOne.deleteItem(1) == true);
        assert(!tableOne.isInTable(newRecordOne));
        assert(tableOne.deleteItem(0) == false);
        assert(tableOne.deleteItem(-1) == false);
        assert(tableOne.deleteItem(1) == false);
        assert(tableOne.deleteItem(2) == false);
        assert(tableOne.deleteItem(3) == false);
        System.out.println("Test of method \"deleteItem\" passed !");
    }
    
    //test method "deleteItemByPk"
    private void testDeleteItemByPk() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        Table tableTwo = new Table(new String[] {"Username", "Name"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        tableTwo.assignPrimaryKey(new String[] {"Username"});
        tableOne.assignForeignKey(tableTwo, "Owner", "Username");
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        Record ownerOne = new Record(2);
        ownerOne.setField(1, "ab123");
        ownerOne.setField(2, "Jo");
        Record ownerTwo = new Record(2);
        ownerTwo.setField(1, "cd456");
        ownerTwo.setField(2, "Sam");
        Record ownerThree = new Record(2);
        ownerThree.setField(1, "ef789");
        ownerThree.setField(2, "Amy");
        Record ownerFour = new Record(2);
        ownerFour.setField(1, "gh012");
        ownerFour.setField(2, "Pete");
        tableTwo.insertItem(ownerOne);
        tableTwo.insertItem(ownerTwo);
        tableTwo.insertItem(ownerThree);
        tableTwo.insertItem(ownerFour);
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        assert(tableTwo.deleteItemByPk(new String[] {"cd456"}) == true);
        assert(tableTwo.deleteItemByPk(new String[] {"gh012"}) == true);
        assert(tableTwo.deleteItemByPk(new String[] {"ab123"}) == false);
        assert(tableTwo.deleteItemByPk(new String[] {"ef789"}) == false);
        System.out.println("Test of method \"deleteItemByPk\" passed !");
    }
    
    
    //test method "calculateColumnWidth"
    private void testCalculateColumnWidth() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        
        int[] temp = tableOne.calculateColumnWidth();
        
        assert(temp[0] == 6);
        assert(temp[1] == 12);
        assert(temp[2] == 8);
        assert(temp[3] == 9);
        System.out.println("Test of method \"calculateColumnWidth\" passed !");
    }
    
    //test of method "printTableHead"
    private void testPrintTableHead() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        System.out.println("Test of method \"printTableHead\". See following:");
        tableOne.printTableHead();
    }
    
    //test of method "printTable"
    private void testPrintTable() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        System.out.println("Test of method \"printTable\". See following:");
        tableOne.printTable();
    }
    
    //testing of method "calculateRowOfFile"
    private void testCalculateRowOfFile(File fr) throws FileNotFoundException {
        Table t = new Table();
        
        try {
            assert(t.calculateRowOfFile(fr) == 4);
            System.out.println("Test of method \"calculateRowOfFile\" passed !");
        } catch (FileNotFoundException e) {
            throw e;
        }
        
    }
    
    //testing of method "calculateColumnOfFile"
    private void testCalculateColumnOfFile(File fr) throws FileNotFoundException {
        Table t = new Table();
        
        try {
            assert(t.calculateColumnOfFile(fr) == 4);
            System.out.println("Test of method \"calculateColumnOfFile\" passed !");
        } catch (FileNotFoundException e) {
            throw e;
        }
        
    }
    
    //test of method "fileToTokens"
    private void testFileToTokens(File fr) throws FileNotFoundException {
        Table t = new Table();
        String[][] tokens;
        
        try {
            tokens = t.fileToTokens(fr);
        } catch (FileNotFoundException e) {
            throw e;
        }
        assert(tokens[0][0].equals("Id"));
        assert(tokens[0][1].equals("Name"));
        assert(tokens[0][2].equals("Kind"));
        assert(tokens[0][3].equals("Owner"));
        assert(tokens[1][0].equals("1"));
        assert(tokens[1][1].equals("Fido"));
        assert(tokens[1][2].equals("dog"));
        assert(tokens[1][3].equals("ab123"));
        assert(tokens[2][0].equals("2"));
        assert(tokens[2][1].equals("Wanda"));
        assert(tokens[2][2].equals("fish"));
        assert(tokens[2][3].equals("ef789"));
        assert(tokens[3][0].equals("3"));
        assert(tokens[3][1].equals("Garfield"));
        assert(tokens[3][2].equals("cat"));
        assert(tokens[3][3].equals("ab123"));
        System.out.println("Test of method \"fileToTokens\" passed !");
    }
    
    //test of method "fileToTable"
    private void testFileToTable(File fr) throws FileNotFoundException {
        Table t = new Table();
        
        try {
            t.fileToTable(fr);
        } catch (FileNotFoundException e) {
            throw e;
        }
        t.printTable();
        assert(t.features.getField(1).toString().equals("Id"));
        assert(t.features.getField(2).toString().equals("Name"));
        assert(t.features.getField(3).toString().equals("Kind"));
        assert(t.features.getField(4).toString().equals("Owner"));
        assert(t.items.get(0).getField(1).toString().equals("1"));
        assert(t.items.get(0).getField(2).toString().equals("Fido"));
        assert(t.items.get(0).getField(3).toString().equals("dog"));
        assert(t.items.get(0).getField(4).toString().equals("ab123"));
        assert(t.items.get(1).getField(1).toString().equals("2"));
        assert(t.items.get(1).getField(2).toString().equals("Wanda"));
        assert(t.items.get(1).getField(3).toString().equals("fish"));
        assert(t.items.get(1).getField(4).toString().equals("ef789"));
        assert(t.items.get(2).getField(1).toString().equals("3"));
        assert(t.items.get(2).getField(2).toString().equals("Garfield"));
        assert(t.items.get(2).getField(3).toString().equals("cat"));
        assert(t.items.get(2).getField(4).toString().equals("ab123"));
        System.out.println("Test of method \"fileToTable\" passed !");
    }
    
    //test of method "tableToFile"
    private void testTableToFile(File fw) throws FileNotFoundException {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        
        try {
            tableOne.tableToFile(fw);
        } catch (FileNotFoundException e) {
            throw e;
        }
        System.out.println("Test of method \"tableToFile\" finished. See the file to verify.");
    }
    
    //test of method "checkPrimaryKey"
    private void testCheckPrimaryKey() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        assert(tableOne.checkPrimaryKey(new String[] {"Id"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Name"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Kind"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Owner"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Id", "Name"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Id", "Name", "Kind"}) == true);
        assert(tableOne.checkPrimaryKey(new String[] {"Id", "Name", "Kind", "Owner"}) == true);
        System.out.println("Test of method \"checkPrimaryKey\" passed !");
    }
    
    //test of method "assignPrimaryKey"
    private void testAssignPrimaryKey() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        assert(tableOne.primaryKey.get(0).toString().equals("Id"));
        tableOne.assignPrimaryKey(new String[] {"Name"});
        assert(tableOne.primaryKey.get(0).toString().equals("Id"));
        assert(tableOne.primaryKey.get(1).toString().equals("Name"));
        System.out.println("Test of method \"assignPrimaryKey\" passed !");
    }
    
    //test of method "checkPkConstraint"
    private void testCheckPkConstraint() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "1");
        newRecordThree.setField(2, "Peter");
        newRecordThree.setField(3, "Bird");
        newRecordThree.setField(4, "xy123");
        
        tableOne.insertItem(newRecordOne);
        assert(tableOne.checkPkConstraint(newRecordTwo) == true);
        assert(tableOne.checkPkConstraint(newRecordThree) == false);
        
        tableOne.assignPrimaryKey(new String[] {"Name"});
        
        Record newRecordFour = new Record(4);
        
        newRecordFour.setField(1, "1");
        newRecordFour.setField(2, "Fido");
        newRecordFour.setField(3, "bird");
        newRecordFour.setField(4, "er123");
        
        assert(tableOne.checkPkConstraint(newRecordThree) == true);
        assert(tableOne.checkPkConstraint(newRecordFour) == false);
        System.out.println("Test of method \"checkPkConstraint\" passed !");
    }
    
    //test of method "checkItem"
    private void testCheckItem() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        tableOne.assignPrimaryKey(new String[] {"Id", "Name"});
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        assert(tableOne.checkItem(new String[] {"1", "Fido"}, newRecordOne) == true);
        assert(tableOne.checkItem(new String[] {"1", "Fido"}, newRecordTwo) == false);
        System.out.println("Test of method \"checkItem\" passed !");
    }
    
    //test of method "selectItemByPrimaryKey"
    private void testSelectItemByPrimaryKey() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        Record newRecordFour = new Record(4);
        
        newRecordFour.setField(1, "4");
        newRecordFour.setField(2, "Fido");
        newRecordFour.setField(3, "dog");
        newRecordFour.setField(4, "ab123");
        
        tableOne.insertItem(newRecordOne);
        tableOne.insertItem(newRecordTwo);
        tableOne.insertItem(newRecordThree);
        tableOne.insertItem(newRecordFour);
        assert(tableOne.selectItemByPrimaryKey(new String[] {"1"}) == (newRecordOne));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"2"}) == (newRecordTwo));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"3"}) == (newRecordThree));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"4"}) == (newRecordFour));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"5"}) == null);
        
        tableOne.assignPrimaryKey(new String[] {"Name"});
        Record newRecordFive = new Record(4);
        
        newRecordFive.setField(1, "5");
        newRecordFive.setField(2, "Mike");
        newRecordFive.setField(3, "dog");
        newRecordFive.setField(4, "ab123");
        tableOne.insertItem(newRecordFive);
        
        assert(tableOne.selectItemByPrimaryKey(new String[] {"1", "Fido"}) == (newRecordOne));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"2", "Wanda"}) == (newRecordTwo));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"3", "Garfield"}) == (newRecordThree));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"4", "Fido"}) == (newRecordFour));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"5", "Mike"}) == (newRecordFive));
        assert(tableOne.selectItemByPrimaryKey(new String[] {"1", "Anny"}) == null);
        assert(tableOne.selectItemByPrimaryKey(new String[] {"8", "Fido"}) == null);
        assert(tableOne.selectItemByPrimaryKey(new String[] {"9", "Anny"}) == null);
        System.out.println("Test of method \"selectItemByPrimaryKey\" passed !");
    }
    
    //test of method "checkForeignKey"
    private void testCheckForeignKey() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        Table tableTwo = new Table(new String[] {"Username", "Name"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableTwo.assignPrimaryKey(new String[] {"Username"});
        
        Record newOwnerRecordOne = new Record(2);
        
        newOwnerRecordOne.setField(1, "ab123");
        newOwnerRecordOne.setField(2, "Jo");
        
        Record newOwnerRecordTwo = new Record(2);
        
        newOwnerRecordTwo.setField(1, "cd456");
        newOwnerRecordTwo.setField(2, "Sam");
        
        Record newOwnerRecordThree = new Record(2);
        
        newOwnerRecordThree.setField(1, "ef789");
        newOwnerRecordThree.setField(2, "Amy");
        
        Record newOwnerRecordFour = new Record(2);
        
        newOwnerRecordFour.setField(1, "gh012");
        newOwnerRecordFour.setField(2, "Pete");
        
        assert(tableOne.checkForeignKey(tableTwo, "Username") == true);
        assert(tableOne.checkForeignKey(tableTwo, "Name") == true);
        assert(tableOne.checkForeignKey(tableTwo, "Id") == false);
        System.out.println("Test of method \"checkForeignKey\" passed !");
    }
    
    //test of method "assignForeignKey"
    private void testAssignForeignKey() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        Table tableTwo = new Table(new String[] {"Username", "Name"});
        Table tableThree = new Table(new String[] {"No", "Value"});
        
        tableOne.assignPrimaryKey(new String[] {"Id"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        tableTwo.assignPrimaryKey(new String[] {"Username"});
        
        Record newOwnerRecordOne = new Record(2);
        
        newOwnerRecordOne.setField(1, "ab123");
        newOwnerRecordOne.setField(2, "Jo");
        
        Record newOwnerRecordTwo = new Record(2);
        
        newOwnerRecordTwo.setField(1, "cd456");
        newOwnerRecordTwo.setField(2, "Sam");
        
        Record newOwnerRecordThree = new Record(2);
        
        newOwnerRecordThree.setField(1, "ef789");
        newOwnerRecordThree.setField(2, "Amy");
        
        Record newOwnerRecordFour = new Record(2);
        
        newOwnerRecordFour.setField(1, "gh012");
        newOwnerRecordFour.setField(2, "Pete");
        
        tableOne.assignForeignKey(tableTwo, "Owner", "Username");
        assert(tableOne.foreignKey.values().contains(tableTwo.features.getRecord().get(0)));
        assert(tableOne.foreignKey.keySet().contains(tableTwo));
        assert(!tableOne.foreignKey.values().contains(tableTwo.features.getRecord().get(1)));
        assert(!tableOne.foreignKey.keySet().contains(tableThree));
        System.out.println("Test of method \"assignForeignKey\" passed !");
    }
    
    //test of method "checkFkConstriant"
    private void testCheckFkConstriant() {
        Table tableOne = new Table(new String[] {"Id", "Name", "Kind", "Owner"});
        Table tableTwo = new Table(new String[] {"Username", "Name"});
        
        Record newRecordOne = new Record(4);
        
        newRecordOne.setField(1, "1");
        newRecordOne.setField(2, "Fido");
        newRecordOne.setField(3, "dog");
        newRecordOne.setField(4, "ab123");
        
        Record newRecordTwo = new Record(4);
        
        newRecordTwo.setField(1, "2");
        newRecordTwo.setField(2, "Wanda");
        newRecordTwo.setField(3, "fish");
        newRecordTwo.setField(4, "ef789");
        
        Record newRecordThree = new Record(4);
        
        newRecordThree.setField(1, "3");
        newRecordThree.setField(2, "Garfield");
        newRecordThree.setField(3, "cat");
        newRecordThree.setField(4, "ab123");
        
        Record newRecordFour = new Record(4);
        
        newRecordFour.setField(1, "4");
        newRecordFour.setField(2, "Marry");
        newRecordFour.setField(3, "mouse");
        newRecordFour.setField(4, "ui999");
        
        tableTwo.assignPrimaryKey(new String[] {"Username"});
        
        Record newOwnerRecordOne = new Record(2);
        
        newOwnerRecordOne.setField(1, "ab123");
        newOwnerRecordOne.setField(2, "Jo");
        
        Record newOwnerRecordTwo = new Record(2);
        
        newOwnerRecordTwo.setField(1, "cd456");
        newOwnerRecordTwo.setField(2, "Sam");
        
        Record newOwnerRecordThree = new Record(2);
        
        newOwnerRecordThree.setField(1, "ef789");
        newOwnerRecordThree.setField(2, "Amy");
        
        Record newOwnerRecordFour = new Record(2);
        
        newOwnerRecordFour.setField(1, "gh012");
        newOwnerRecordFour.setField(2, "Pete");
        
        tableOne.assignForeignKey(tableTwo, "Owner", "Username");
        tableTwo.insertItem(newOwnerRecordOne);
        tableTwo.insertItem(newOwnerRecordTwo);
        tableTwo.insertItem(newOwnerRecordThree);
        tableTwo.insertItem(newOwnerRecordFour);
        assert(tableOne.checkFkConstriant(newRecordOne) == true);
        assert(tableOne.checkFkConstriant(newRecordTwo) == true);
        assert(tableOne.checkFkConstriant(newRecordThree) == true);
        assert(tableOne.checkFkConstriant(newRecordFour) == false);
        System.out.println("Test of method \"checkFkConstriant\" passed !");
    }
    
    public static void main(String[] args) {
        Table testTable = new Table();
        
        testTable.testConstructor();
        testTable.testAlterTableAddColumn();
        testTable.testAlterTableDeleteColumn();
        testTable.testInsertItem();
        testTable.testIsInTable();
        testTable.testSelectItem();
        testTable.testDeleteItem();
        testTable.testDeleteItemByPk();
        testTable.testCalculateColumnWidth();
        testTable.testPrintTableHead();
        testTable.testPrintTable();      
        try{
            File fr = new File("fileToTable.txt");
            testTable.testCalculateRowOfFile(fr);
            testTable.testCalculateColumnOfFile(fr);
            testTable.testFileToTokens(fr);
            testTable.testFileToTable(fr);
        } catch (FileNotFoundException e) {
            System.out.println("File not found during testing !");
        } finally {
            try {
                File fw = new File("tableToFile.txt");
                
                testTable.testTableToFile(fw);
            } catch (FileNotFoundException e) {
                System.out.println("File not found during testing !");
            } finally {
                testTable.testCheckPrimaryKey();
                testTable.testAssignPrimaryKey();
                testTable.testCheckPkConstraint();
                testTable.testCheckItem();
                testTable.testSelectItemByPrimaryKey();
                testTable.testCheckForeignKey();
                testTable.testAssignForeignKey();
                testTable.testCheckFkConstriant();
            }
        }
    }
}
