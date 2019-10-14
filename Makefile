target:
	@javac Record.java
	@javac Table.java
	@javac Database.java
	@javac Menu.java
	@javac UserInterface.java

run:
	@java -ea Record
	@java -ea Table
	@java -ea Database
	@java -ea Menu
	@java UserInterface

clean:
	rm *.class
