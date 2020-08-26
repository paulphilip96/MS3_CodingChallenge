import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.logging.*;
import java.sql.*;

public class MS3_CodingChallenge {

    private static Connection dbConnect;

    //Redundant function to create loading icon
	public static void modifiedPrint() {

		try {
			System.out.print("|");
			System.out.print("\b");
			Thread.sleep(100);
			System.out.print("/");
			System.out.print("\b");
			Thread.sleep(100);
			System.out.print("-");
			System.out.print("\b");
			Thread.sleep(100);
			System.out.print("\\");
			System.out.print("\b");
			Thread.sleep(100);
			System.out.print("|");
			System.out.print("\b");;
			Thread.sleep(100);
			System.out.print("/");
			System.out.print("\b");;
			Thread.sleep(100);
			System.out.print("-");
			System.out.print("\b");;
			Thread.sleep(100);
			System.out.print("\\");
			System.out.print("\b");
			Thread.sleep(100);
			System.out.print("\b");
			System.out.print("|");
			System.out.print("\b");
			System.out.print("\b");
		}

		catch (InterruptedException e) {
			System.out.println("\nInterrupted Exception Error. More details below:");
			e.printStackTrace();
		}
	}

	//Function to write statistics to LOG file
    public static void writeLOG(String fileName, int total, int valid, int invalid) {

	    try {
		    //Variable declarations
			FileHandler fh = new FileHandler(fileName+".log", true);
			Logger log = Logger.getLogger("Statistics from <"+fileName+">\n");

			//Add statistics
	    	log.addHandler(fh);
	    	System.out.println("\n");
	    	log.info("Number of records received: "+total+"\n");
	    	log.info("Number of records successful "+valid+"\n");
	    	log.info("Number of records failed "+invalid+"\n");
	    }

	    	//Error handling for writing to specified LOG file
	    	catch (IOException e) {
	    		System.out.println("Error writing to LOG file");
	    		e.printStackTrace();
	    	}
    }

	//Function to write invalid records to CSV file
    public static void writeCSV(String fileName, String[][] array, int newSize) {

	    //Variable declarations
	    String fName = fileName+"-bad.csv", newArray[][] = new String[newSize+1][10];
	    int i, j, l;
	    
	    //Remove null rows
	    for (l = 0; l < (newSize+1); l++) {
	    	newArray[l] = array[l];
	    }

	    try {
	    	//Variable declarations
	    	PrintWriter print = new PrintWriter(new File(fName));
	    	StringBuilder write_string = new StringBuilder();

	    	//Write invalid rows
	    	for (i = 0; i < newArray.length; i++) {
	    		for (j = 0; j < newArray[i].length; j++) {
	    			write_string.append(newArray[i][j]);
	    			write_string.append(",");
	    		}
	    		write_string.append("\n");
	    	}
	    	print.write(write_string.toString());
	    	print.close();	
	    }

	    //Error handling for writing to specified CSV file
	    catch (Exception e) {
	    	System.out.println("Error writing to CSV file");
	    	e.printStackTrace();
	    }
    }

    //Function to write valid entries to the database
    public static void writeDatabase(String fileName, String[][] array) {

    	//Variable declarations
    	DatabaseMetaData dbInfo;
    	Statement sql_state;
    	PreparedStatement sql_prep;
    	String sql_string_create, sql_string_insert;
    	int i,j, batchSize = 1000, count = 0;

		try {
	    	//Start the database connection and display details
	    	Class.forName("org.sqlite.JDBC");
	    	dbConnect = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
	    	dbInfo = (DatabaseMetaData) dbConnect.getMetaData();	    	
	    	System.out.println("\nExecuting connection to SQLite Database");
	    	modifiedPrint();
	    	System.out.println("\nConnection to SQLite Database successful.Details Below:");
	    	System.out.println("Driver Version: "+dbInfo.getDriverVersion());
	    	System.out.println("Driver Name: "+dbInfo.getDriverName());
	    	System.out.println("Product Version: "+dbInfo.getDatabaseProductVersion());
	    	System.out.println("Product Name: "+dbInfo.getDatabaseProductName()+"\n");

	    	//Creating database table. In the problem statement we are told there are 10 columns respective to the csv headers
	    	System.out.println("\nCreating database table \"MS3_Data\"");
	    	modifiedPrint();
	    	sql_string_create = "CREATE TABLE IF NOT EXISTS MS3_Data" +
	    					"("+array[0][0]+" TEXT NOT NULL," +
	    					""+array[0][1]+" TEXT NOT NULL," +
	    					""+array[0][2]+" TEXT NOT NULL,"+
	    					""+array[0][3]+" TEXT NOT NULL," +
	    					""+array[0][4]+" TEXT NOT NULL," +
	    					""+array[0][5]+" TEXT NOT NULL," +
	    					""+array[0][6]+" TEXT NOT NULL," +
	    					""+array[0][7]+" TEXT NOT NULL," +
	    					""+array[0][8]+" TEXT NOT NULL," +
	    					""+array[0][9]+" TEXT NOT NULL)";
	    	sql_state = dbConnect.createStatement();
	    	sql_state.executeUpdate(sql_string_create);
	    	System.out.println("Table succesfully created");

	    	//Inserting values in database table
	    	System.out.println("\nInserting entries into database table");
	    	modifiedPrint();
	    	modifiedPrint();
	    	modifiedPrint();
			System.out.print("\b");
	    	sql_string_insert = "INSERT INTO MS3_Data VALUES (?,?,?,?,?,?,?,?,?,?)";
			sql_prep = dbConnect.prepareStatement(sql_string_insert);

			//Data is inserted in segments (every 1000 rows). This is to optimize the insert speed which is better than inserting thousands of entries at once
			for (j = 0; j < array.length; j++) {
		 			sql_prep.setString(1, array[j][0]);
		 			sql_prep.setString(2, array[j][1]);
		 			sql_prep.setString(3, array[j][2]);
		 			sql_prep.setString(4, array[j][3]);
		 			sql_prep.setString(5, array[j][4]);
		 			sql_prep.setString(6, array[j][5]);
		 			sql_prep.setString(7, array[j][6]);
		 			sql_prep.setString(8, array[j][7]);
		 			sql_prep.setString(9, array[j][8]);
		 			sql_prep.setString(10, array[j][9]);
		 			sql_prep.addBatch();
		 		
		 		if ((count++ % batchSize) == 0) {
		 			sql_prep.executeBatch();
		 		}
	 		}

	 		//Execute remaining segments
	 		sql_prep.executeBatch();

	 		//Close database connectionv variables
	 		sql_prep.close();
	 		dbConnect.close();
	 		System.out.println("\nEntries inserted successfully");
	 	}

	 	//Error handling for database connection
	 	catch (ClassNotFoundException e) {
	 		System.out.println("\nDatabase class not found. Make sure:\n1. The command to execute the program includes the classpath (java -classpath \".:sqlite-jdbc-3.20.0.jar\" MS3_CodingChallenge)\n2. The sqlite-jdbc jar file in the provided respiratory is downloaded and in the same path as the program. More details below\n");
	    	e.printStackTrace();
	    }

	    //Error handling for SQL errors
	    catch (SQLException e) {
	    	System.out.println("\nSQL Error. See details below:");
	    	e.printStackTrace();
	    }
    } 

    //Function to execute core functionalities such as writing to CSV,LOG and database. This function also determines which records are valid and invalid.
    public static void core(String[][] array, String fileName) {

	    //Variable declarations
	    Boolean invalidRow = false;
	    int tempArray[] = new int[array.length], invalidCount = 0, temp = 0, nrow = 1, vrow = 1, tempCount = 0, i, j, k, l;
	    String invalidArray[][] = new String[array.length][10], validArray[][] = new String[array.length][10];

	    //Initialize headers
	    invalidArray[0] = array[0];
	    validArray[0] = array[0];

    	//Nested for loop to check index for when a row is invalid. The index values are stored in 'tempArray'
	    for (i = 0; i < array.length; i++) {

	    	colLoop:
	    	for (j = 0; j < array[i].length; j++) {

	    		if (array[i][j].isEmpty()) {
	    			invalidRow = true;
	    			invalidArray[nrow] = array[i];
	    			tempArray[tempCount] = i; 
	    			nrow++;
	    			tempCount++;
	    			break colLoop;
	    		}
	    	}

	    	if (invalidRow) {
	    		invalidCount++;
	    		invalidRow = false;
	    	}
	    }
		
		//Create a new array but skip the invalid entries
	    for (k = 1; k < array.length; k++) {

	    	if (k == tempArray[temp]) {
	    		temp++;
	    		continue;
	    	}

	    	else {
	    		validArray[vrow] = array[k];
	    		vrow++;
	    	}
	    }

	    //Remove null rows since we have the new size of valid rows
	    String finalArr[][] = new String[vrow][10];
	    for (l = 0; l < vrow; l++) {
	    	finalArr[l] = validArray[l];
	    }

		//Print statistics to console to update the user
		System.out.println(array.length+" column entries detected");
		System.out.println((array.length - invalidCount)+" entries successful");
		System.out.println(invalidCount+" entries failed");
		System.out.println("Writing successful entries to "+fileName+".db");
		System.out.println("Writing failed entries to "+fileName+"-bad.csv");
		System.out.println("Writing statistics to "+fileName+".log");

		//Execute writing to CSV file, LOG file and SQLite database
		writeCSV(fileName, invalidArray, invalidCount);
		writeLOG(fileName, array.length, (array.length - invalidCount), invalidCount);
		writeDatabase(fileName, finalArr);
    } 

    public static void main(String[] args) {

	    //Variable declarations for file and token streams
	    Scanner file = null, fileName = new Scanner(System.in); FileInputStream fileInput = null;

	    //Other variable declarations
	    String tempLine = "", delimeter = ",(?!iVBOR)", temp;
	    List<String[]> col = new ArrayList<String[]>();

	    //Print statements to find file name
	    System.out.println(" | MS3 Coding Challenge |\n");
	    System.out.println("Please enter the CSV file name:");
	    temp = fileName.nextLine();
	    System.out.println("\nFile Entered: "+temp);
	    modifiedPrint();
	    String inputFile = temp.replaceFirst(".csv", "");

	    try {
	    	//Opening and reading a file
	    	fileInput = new FileInputStream(temp);
	    	file = new Scanner(fileInput, "UTF-8");

	    	//Entries from the specified file are stored in a String Array List since the number of entries is not known yet. A regrex expression is used as the delimeter to avoid parsing in-value-commas.
			while (file.hasNextLine()) {
	    		tempLine = file.nextLine();
	    		//System.out.println(tempLine);
	    		col.add(tempLine.split(delimeter, -1));
	    	}

	    	//List is finished. Since we know the number of entries now we can convert the list to a 2D-Array with the newly discovered size. A 2D-Array is easier to access
	    	String[][] array = new String[col.size()][10];
	    	array = new String[col.size()][10];
	    	col.toArray(array);
	    	core(array, inputFile);
	    }

    //Error handling when a file cannot be opened
    catch (FileNotFoundException e) {
   		System.out.println("\nFile not found. Attempt the following:\n1. Make sure the specified file is located in the same directory of the program.\n2. Make sure the file name entered includes the <.csv> extension. More details below:\n");
   		e.printStackTrace();
    }

    finally {
     	//Error handling when file is done being read
    	if (fileInput != null) {

    		try { 
    			fileInput.close(); 
    		}

    		catch (IOException e) {
    			System.out.println("\nUnable to close file\n");
    			e.printStackTrace();
    		}
    	}
    	
		if (file != null) {
    		file.close();
    	}
    }
    }
}