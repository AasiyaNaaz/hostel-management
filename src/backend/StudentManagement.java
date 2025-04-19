package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class StudentManagement {

	int allocateRoom(String studentID, String buildingName,int floorNumber, int roomNumber)
	{
		Statement st=dbCommands.getStatement();
		String sqlStr1 = "update student set roomID=(select ID from Room where roomNumber=" + roomNumber + "and floorID = (select ID from Floor where floorNumber = " + floorNumber + " and buildingID = (select ID from Building where buildingName='" + buildingName + "'))) where ID='" + studentID +"'";
		
	int count = 0;
		try {
			
			if (checkRoomAvailability(buildingName,floorNumber,roomNumber) == -1)
			{
				System.out.println("Room already has max students that can stay!");
				return -1;
			}
			count = st.executeUpdate(sqlStr1);
			//System.out.println(count + " record(s) updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int deallocateRoom(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String sqlStr1 = "update student set roomID =  NULL where ID ='" + studentID + "'";

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
	
	//exception handling
	void viewStudentDetails(String studentID)
	{
		
		Statement st = dbCommands.getStatement();
		String student = "SELECT * FROM STUDENT WHERE ID = '" + studentID + "'";
		ResultSet resultSet;

		try {
			resultSet = st.executeQuery(student);
			resultSet.next();
		
			int roomID = resultSet.getInt("roomID");
			System.out.println("Name: " + resultSet.getString("firstName") + " " + resultSet.getString("lastName"));
			System.out.println("Date of Birth: " + resultSet.getDate("dob").toString());
			System.out.println("Contact Number: " + resultSet.getInt("contactNumber"));
			
			String room = "SELECT * FROM ROOM WHERE ID = " + roomID;
			resultSet = st.executeQuery(room);
			resultSet.next();
			System.out.println("Room Number: " + resultSet.getInt("roomNumber"));
			int floorID = resultSet.getInt("floorID");
			
			String floor = "SELECT * FROM FLOOR WHERE ID = " + floorID;
			resultSet = st.executeQuery(floor);
			resultSet.next();
			System.out.println("Floor Number: " + resultSet.getInt("floorNumber"));
			int buildingID = resultSet.getInt("buildingID");
			
			String building = "SELECT * FROM BUILDING WHERE ID = " + buildingID;
			resultSet = st.executeQuery(building);
			resultSet.next();
			System.out.println("Building Name: " + resultSet.getString("buildingName"));
			
		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewAllHostelites()
	{
		Statement st = dbCommands.getStatement();
		
		String viewHostelites =  "SELECT S.ID, S.firstName, S.lastName, R.roomNumber, F.floorNumber, B.buildingName " +
                "FROM STUDENT S " +
                "JOIN ROOM R ON S.roomID = R.ID " +
                "JOIN FLOOR F ON R.floorID = F.ID " +
                "JOIN BUILDING B ON F.buildingID = B.ID " +
                "WHERE S.roomID IS NOT NULL";
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(viewHostelites);
			System.out.println("Student ID\tFirst Name\tLast Name\tRoom Number\tFloor Number\tBuilding Name");
			
			while (resultSet.next()) {
	            String studentID = resultSet.getString("ID");
	            String firstName = resultSet.getString("firstName");
	            String lastName = resultSet.getString("lastName");
	            int roomNumber = resultSet.getInt("roomNumber");
	            int floorNumber = resultSet.getInt("floorNumber");
	            String buildingName = resultSet.getString("buildingName");

	            // Print the student details
	            System.out.println(studentID + "\t" + firstName + "\t\t" + lastName + "\t\t" + roomNumber + "\t\t" + floorNumber + "\t\t" + buildingName);
	        }
			
			 resultSet.close();
		     st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int checkRoomAvailability(String buildingName, int floorNumber, int roomNumber)
	{
		Statement st=dbCommands.getStatement();
		String str = "select count(*) from STUDENT where roomID = (select ID from room where roomNumber = " + roomNumber +" and floorID = (select ID from Floor where floorNumber = " + floorNumber + " and buildingID = (select ID from building where buildingName = '" + buildingName + "')))";
		String room = "select roomCapacity from room where roomNumber = " + roomNumber +" and floorID = (select ID from Floor where floorNumber = " + floorNumber + " and buildingID = (select ID from building where buildingName = '" + buildingName + "'))";
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery(str);
			resultSet.next();
			int currentCapacity = resultSet.getInt(1);
			resultSet = st.executeQuery(room);
			resultSet.next();
			int maxRoomCapacity = resultSet.getInt("roomCapacity");
			
			if (maxRoomCapacity - currentCapacity == 0)
				return -1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	int changeRoom(String studentID, String buildingName,int floorNumber,int roomNumber)
	{
		deallocateRoom(studentID);
		int res = allocateRoom(studentID,buildingName,floorNumber,roomNumber);
		return res;
	}
	
	void printRoomCapacity(String buildingName,int floorNumber, int roomNumber)
	{
		/* Prints capacity for the given room roomNumber in building with name buildingName.*/
		
		Statement st=dbCommands.getStatement();
		String roomCapacity = "select count(*) as RoomCapacity from student where roomID=(select ID from Room where roomNumber=" + roomNumber +" and floorID = (select ID from Floor where floorNumber = " + floorNumber +" and buildingID = (select ID from Building where buildingName='" + buildingName + "')))";
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery(roomCapacity);
			while( resultSet.next())
	        {
	        	int count = resultSet.getInt("RoomCapacity");
	        	System.out.println("Room Capacity\n" + count);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void printRoomsCapacity(String buildingName,int floorNumber)
	{
		/* Prints room-wise capacity for Building with name buildingName*/
		
		Statement st=dbCommands.getStatement();
		String sqlStr = "SELECT COUNT(*) AS StudentCount, R.roomNumber,F.floorNumber, B.buildingName FROM Student JOIN Room R ON Student.roomID = R.ID JOIN Floor F ON R.floorID = F.ID JOIN Building B ON F.buildingID = B.ID where F.floorNumber = " + floorNumber + " and B.buildingName = '" + buildingName + "' GROUP BY R.roomNumber ,F.floorNumber, B.buildingName";
		
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery( sqlStr );
			System.out.println("Student Count\tRoom Number\tFloor Number\tBuilding Name");
			 while(resultSet.next())
		        {
		        	int count = resultSet.getInt("StudentCount");
		        	int roomNumber = resultSet.getInt("roomNumber");
		        	
		        	System.out.println(count + "\t\t" + roomNumber + "\t\t" + floorNumber + "\t\t" + buildingName);
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void printRoomsCapacity(String buildingName)
	{
		/* Prints room-wise capacity for Building with name buildingName*/
		
		Statement st=dbCommands.getStatement();
		String sqlStr = "SELECT COUNT(*) AS StudentCount, R.roomNumber,F.floorNumber, B.buildingName FROM Student JOIN Room R ON Student.roomID = R.ID JOIN Floor F ON R.floorID = F.ID JOIN Building B ON F.buildingID = B.ID where B.buildingName = '" + buildingName + "' GROUP BY R.roomNumber ,F.floorNumber, B.buildingName";
		
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery( sqlStr );
			System.out.println("Student Count\tRoom Number\tFloor Number\tBuilding Name");
			 while(resultSet.next())
		        {
		        	int count = resultSet.getInt("StudentCount");
		        	int roomNumber = resultSet.getInt("roomNumber");
		        	int floorNumber = resultSet.getInt("floorNUmber");
		        	
		        	System.out.println(count + "\t\t" + roomNumber + "\t\t" + floorNumber + "\t\t" + buildingName);
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void printRoomsCapacity()
	{
		/* Prints room-wise capacity for Building with name buildingName*/
		
		Statement st=dbCommands.getStatement();
		String sqlStr = "SELECT COUNT(*) AS StudentCount, R.roomNumber,F.floorNumber, B.buildingName FROM Student JOIN Room R ON Student.roomID = R.ID JOIN Floor F ON R.floorID = F.ID JOIN Building B ON F.buildingID = B.ID GROUP BY R.roomNumber ,F.floorNumber, B.buildingName";
		
		ResultSet resultSet;
		try {
			resultSet = st.executeQuery( sqlStr );
			System.out.println("Student Count\tRoom Number\tFloor Number\tBuilding Name");
			 while(resultSet.next())
		        {
		        	int count = resultSet.getInt("StudentCount");
		        	int roomNumber = resultSet.getInt("roomNumber");
		        	int floorNumber = resultSet.getInt("floorNUmber");
		        	String buildingName = resultSet.getString("buildingName");
		        	
		        	System.out.println(count + "\t\t" + roomNumber + "\t\t" + floorNumber + "\t\t" + buildingName);
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewEntries()
	{
		Statement st=dbCommands.getStatement();
		String checkin = "select studentID,Time_stamp,InOutRegister.guardianContact,S.firstName,S.lastName from InOutRegister JOIN STUDENT S ON InOutRegister.studentID = S.ID where status = 0";		
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(checkin);
			
			int i = 1;
			System.out.println("S.No\tStudent ID\tName\t\tParents Contact\t\tEntry Time");
			while(resultSet.next())
			{
				String studentID = resultSet.getString("studentID");
				Timestamp timestamp = resultSet.getTimestamp("Time_stamp");
				long contact = resultSet.getLong("guardianContact");
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				
				System.out.println(i + "\t" + studentID + "\t" + name + "\t" + contact + "\t\t\t" + timestamp);
				i++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewExits()
	{
		Statement st=dbCommands.getStatement();
		String checkin = "select studentID,Time_stamp,InOutRegister.guardianContact,S.firstName,S.lastName from InOutRegister JOIN STUDENT S ON InOutRegister.studentID = S.ID where status = 1";		
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(checkin);
			
			int i = 1;
			System.out.println("S.No\tStudent ID\tName\t\tParents Contact\t\tExit Time");
			while(resultSet.next())
			{
				String studentID = resultSet.getString("studentID");
				Timestamp timestamp = resultSet.getTimestamp("Time_stamp");
				long contact = resultSet.getLong("guardianContact");
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				
				System.out.println(i + "\t" + studentID + "\t" + name + "\t" + contact + "\t\t\t" + timestamp);
				i++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
