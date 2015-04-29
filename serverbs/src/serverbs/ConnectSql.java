package serverbs;
import java.nio.file.attribute.FileTime;
import java.sql.*;

public class ConnectSql {
   // Sterownik jdbc i nazwa bazy danych
   static final String JDBC_DRIVER 	= "com.mysql.jdbc.Driver";  
   static final String DB_URL 		= "jdbc:mysql://localhost/dirdb";

   //  Dostep do bazy
   static final String USER = "root";
   static final String PASS = "";
   
   // argumenty: atrybuty pliku dodawane do bazy danych
   public void connect(String owner, int size, FileTime czas){
   Connection conn = null;
   Statement stmt = null;

   try{
      // 1. Rejestracja sterwonika sql
      Class.forName("com.mysql.jdbc.Driver");

      // 2. Otwarcie po��czenia
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      // 3. Wykonanie zapytania do bazy
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
/*
      String sql = "SELECT * FROM User";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
         int id  = rs.getInt("User_id");
         int ip = rs.getInt("User_ip");

         System.out.print("ID: " + id);
         System.out.println(", IP: " + ip);
      }
      rs.close();

      String sql = "INSERT INTO File " +
              "VALUES (NULL, '" + owner  + "', " +  size + ", '" + czas + "', + NULL)";
 stmt.executeUpdate(sql);
*/
      System.out.println("Inserted records into the table...");
   } catch (SQLException se) {
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   } finally {
	   // 4. Zamykanie zasob�w
	   try {
		   if(stmt!=null) {
			   conn.close();
		   }
       } catch (SQLException se) {
       }// nic nie r�b

       try {
    	   if(conn!=null)
    		   conn.close();
       } catch (SQLException se) {
    	   se.printStackTrace();
       }
   }//end try
   System.out.println("Goodbye!");
   }//end main
}//end JDBCExample
