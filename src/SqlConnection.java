


import java.sql.DriverManager;
import com.mysql.jdbc.Connection;

public class SqlConnection { 
static	public Connection getconnection(){
	  try {  
	      Class.forName("com.mysql.jdbc.Driver");     //����MYSQL JDBC��������     
	   }  
	    catch (Exception e) {  
	    	System.out.println(e.toString());
	    
	    }  
	    try {  
	      Connection connect = (Connection) DriverManager.getConnection(  
	          "jdbc:mysql://localhost:3306/zr?useUnicode=true&characterEncoding=utf-8","root","toor");  
	      return connect;
	      
	    }  
	    catch (Exception e) {  
	    	System.out.println(""+e.toString());
 
	    }  
	  
return null;
}
 public    boolean sqlclose(Connection connection) {
	try{
	connection.close();
	}
	catch (Exception e) {
	
		System.out.println(e.toString());
	}
	return true;
}
}
