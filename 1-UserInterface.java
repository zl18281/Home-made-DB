import java.io.*;
import java.util.*;

public class UserInterface {

    public static void main(String[] args) {
        ArrayList<Database> db = new ArrayList<Database>();
        boolean b = true;
        Menu m = new Menu();
        
        do {
            int i = 0;
            Scanner input = new Scanner(System.in);
            
            m.mainMenu();
            System.out.println("Input your choice:");
            i = input.nextInt();
            switch(i) {
                case 1: {b = true; UserInterface.oneOne(db); break;}
                case 2: {b = true; UserInterface.oneTwo(db, m); break;}
                case 3:{b = true; UserInterface.oneThree(db); break;}
                case 4:{b = false; break;}
            }
        } while(b);
    }
    
    //Choice 1 of 1st level menu 
    private static void oneOne(ArrayList<Database> db) {
        if(db.size() == 0) {
            System.out.println();
            System.out.println("There is no database");
            return;
        }
        System.out.println();
        for(int j = 0; j < db.size(); j++) {
            System.out.println((j + 1) + ". " + db.get(j).getName().toString() + "\n");
        }
    }
    
    //Choice 2 of 1st level menu 
    private static void oneTwo(ArrayList<Database> db, Menu m) {
        int k = 0;
        boolean s = true;
        Scanner input = new Scanner(System.in);
        
        if(db.size() == 0) {
            System.out.println("There is no database");
            return;
        }
        System.out.println("Input database name:");
        String temp = input.next();
        Database choose = null;
        
        for(int t = 0; t < db.size(); t++) {
            if(db.get(t).getName().equals(temp)) {
                choose = db.get(t);
            }
        }
        if(choose == null) {
            System.out.println("Database does not exit !");
            return;
        }
        System.out.println("\nAlter to database: \"" + choose.getName() + "\"");
        do {
            m.secondMenu(choose);
            k = input.nextInt();
            switch(k) {
                case 1:{s = true; UserInterface.twoOne(choose); break;}
                case 2: {s = true; UserInterface.twoTwo(choose); break;}
                case 3: {s = true; UserInterface.twoThree(choose); break;}
                case 4:{s = true; UserInterface.twoFour(m, choose); break;}
                case 5: {s = true; UserInterface.twoFive(choose); break;}
                case 6: {s = false; break;}
            }
        }while(s);
    }
    
    //Choice 3 of 1st level menu 
    private static void oneThree(ArrayList<Database> db) {
        Scanner input = new Scanner(System.in); 
        System.out.println("Input database name:");
        String tempDbName = input.next();
        Database newdb = new Database(tempDbName);
        db.add(newdb);
        File dir = new File(newdb.getName());
        if(!dir.exists()) {
            dir.mkdir();
        }
        System.out.println();
        System.out.println("Database \"" + tempDbName + "\" is created !");
    }
    
    //Choice 1 of 2nd level menu 
    private static void twoOne(Database d) {
        d.showTables();
    }
    
    //Choice 2 of 2nd level menu 
    private static void twoTwo(Database d) {
        String [] y;
        int cnt = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("Input table name:");
        String tempName = input.next();
        System.out.println("Input fields (seperated by space):");
        Scanner tempInput = new Scanner(System.in);
        String tempField = tempInput.nextLine();
        Scanner in = new Scanner(tempField);
        while(in.hasNext()) {
            cnt++;
            in.next();
        }
        y = new String[cnt];
        in = new Scanner(tempField);
        int index = 0;
        while(in.hasNext()) {
            y[index] = in.next();
            index++;
        }
        d.addTable(tempName, y);
    }
    
    //Choice 3 of 2nd level menu
    private static void twoThree(Database d) {
        Scanner input = new Scanner(System.in);
        System.out.println("Input table name:");
        String tableForRemove = input.next();
        
        Table tForRemove = d.getTableByName(tableForRemove);
        if(tForRemove == null) {
            System.out.println("Table does not exists !");
            return;
        }
        d.dropTable(tableForRemove);
    }
    
    //Choice 4 of 2nd level menu
    private static void twoFour(Menu m, Database choose) {
        Scanner input =new Scanner(System.in);
        System.out.println("Input table name:");
        String tempTableName = input.next();
        Table tempTable = choose.getTableByName(tempTableName);
        Scanner inputTwo = new Scanner(System.in);
        if(tempTable == null) {
            return;
        }
        boolean u = true;
        do {
            m.thirdMenu(tempTable);
            int l = inputTwo.nextInt();
            switch (l) {
                case 1: {u = true; UserInterface.thirdOne(tempTable); break;}
                case 2: {u = true; UserInterface.thirdTwo(tempTable); break;}
                case 3: {u = true; UserInterface.thirdThree(tempTable); break;}
                case 4: {u = true; UserInterface.thirdFour(tempTable); break;}
                case 5: {u = true; UserInterface.thirdFive(tempTable); break;}
                case 6: {u = true; UserInterface.thirdSix(tempTable); break;}
                case 7: {u = true; UserInterface.thirdSeven(choose, tempTable); break;}
                case 8: {u = true; UserInterface.thirdEight(tempTable); break;}
                case 9: {u = true; UserInterface.thirdNine(tempTable); break;}
                case 10: {u = true; UserInterface.thirdTen(tempTable); break;}
                case 11: {u = true; UserInterface.thirdEleven(tempTable, choose); break;}
                case 12: {u = false; break;}
            }
        }while(u);
    }
    
    //Choice 5 of 2nd level menu
    private static void twoFive(Database b) {
        System.out.println("Input table name: ");
        Scanner input = new Scanner(System.in);
        String s = input.next();
        boolean bool = true;
        for(int i = 0; i < b.getTables().size(); i++) {
            if(b.getTables().get(i).getName().equals(s)) {
                bool = false;
            }
        }
        if(bool == false) {
            System.out.println("Naming conflict !");
            return;
        }
        System.out.println("Input file name:");
        String fileName = input.next();
        try {
            File f = new File(fileName);
            Table t = new Table(new String[] {""});
            t.setName(s);
            t.fileToTable(f);
            b.getTables().add(t);
        }catch(FileNotFoundException e) {
            System.out.println("File not found !");
            return;
        }
    }
    
    //Choice 1 of 3rd level menu
    private static void thirdOne(Table t) {
        t.printTable();
    }
    
    //Choice 2 of 3rd level menu
    private static void thirdTwo(Table t) {
        System.out.println("Input data for the following fields:");
        for(int w = 0; w < t.getFields().getNumberOfFields(); w++) {
            System.out.print(t.getFields().getField(w + 1) + " ");
        }
        System.out.println();
        Scanner inputFields = new Scanner(System.in);
        Record rTemp = new Record(t.getFields().getNumberOfFields());
        for(int w = 0; w < t.getFields().getNumberOfFields(); w++) {
            rTemp.setField(w + 1, inputFields.next());
        }
        t.insertItem(rTemp);
    }
    
    //Choice 3 of 3rd level menu
    private static void thirdThree(Table t) {
        System.out.println("Input the index for the tuple:");
        Scanner input = new Scanner(System.in);
        t.deleteItem(input.nextInt());
    }
    
    //Choice 4 of 3rd level menu
    private static void thirdFour(Table t) {
        System.out.println("Input the PK fields:");
        Scanner inputPkField = new Scanner(System.in);
        String PkFields = inputPkField.nextLine();
        Scanner inputPkFieldTokens = new Scanner(PkFields);
        int cnt = 0;
        while(inputPkFieldTokens.hasNext()) {
            cnt++;
            inputPkFieldTokens.next();
        }
        String[] PkTokens = new String[cnt];
        inputPkFieldTokens = new Scanner(PkFields);
        for(int p = 0; p < cnt; p++) {
            PkTokens[p] = inputPkFieldTokens.next();
        }
        t.deleteItemByPk(PkTokens);
    }
    
    //Choice 5 of 3rd level menu
    private static void thirdFive(Table t) {
        Scanner inputALine = new Scanner(System.in);
        System.out.println("Input the Primary Key for the tuple:");
        String pKOfTuple = inputALine.nextLine();
        int cnt = 0;
        Scanner tempString = new Scanner(pKOfTuple);
        while(tempString.hasNext()) {
            cnt++;
            tempString.next();
        }
        String []pKToken = new String[cnt];
        Scanner newTempString = new Scanner(pKOfTuple);
        int z = 0;
        while(newTempString.hasNext()) {
            pKToken[z] = newTempString.next();
            z++;
        }
        Record tempRecord = t.selectItemByPrimaryKey(pKToken);
        System.out.println("Input the field name:");
        String fieldName = inputALine.next();
        System.out.println("Input new value:");
        String newValue = inputALine.next();
        int indexForUpdating = -1;
        for(int x = 0; x < t.getFields().getNumberOfFields(); x++) {
            if(t.getFields().getField(x + 1).toString().equals(fieldName)) {
                indexForUpdating = x;
            }
        }
        if(indexForUpdating != (-1)) {
            tempRecord.setField(indexForUpdating + 1, newValue);
        }
    }
    
    //Choice 6 of 3rd level menu
    private static void thirdSix(Table t) {
        Scanner inputPk = new Scanner(System.in);
        System.out.println("Input the PKs:");
        String tempPk = inputPk.nextLine();
        Scanner inputPkToken = new Scanner(tempPk);
        int arraySizeOfPk = 0;
        while(inputPkToken.hasNext()) {
            arraySizeOfPk++;
            inputPkToken.next();
        }
        String[] token = new String[arraySizeOfPk];
        inputPkToken = new Scanner(tempPk);
        for(int o = 0; o < arraySizeOfPk; o++) {
            token[o] = inputPkToken.next();
        }
        t.assignPrimaryKey(token);
    }
    
   //Choice 7 of 3rd level menu
    private static void thirdSeven(Database d, Table t) {
        Scanner inputRefTable = new Scanner(System.in);
        System.out.println("Input refernced table:");
        Table refTable = d.getTableByName(inputRefTable.next());
        Scanner inputFk = new Scanner(System.in);
        System.out.println("Input fK");
        String fK = inputFk.next();
        System.out.println("Input referencing field:");
        Scanner inputFkHere = new Scanner(System.in);
        String fKHere = inputFkHere.next();
        t.assignForeignKey(refTable, fKHere, fK);
    }
    
    //Choice 8 of 3rd level menu
    private static void thirdEight(Table t) {
        Scanner inputColumnForRemove = new Scanner(System.in);
        System.out.println("Input the name of a column for removing:");
        t.alterTableDeleteColumn(inputColumnForRemove.next());
    }
    
    //Choice 9 of 3rd level menu
    private static void thirdNine(Table t) {
        Scanner inputColumnForAdd = new Scanner(System.in);
        System.out.println("Input the name of a column for adding:");
        t.alterTableAddColumn(inputColumnForAdd.next());
    }
    
    //Choice 10 of 3rd level menu
    private static void thirdTen(Table t) {
        Scanner inputTupleInfo = new Scanner(System.in);
        System.out.println("Input the the values for the following PK fields:");
        for(int f = 0; f < t.getPrimaryKey().size(); f++) {
            System.out.print(t.getPrimaryKey().get(f));
        }
        System.out.println();
        String PkFields = inputTupleInfo.next();
        Scanner PkFieldToken = new Scanner(PkFields);
        int numberOfPkTokens = 0;
        while(PkFieldToken.hasNext()) {
            numberOfPkTokens++;
            PkFieldToken.next();
        }
        String[] PkTokens = new String[numberOfPkTokens];
        PkFieldToken = new Scanner(PkFields);
        int indexForPkToken = 0;
        while(PkFieldToken.hasNext()) {
            PkTokens[indexForPkToken] = PkFieldToken.next();
            indexForPkToken++;
        }
        Record selectedTuple = t.selectItemByPrimaryKey(PkTokens);
        String[] tempField = new String[t.getFields().getNumberOfFields()];
        for(int i = 0; i < tempField.length; i++) {
            tempField[i] = t.getFields().getField(i + 1).toString(); 
        }
        Table tForPrint = new Table(tempField);
        tForPrint.getItems().add(selectedTuple);
        tForPrint.printTable();
    }
    
   //Choice 11 of 3rd level menu
    private static void thirdEleven(Table t, Database d) {
        File f = new File(d.getName(), t.getName() + ".txt");
        try{
            t.tableToFile(f);
        }catch(FileNotFoundException e) {
            System.out.println("File not found !");
        }
    }
}
