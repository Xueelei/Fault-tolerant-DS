import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class checkpointing {

	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		Map<String, Integer> sampleCatalog = new HashMap<String, Integer>() {{ put("A", 10); put("B", 7); put("Jane Eyre", 19); }};
		createCheckpoint();
		processCheckpoint(sampleCatalog);
		
	}
	
	public static HashMap createCheckpoint() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		  
	    Map<String, Integer> checkpoint = new HashMap<String, Integer>();

		  try
		  {
		     // create a database connection
		     connection = DriverManager.getConnection("jdbc:sqlite:/Users/gngn/Desktop/LibraryCatalog.db");
		     Statement statement = connection.createStatement();
		     statement.setQueryTimeout(30);  
		     
		    ResultSet res = statement.executeQuery("SELECT * FROM catalog");
		    
		    
		    while (res.next()) {
		    	String bookName = res.getString("bookName");
		    	Integer inquiries = res.getInt("inquiries");
		    	checkpoint.put(bookName, inquiries);
		    	System.out.println(checkpoint);
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
		  
		  return (HashMap) checkpoint;
	}
	
	public static void processCheckpoint(Map<String, Integer> checkpoint) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		  
		try
		{
		     // create a database connection
			  connection = DriverManager.getConnection("jdbc:sqlite:/Users/gngn/Desktop/LibraryCatalog.db");
			  Statement statement = connection.createStatement();
			  statement.setQueryTimeout(30);  

			  for (String eachBook: checkpoint.keySet())  {
				  System.out.println("here");
				  int numOfInquiries = checkpoint.get(eachBook);
				  statement.executeUpdate("UPDATE catalog SET inquiries = " + "'" + numOfInquiries + "' WHERE bookName = " + "'" + eachBook + "'");  
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
