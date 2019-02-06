import java.sql.*;

public class Database
{
	// only to initiate the database for the first time
	public static void main(String[] args) throws ClassNotFoundException
	 {
	  // load the sqlite-JDBC driver using the current class loader
	  Class.forName("org.sqlite.JDBC");
	  Connection connection = null;
	  
	  try
	  {
	     // create a database connection
	     connection = DriverManager.getConnection("jdbc:sqlite:/Users/gngn/Desktop/LibraryCatalog.db");
	
	     Statement statement = connection.createStatement();
	     statement.setQueryTimeout(30);  // set timeout to 30 sec.
	
	     statement.executeUpdate("DROP TABLE IF EXISTS catalog");
	     statement.executeUpdate("CREATE TABLE catalog (bookName STRING, availability STRING, inquiries INTEGER)");
	
	     String bookNames [] = {"Pride and Prejudice","Little Fires Everywhere","Great Expectations","Jane Eyre","The Kite Runner"};
	     String availabilities [] = {"Yes","No","Yes","Yes","No"};
	     Integer inquiries [] = {0,0,0,0,0};
	     
	     for(int i=0;i<bookNames.length;i++){
	         statement.executeUpdate("INSERT INTO catalog values('"+bookNames[i]+"','"+availabilities[i]+"','"+inquiries[i]+"')");   
	    }
	
	     //This is how to update book availability 
//	    statement.executeUpdate("UPDATE catalog SET availability='Yes' WHERE bookName='Great Expectations'");
	
	      ResultSet resultSet = statement.executeQuery("SELECT * from catalog");
	   
	      while(resultSet.next())
	      {
	         System.out.println("Book Name = " + resultSet.getString("bookName"));
	         System.out.println("Availability = " + resultSet.getString("availability"));
	         System.out.println("Number of Inquiries = " + resultSet.getString("inquiries"));
	      }
	      
	      
	  }
	  
	  catch(SQLException e)
	  {  
		  System.err.println(e.getMessage()); 
      }
	  
	  finally {         
	        try {
	              if(connection != null)
	                 connection.close();
	              }
	        catch(SQLException e) {  // Use SQLException class instead.          
	           System.err.println(e); 
	         }
	  }
	}
}