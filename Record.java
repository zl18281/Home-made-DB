import java.util.*;

//This class act as a tuple of a table.
public class Record {
    
    private ArrayList<StringBuilder> tuple;
    
    //Trivial Constructor.
    public Record() {
        
    }
    
    //Constructor that build a tuple with specified number of fields. 
    public Record(int numberOfFields) {
        this.tuple = new ArrayList<StringBuilder>();
        for(int i = 0; i < numberOfFields; i++) {
            this.tuple.add(new StringBuilder(""));
        }
    }
    
    //private filed "tuple" can be mutated through this !
    public ArrayList<StringBuilder> getRecord() {
        return this.tuple;
    }
    
    //Used to set a field.
    public boolean setField(int i, String s) {
        if(this.tuple != null) {
            if((i - 1) < tuple.size() && (i - 1) >= 0) {
                tuple.get(i - 1).replace(0, (tuple.get(i - 1).length()), s);
                return true;
            }else {
                return false;
            } 
        }else {
            return false;
        }
    }
    
    //Used to get a field.
    public StringBuilder getField(int i) {
        if(this.tuple != null) {
            if((i - 1) < tuple.size() && (i - 1) >= 0) {
                return this.tuple.get(i - 1);
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
    
    //Used to print the tuple.
    public void printRecord() {
        if(this.tuple != null) {
            for(int i = 0; i < (this.tuple.size() - 1); i++) {
                System.out.print(this.tuple.get(i) + " ");
            }
            System.out.print(this.tuple.get(this.tuple.size() - 1));
        }
    }
    
    //Used to get the number of Strings in a tuple.
    public int getNumberOfFields() {
        if(this.tuple != null) {
            return this.tuple.size();
        }else {
            return (-1);
        }
    }
    
    
    
    
    
    //------testing------
    
    //Testing Constructor.
    private void testConstructor() {
        //Test empty constructor.
        Record recordOne = new Record();
        
        assert(recordOne.tuple == null);
        
        //Test boundary.
        Record recordTwo = new Record(0);
        
        assert(recordTwo.tuple.size() == 0);
        
        //Test random size.
        Record recordThree = new Record(5);
        
        assert(recordThree.tuple.size() == 5);
        assert(recordThree.tuple.get(0).compareTo(new StringBuilder("")) == 0);
        assert(recordThree.tuple.get(1).compareTo(new StringBuilder("")) == 0);
        assert(recordThree.tuple.get(2).compareTo(new StringBuilder("")) == 0);
        assert(recordThree.tuple.get(3).compareTo(new StringBuilder("")) == 0);
        assert(recordThree.tuple.get(4).compareTo(new StringBuilder("")) == 0);
        System.out.println("Testing of \"Constructor\" passed !");
    }
    
    //Testing method "setField".
    private void testSetField() {
        Record recordOne = new Record(3);
        
        recordOne.setField(1, "001");
        recordOne.setField(2, "Tom");
        recordOne.setField(3, "Male");
        assert(recordOne.tuple.get(0).toString().equals("001"));
        assert(recordOne.tuple.get(1).toString().equals("Tom"));
        assert(recordOne.tuple.get(2).toString().equals("Male"));
        assert(recordOne.setField(0, "002") == false);
        assert(recordOne.setField(4, "002") == false);
        recordOne.setField(2, "Peter");
        assert(recordOne.tuple.get(1).toString().equals("Peter"));
        
        Record recordTwo = new Record();
        
        assert(recordTwo.setField(1, "001") == false);
        System.out.println("Testing of method \"setField\" passed !");
    }
    
    //Testing method "getField".
    private void testGetField() {
        Record recordOne = new Record(3);
        
        recordOne.setField(1, "001");
        recordOne.setField(2, "Tom");
        recordOne.setField(3, "Male");
        assert(recordOne.getField(1).toString().equals("001"));
        assert(recordOne.getField(2).toString().equals("Tom"));
        assert(recordOne.getField(3).toString().equals("Male"));
        assert(recordOne.getField(0) == null);
        assert(recordOne.getField(4) == null);
        
        Record recordTwo = new Record();
        
        assert(recordTwo.getField(2) == null);
        
        System.out.println("Testing of method \"getField\" passed !");
    }
    
    //Testing method "printRecord".
    private void testPrintRecord() {
        Record recordOne = new Record(3);
        
        recordOne.setField(1, "001");
        recordOne.setField(2, "Tom");
        recordOne.setField(3, "Male");
        recordOne.printRecord();
        System.out.println("\nTesting of method \"printRecord\", see what's printed");
    }
    
    //Testing method "getNumberOfFields".
    private void testGetNumberOfFields() {
        Record recordOne = new Record(3);
        
        assert(recordOne.getNumberOfFields() == 3);
        
        Record recordTwo = new Record();
        
        assert(recordTwo.getNumberOfFields() == (-1));
        System.out.println("Testing of method \"getNumberOfFields\" passed !");
    }
    
    public static void main(String[] args) {
        Record testRecord = new Record();
        testRecord.testConstructor();
        testRecord.testSetField();
        testRecord.testGetField();
        testRecord.testPrintRecord();
        testRecord.testGetNumberOfFields();
    }
}
