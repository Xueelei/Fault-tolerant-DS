import java.sql.*;

public class checkAvailability {

    public static void main(String[] args) throws ClassNotFoundException {
        // TODO Auto-generated method stub
        System.out.println(checkStatus("Pride and Prejudice")); //Yes
        System.out.println(checkStatus("Gone Girl")); //No - not in the catalog
        System.out.println(checkStatus("The Kite Runner")); //No
    }
    
    
    public static String checkStatus(String book_Name) throws ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        String status = "Not available";
          
          try
          {
             // create a database connection
             connection = DriverManager.getConnection("jdbc:sqlite:/Users/hyunjungkim/Desktop/LibraryCatalog.db");
             Statement statement = connection.createStatement();
             statement.setQueryTimeout(30);
            
             
            ResultSet resultSet = statement.executeQuery("SELECT availability FROM catalog WHERE bookName = " + "'" + book_Name + "'");
          
            if (resultSet.isBeforeFirst()) {
                status = resultSet.getString("availability");
                if (status.equals("Yes")) {
                	status = "Available";
                }
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
          
          
        return status;
    }
}