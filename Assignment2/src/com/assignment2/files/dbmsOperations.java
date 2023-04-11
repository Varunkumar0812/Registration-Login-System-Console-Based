package com.assignment2.files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class dbmsOperations {

	//Function to insert the record into database once all
	// the necessary details have been received from the user
	public static int insertRecord(ArrayList<String> tuple) {
		int count = 0;
		
		try {  
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Main.dbmsDatabase, Main.dbmsUser, Main.dbmsPassword);  
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setString(1, tuple.get(0));
			stmt.setString(2, tuple.get(1));
			stmt.setString(3, tuple.get(2));
			stmt.setString(4, tuple.get(3));
			stmt.setString(5, tuple.get(4));
			stmt.setString(6, tuple.get(5));
			stmt.setString(7, tuple.get(6));
			
			count = stmt.executeUpdate();
			
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return count;
	}
	
	//Function to update the new password in the database
	// after the forgot password option is choosen
	public static int updatePassword(String username, String new_password) {
		int count = 0;
			
		try {  
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Main.dbmsDatabase, Main.dbmsUser, Main.dbmsPassword);  
			PreparedStatement stmt = con.prepareStatement("UPDATE Users SET password=? WHERE username=?");
			
			stmt.setString(1, Encryption.encrypt(new_password));
			stmt.setString(2, username);
						
			count = stmt.executeUpdate();
			
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return count;
	}
}
