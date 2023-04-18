package com.assignment2.files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validation {
		
	//Function to check if the  given DOB is a valid date
	public static boolean dateValidity(String date) {
	    try {
	        LocalDate.parse(date);
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}
	
	//Function to check if the first name and last name entered
	// match the requirements
	// Name can contain letters (both upper and lower case) and numbers
	public static boolean nameValidity(String name) {
		return Pattern.compile("^[a-zA-Z0-9]+$").matcher(name).matches();
	}
	
	//Function to check if the username entered match the 
	// necessary requirements :
	// Username should contain only letters(only lower case), numbers and underscore
	// No other symbols are allowed
	public static boolean usernameValidity(String username) {
		return Pattern.compile("^[a-z0-9-]+$").matcher(username).matches();
	}
	
	//Function to check if the password entered match the 
	// requirements in-order to be a strong password.
	// The requirements are as follows :
	//      i) Minimum length 8 characters
	//     ii) At least one Upper case letter
	//    iii) At least one number
	//     iv) At least one symbol
	public static int passwordValidity(String password) {
		int uppercase = 0;
		int symbols   = 0;
		int numbers   = 0;
			
		String numberSet = "0123456789";
		String symbolSet = "`~!@#$%^&*()_-+=/'|[]{};:\\\"";
		
		for(int i = 0;i < password.length();i++) {
			if(Character.isUpperCase(password.charAt(i))) {
				uppercase += 1;
			}
			else if(numberSet.contains(String.valueOf(password.charAt(i)))) {
				numbers += 1;
			}
			else if(symbolSet.contains(String.valueOf(password.charAt(i)))) {
				symbols += 1;
			}
		}
				
		if (password.length() <= 8) {
			return 1;
		}
		else if(uppercase == 0 && numbers == 0 && symbols == 0) {
			return 2;
		}
		else if(uppercase == 0 && numbers == 0 && symbols != 0) {
			return 3;
		}
		else if(uppercase == 0 && symbols == 0 && numbers != 0) {
			return 4;
		}
		else if(numbers == 0 && symbols == 0 && uppercase != 0) {
			return 5;
		}
		else if(uppercase == 0 && numbers != 0 && symbols != 0) {
			return 6;
		}
		else if(numbers == 0 && uppercase != 0 && symbols != 0) {
			return 7;
		}
		else if(symbols == 0 && uppercase != 0 && numbers != 0) {
			return 8;
		}
		else {
			return 9;
		}
	}

	// Function to establish database connection
	public static ResultSet dbmsConnection(String statement) {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Main.dbmsDatabase, Main.dbmsUser, Main.dbmsPassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(statement);
			return rs;
		}
		catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//Function to check if the username already exists in the database
	// Returns true if the username doesn't exist
	public static boolean usernameExists(String username) {
		try {  
			ResultSet rs = dbmsConnection("SELECT username FROM Users");  
			while(rs.next())  {
				if(rs.getString(1).equals(username)) {
					return false;
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return true;
	}
	
	//Function to check whether the username exists in the database
	// while logging in
	public static boolean loginValidity(String username, String password) {
		try {  
			ResultSet rs = dbmsConnection("SELECT username, password FROM Users WHERE username=\"" + username + "\""); 
			while(rs.next()) { 
				if(!rs.getString(2).equals(Encryption.encrypt(password))) {
					return false;
				}
			}
		}
		catch(Exception e) {
				System.out.println(e);
		}
		return true;
	}
	
	//Function to check whether the username exists and if the
	// recovery email is correct while applying for 
	// forgot password
	public static boolean forgotPasswordValidity(String username, String recoveryEmail) {
		try {  
			ResultSet rs = dbmsConnection("SELECT username, recovery_email FROM Users WHERE username=\"" + username + "\"");  
			while(rs.next()) { 
				if(!rs.getString(2).equals(recoveryEmail)) {
					return false;
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return true;
	}
}