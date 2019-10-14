Final version:

(.java files outside zip files (directly uploaded) are the final codes.)

1. User interface are split into several smaller functions for meeting dry code standards

2. When creating a database, a folder with the same name is created. Then, when storing tables in this database to files will only create files in this folder



Extensions:

1. A text user interface

2. PK and FK constraint are implemented:

	a. PK can be assigned. Composite PK is enabled. When inserting data, PK field cannot repeat.

	b. FK can be assigned. When inserting data, value for FK field must exist in refered table. When deleting data, this data must not be refered by another tuple in another table. When droping a table, this table must not be refered by another table(there is an order for dropping tables according to FK constraint). 



Reports for each stage: 


Stage 1:

Class Record function as a tuple of a table including the zeroth row (the attributes of a table).

1. An array list, with StringBuilder as its storing object type, is used to represent a tuple. 

2. The second constructor build a tuple with specified number of fields, setting the content of each field to an empty string.

3. Function "setField" receives the index of the field and candidate string as arguments and set the field of this index to this string.

3. Function "getField" receives an index and return the object(a StringBuilder) with this index, thus getting the conrresponding field.

4. Also, some other setter and getter methods are added, including obtaining the number of fields in a tuple.

5. The main design idea is providing some public interfaces for the upper class (Table) to access.



Stage 2:

This stage adds the class "Table" which acts as a Table (Grouping the attribute row and tuples into one). Some features might be missing but were implemented later (some issue came up later as developing went).

1. The property "feature" is a "Record" type to store the attributes. The "items" is an array list of "Record" type to store the data.

2. The Second Constructor initialise the table arributes and build a empty "bowl" for the data. 

3. The main public functions include those for adding/deleting a column, inserting/deleting a tuple, selecting a tuple and printing the table in a clean format. Other private functions are designed to assist the public functions.

note: Printing neatly is implemented here.



Stage 3:

This stage mainly adds the function of saving the table to files and reading data from files.

1. Reading a file concerns the process of storing the tokens to a 2D array according to each token's row and column positions. And the first line is treated as the attribute row, while the rest are stored into items row by row.

2. Wrtting to a file concerns the establishing output stream to this file and print to file line by line (within the same line, tokens are seperated by spaces and lines are seperated by "newline" character). This is consistent with the format needed by File Reading.

3. Up to this point, FileNotFound exception was not considered but was added later. 



Stage 4:

Primary key function is added.

1. I used an array list to store PKs, thus composite PK is fulfilled.

2. Functions include assigning PK, checking PK constriant when inserting data, selecting a row by PK values.



Stage 5:

Part of Foreign Key function is fulfilled.

1. I used two HashMap to represent the FK relationship. One for storing all the Referred Table/Attibute pair and the other for linking the attribute in this table with that of the referred table.

2. FK constriant can be checked when inserting data, but deleting data while not violating FK constriant are fulfilled later. 



Stage 6:

The class "Database" is added.

1. The main use of this class is to store the Tables into one entity. 

2. Tables can be created by providing field names and can be dropped. However, at this stage, the order for dropping tables is not fulfilled (not violating FK constriant).



Stage 7:

1. Menu class is added to print the first, second and third level menus

2. UserInterface class use nested do---while loop to implement the menu function. Public functions/interfaces in class "Database" and "Table" are called to do the actual task of creating/drop database, create/drop table, inserting/deleting data, etc.



Stage 8:

This part mainly tackled the issue of FK constriant when deleting data and table.

Another hashmap is added to "Table" class to record those table/attribute pairs that refer this table. A table cannot be deleted until all the tables depending on this table are deleted. A record can not be deleted until all the records that depending on this record are deleted. Before deleting a data or a table, its record of dependency in the hashmap is deleted first.




note: testings are carried out for most of the methods.


