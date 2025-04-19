package backend;

import java.util.*;
import java.sql.*;

public class FacilityManagement {

	int createBuilding(String buildingName,int floorCount, HashMap<Integer, Integer> map)
	//int createBuilding(String buildingName, int floorCount)
	{
		ResultSet generatedKeys;
		Connection con = dbCommands.getConnection();
		Statement st = dbCommands.getStatement();
		String strSql = "INSERT INTO BUILDING (buildingName, floorCount) VALUES('" + buildingName + "', " + floorCount + ")";
		
		try
		{
			PreparedStatement preparedStatement = con.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
		
			int res = preparedStatement.executeUpdate();

			if (res == 1)
			{
				generatedKeys = preparedStatement.getGeneratedKeys();
				generatedKeys.next();
				int buildingID = generatedKeys.getInt(1);
			
				for (int i=1; i<= floorCount; i++)
				{
					int roomCount = map.get(i);
					strSql = "INSERT INTO FLOOR VALUES(" + i + ", " + roomCount + ", " + buildingID + ")";
					preparedStatement = con.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
					
					res = preparedStatement.executeUpdate();

					if (res == 1)
					{
						generatedKeys = preparedStatement.getGeneratedKeys();
						generatedKeys.next();
						int floorID = generatedKeys.getInt(1);
						for (int j=1; j<= roomCount; j++)
						{
							strSql = "INSERT INTO ROOM VALUES(" + (i*100 + j) + ", " + floorID + ", " + 4 + ")";
							res = st.executeUpdate(strSql);
						}
					}
					
				}
			}
			System.out.println(res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	int removeBuilding(String buildingName)
	{
		Statement st = dbCommands.getStatement();
		String check = "Select ID from BUILDING where buildingName = '" + buildingName + "' ";
		String remove = "DELETE FROM ROOM WHERE floorID IN (SELECT ID FROM FLOOR WHERE buildingID in (Select ID from BUILDING where buildingName = '" + buildingName + "') ) DELETE FROM FLOOR WHERE buildingID in (Select ID from BUILDING where buildingName = '" + buildingName + "') DELETE FROM BUILDING WHERE ID in (Select ID from BUILDING where buildingName = '" + buildingName + "')";
		ResultSet resultSet;
		int count = 0;
		try {
			resultSet = st.executeQuery(check);
			if (!resultSet.next())
			{
				System.out.println("Building does not exists:(");
				return -1;
				
			}
			count = st.executeUpdate(remove);                                                                                                                                                                                                                                                                      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	void printBuildingDetails(String buildingName)
	{

		Statement st = dbCommands.getStatement();
		String sqlStr = "select * from building where ID = (select ID from BUILDING where buildingName ='" + buildingName + "')";
		String sqlStr2 = "select * from floor where buildingID = (select ID from BUILDING where buildingName ='" + buildingName + "')";
		ResultSet resultSet;

		try {
			resultSet = st.executeQuery(sqlStr);
			resultSet.next();
		
						
			System.out.println("Building Name: " + resultSet.getString("buildingName"));
			System.out.println("Floor Count: " + resultSet.getInt("floorCount"));
			String adminID = resultSet.getString("adminID");			
			resultSet = st.executeQuery(sqlStr2);
			
			int roomCount = 0;
			while(resultSet.next())
			{
				roomCount = roomCount + resultSet.getInt("roomCount");
			}
			
			System.out.println("Room Count: : " + roomCount);	
			System.out.println("Assigned Admin: " + adminID);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	int addFacility(String buildingName,String facilityName,int capacity)
	{
		Statement st = dbCommands.getStatement();
		String facility = "insert into facility (buildingName ,facilityName,capacity,status) values ('" + buildingName + "','" + facilityName +"'," + capacity +",1)";
		int count = 0;
		try {
			count = st.executeUpdate(facility);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	int removeFacility(String buildingName,String facilityName)
	{
		Statement st = dbCommands.getStatement();
		String facility = "delete from facility where buildingName = '" + buildingName + "' and facilityName = '" + facilityName + "'";
		int count = 0;
		try {
			count = st.executeUpdate(facility);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	//work on this
	int blockFacility(String buildingName,String facilityName)
	{
		Statement st = dbCommands.getStatement();
		String remove = "delete from reservation where facilityID = (select facilityID from facility where facilityName = '" + facilityName + "' and buildingName = '" +buildingName + "') "
		String block = "update facility set status = 0 where facilityName =  '" + facilityName + "' and buildingName = '" + buildingName + "'";
		int count = 0;
		try {
			count = st.executeUpdate(remove) + st.executeUpdate(block);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	int makeFacilityAvailable(String buildingName,String facilityName)
	{
		Statement st = dbCommands.getStatement();
		String unblock = "update facility set status = 1 where facilityName =  '" + facilityName + "' and buildingName = '" + buildingName + "'";
		int count = 0;
		try {
			count = st.executeUpdate(unblock);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	void viewAllFacilities()
	{
		Statement st = dbCommands.getStatement();
		String facility = "select * from facility" ;
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(facility);
			
			System.out.println("facilityID\tBuilding Name\tFacility Name\tCapacity\tStatus");
			while(resultSet.next())
			{
				int facilityID = resultSet.getInt("facilityID");
				String buildingName = resultSet.getString("buildingName");
				String facilityName = resultSet.getString("facilityName");
				int capacity = resultSet.getInt("capacity");
				String status = resultSet.getInt("status") == 1 ? "Available" : "Blocked";
				
				System.out.println(facilityID + "\t" + buildingName + "\t" + facilityName + "\t" + capacity + "\t" + status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewAllFacilities(String buildingName)
	{
		Statement st = dbCommands.getStatement();
		String facility = "select * from facility where buildingName = '" + buildingName + "'" ;
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(facility);
			
			System.out.println("facilityID\tBuilding Name\tFacility Name\tCapacity\tStatus");
			while(resultSet.next())
			{
				int facilityID = resultSet.getInt("facilityID");
				String facilityName = resultSet.getString("facilityName");
				int capacity = resultSet.getInt("capacity");
				String status = resultSet.getInt("status") == 1 ? "Available" : "Blocked";
				
				System.out.println(facilityID + "\t" + buildingName + "\t" + facilityName + "\t" + capacity + "\t" + status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewFacility(String facilityName)
	{
		Statement st = dbCommands.getStatement();
		String facility = "select * from facility where facilityName = '" + facilityName + "'" ;
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(facility);
			
			System.out.println("facilityID\tBuilding Name\tFacility Name\tCapacity\tStatus");
			while(resultSet.next())
			{
				int facilityID = resultSet.getInt("facilityID");
				String buildingName = resultSet.getString("buildingName");
				int capacity = resultSet.getInt("capacity");
				String status = resultSet.getInt("status") == 1 ? "Available" : "Blocked";
				
				System.out.println(facilityID + "\t" + buildingName + "\t" + facilityName + "\t" + capacity + "\t" + status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void displayCurrentUsers(String buildingName,String facilityName)
	{
		Statement st = dbCommands.getStatement();
		String reservation = "select *,S.firstName,S.lastName from reservation JOIN STUDENT S ON studentID = S.ID where facilityID = (select facilityID from facility where facilityName = '" + facilityName +"' and buildingName = '" + buildingName + "')";
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(reservation);
			
			//System.out.println("facilityID\tBuilding Name\tFacility Name\tCapacity\tCurrent Capacity\tStatus\tCurrent Users");
			while(resultSet.next())
			{
				String name = resultSet.getString("firstName") + resultSet.getString("lastName");
				int facilityID = resultSet.getInt("facilityID");
				String dateForReservation = resultSet.getDate("dateForReservation").toString();
				String startTime = resultSet.getTime("startTime").toString();
				String endTime = resultSet.getTime("endTime").toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
