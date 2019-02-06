import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class updateCount{

	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		updateInquiries("Pride and Prejudice");
//		updateInquiries("Whatever Not Existing");
	}
	
	public static void updateInquiries(String book_Name) throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		int numOfInquiries;
		  
		  try
		  {
		     // create a database connection
		     connection = DriverManager.getConnection("jdbc:sqlite:/Users/gngn/Desktop/LibraryCatalog.db");
		     Statement statement = connection.createStatement();
		     statement.setQueryTimeout(30);  
		     
		    ResultSet resultSet = statement.executeQuery("SELECT inquiries FROM catalog WHERE bookName = " + "'" + book_Name + "'");    
		    
	        if (resultSet.isBeforeFirst()) {
	        	numOfInquiries = resultSet.getInt("Inquiries");
	        	numOfInquiries += 1;
	        	statement.executeUpdate("UPDATE catalog SET inquiries = " + "'" + numOfInquiries + "' WHERE bookName = " + "'" + book_Name + "'");
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
