package backend;

import java.sql.*;

public class dbCommands {
	
	static Connection dbConnection;
	static Statement dbStatement;
	
	static void connectDatabase()
	{
		connectDatabase("hostelManagement");
	}
	
	static void connectDatabase(String strDatabaseName)
	{
		if (dbConnection == null)
		{
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String connectionUrl =
				    "jdbc:sqlserver://localhost:1433;" +
				     "databaseName=" + strDatabaseName + ";integratedSecurity=true;" +
				     "encrypt=true;trustServerCertificate=true";
			try {
				dbConnection = DriverManager.getConnection(connectionUrl);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	static Connection getConnection()
	{
				
		return dbConnection;

	}
	
	static Statement getStatement()
	{
		//connectDatabase();
		if ( (dbConnection != null) && (dbStatement == null) )
		{
			try {
				dbStatement = dbConnection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dbStatement;

	}
	

}
