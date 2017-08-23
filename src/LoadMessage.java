

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class LoadMessage {
static public void Load(BloomFilter bloomFilter){
	
	Connection con = SqlConnection.getconnection();
	String sql="select ID,Price,Name,SaleVolume,PraiseNum,MediumNum,BadNum,Sore from attributes";
	try {
		java.sql.PreparedStatement s = con.prepareStatement(sql);
		ResultSet r = s.executeQuery();
		while (r.next()) {
				String date ="";
				float price;
				date+= r.getString(1);
				price=r.getFloat(2);
				date+=price;
				date+= r.getString(3);
				date+=r.getString(4);
				date+= r.getString(5);
				date+=r.getString(6);
				date+= r.getString(7);
				date+=r.getString(8);
				System.out.println(date);
				bloomFilter.add(date);	
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
	
	System.out.println(e.toString());
	}
	
}
 public static String cleazreo(String date){
	while(true){
		if(date.charAt(0)=='0'){
		date = date.substring(1,date.length());
		}
		else break;
	}
		
	return date;
	
}
static File OpenFile(){
	String path = "D:\\Message.txt";
	File file = new File(path);
	if(!file.exists()){
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		System.out.println(e.toString());
		return null;
		}
		return file;
	}
	return file;
}
static void write (String date){
	File file = OpenFile();
	date +="\r\n";
	try {
		FileWriter writer = new FileWriter(file,true);
		writer.write(date);
		writer.close();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
		
	
}

}
