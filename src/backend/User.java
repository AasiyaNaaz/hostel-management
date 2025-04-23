package backend;

import java.sql.*;	

public class User {

	public String currentUser;
	userTypes ut;
	public enum userTypes {
		Admin,
		Student,
		SuperAdmin
		}
	
	String checkUserType(String username)
	{
		Statement st=dbCommands.getStatement();
		String sqlStr ="select userType from USERS where username = '" + username + "'";
		ResultSet resultSet;
		String userType = "";
		try {
			resultSet = st.executeQuery(sqlStr);
			resultSet.next();
			userType = resultSet.getString("userType");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return userType;
	}
	
	int createUser(String username,String pwd,userTypes userType,String email)
	{
		Statement st=dbCommands.getStatement();
		String sqlStr ="SELECT * FROM STUDENT WHERE ID = '" + username + "'";
		ResultSet resultSet;
		int count = 0;
		
		try {
			//Check if student exists in student table.
			resultSet = st.executeQuery(sqlStr);
			if (!resultSet.next()) 
			{
				System.out.println("Student does not exists..!!!");
				return -1;
			}
			
			//Check if user already exists.
			String sqlStr1 ="SELECT * FROM USERS WHERE username = '" + username + "'";
			resultSet = st.executeQuery(sqlStr1);
			if (resultSet.next()) 
			{
				System.out.println("user already exists..!!!");
				return -2;
			}
			
			sqlStr1 = "INSERT INTO USERS (username,password,userType,emailID) values ('" + username + "', '" + pwd +"', '" + userType +"','" + email + "')";			
			count = st.executeUpdate(sqlStr1);
				//System.out.println(count + " record(s) updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
		
	}
	
	int forgetPassword(String username, String newPassword)
	{

		Statement st=dbCommands.getStatement();
		String sqlStr1 = "UPDATE USERS SET password = '" + newPassword + "' where username ='" + username + "'";

		int count = 0;
		try {
			count = st.executeUpdate(sqlStr1);
			//System.out.println(count + " record(s) updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int changePassword(String studentID, String oldPassword, String newPassword)
	{

		Statement st=dbCommands.getStatement();
		String sqlStr1 = "UPDATE USERS SET password = '" + newPassword + "' where username ='" + studentID + "'";
		String check = "Select password from users where username = '" + studentID + "'";
		int count = 0;
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery(check);
			
			if (resultSet.next())
			{
				if (oldPassword.equals(resultSet.getString("password")))
				{
					count = st.executeUpdate(sqlStr1);	
				} else {
					return -1;
				}
			} else {
				return -2;
			}
			
			//System.out.println(count + " record(s) updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int login(String userID,String password)
	{
		ResultSet resultSet;
		
		try {
			Statement st=dbCommands.getStatement();
			String sqlStr ="SELECT password FROM USERS WHERE username = '" + userID + "'";
			resultSet = st.executeQuery(sqlStr);
			if (!resultSet.next()) 
			{
				//System.out.println("user does not exists.");
				return -1;
			}
			String str1 = resultSet.getString("password");
			if (str1.equals(password))
			{
				this.currentUser = userID;
				//System.out.println("LOGIN SUCCESSFULL!");
			} else {
				//System.out.println("Wrong password!");
				return -2;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 1;
		
	}
	
	int logout() 
	{
		if(this.currentUser == null)
		{
			System.out.println("User isnt logged in!");
			return -1;
		}
		this.currentUser = null;
		System.out.println("Succesfully logged out.");
		return 1;
	}

}
