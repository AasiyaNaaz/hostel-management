package backend;

import java.sql.*;

public class Student {
	String studentID;
	

	//fill all the entries later
	ResultSet getProfile(String studentID)
	{

		Statement st = dbCommands.getStatement();
		String sqlStr = "SELECT * FROM STUDENT WHERE ID = '" + studentID + "'";
		ResultSet resultSet = null;

		try {
			resultSet = st.executeQuery(sqlStr);
			//resultSet.next();
		
			
			/*for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
			{
				System.out.print(resultSet.getObject(i) + "\t");
			}*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	ResultSet getRoomDetails(String studentID)
	{
		Statement st = dbCommands.getStatement();
		String sqlStr = "SELECT * ,F.floorNumber,B.buildingName FROM ROOM JOIN FLOOR F ON ROOM.floorID = F.ID JOIN BUILDING B ON F.buildingID = B.ID WHERE Room.ID = (SELECT roomID FROM STUDENT WHERE ID = '" + studentID + "')";
		ResultSet resultSet = null;
		
		try {
			resultSet = st.executeQuery(sqlStr);
			//resultSet.next();
			/*if (!resultSet.next())
			{
				System.out.println("Student isnt assigned to any of the rooms.");
				return -1;
			}
			/*System.out.println("Room Number: " + resultSet.getInt("roomNumber"));
			System.out.println("Floor Number: " + resultSet.getInt("floorNumber"));
			System.out.println("Building Name: " + resultSet.getString("buildingName"));*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	int checkOut(String studentID, String cityOfVisit, long contact)
	{
		Statement st=dbCommands.getStatement();
		String checkout = "insert into InOutRegister (studentID,Status,Time_stamp, placeOfVistit,guardianContact) values ('" + studentID + "',1,GETDATE(),'"+ cityOfVisit +"',"+ contact+ ")";
		String roomExists = "select roomID from student where ID = '" + studentID + "'";
		String checkoutStatus = "select checkOutStatus from student where ID = '" + studentID + "'";
		String status = "update STUDENT SET checkOutStatus = 1 where ID = '" + studentID + "'";
		String datetime = "UPDATE INOUTREGISTER SET Time_stamp = GETDATE() ";
		ResultSet resultSet;
		int count = 0;
		try {
			resultSet = st.executeQuery(roomExists);
			resultSet.next();
			if (resultSet.getInt("roomID") == 0)
			{
				System.out.println("Student isnt assigned to room yet and cannot check out");
				return -1;
			}
			resultSet = st.executeQuery(checkoutStatus);
			resultSet.next();
			if (resultSet.getBoolean("checkOutStatus"))
			{
				System.out.println("student is already checked out!");
				return -2;
			}
			count = st.executeUpdate(checkout) + st.executeUpdate(status) + st.executeUpdate(datetime);
			
			//System.out.println(count + " record(s) updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int checkIn(String studentID,long contact)
	{
		Statement st=dbCommands.getStatement();
		String status = "select checkOutStatus from STUDENT WHERE ID = '" + studentID + "'";
		String checkout = "insert into InOutRegister (studentID,Status,Time_stamp ,guardianContact) values ('" + studentID + "',0,GETDATE(),'"+ contact + "')";
		String changeStatus = "update STUDENT SET checkOutStatus = 0 WHERE ID = '" + studentID +"'";
		ResultSet resultSet;
		int count = 0;
		try {
			resultSet = st.executeQuery(status);
			resultSet.next();
			if (!resultSet.getBoolean("checkOutStatus"))
			{
				System.out.println("student is already checked in!");
				return -1;
			}
			count = st.executeUpdate(checkout) + st.executeUpdate(changeStatus);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	ResultSet viewRoommates(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String student = "select ID, firstName,lastName,contactNumber from Student where roomID = (select roomID from Student where ID = '" + studentID + "')";
		ResultSet resultSet = null;
		
		try {
			resultSet = st.executeQuery(student);
			/*System.out.println("Student ID\tFirst Name\tLast Name");
			while (resultSet.next())
			{
				System.out.println(resultSet.getString("ID") + "\t" + resultSet.getString("firstName") + "\t" + resultSet.getString("lastName"));			
			} */
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	ResultSet viewFeeRecords(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String fee = "select feeID,academicYear, academicYear,totalFee,dueDate, paymentStatus,S.firstName,S.lastName from FEEMANAGEMENT JOIN STUDENT S ON studentID = S.ID WHERE studentID = '" + studentID + "'";
		ResultSet resultSet = null;
		
		try {
			//System.out.println("Fee ID\tStudent ID\tName\t\tAcademic Year\tTotal Fee\tDue Date\tPayment Status");
			resultSet = st.executeQuery(fee);
			/*while (resultSet.next())
			{
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				String status = resultSet.getInt("paymentStatus") == 1 ? "Paid" : "Not Paid";
				System.out.println(resultSet.getInt("feeID") + "\t" + studentID + "\t" + name + "\t" + resultSet.getString("academicYear") + "\t" + resultSet.getString("totalFee") + "\t\t" + resultSet.getDate("dueDate") + "\t" + status );			
			} */
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	int payFees(String studentID,String acadyr)
	{
		int randomNum = (int)(Math.random() * 1000000) - (int)(Math.random() * 100000); 
		Statement st=dbCommands.getStatement();
		String fee = "update feemanagement set receiptNumber = " + randomNum + " where studentID = '" + studentID + "' and academicYear = '" + acadyr + "'";
		String fee2 = "update feemanagement set amountPaid = (select totalFee from feemanagement where studentID ='" + studentID + "') where studentID = '" + studentID + "' and academicYear = '" + acadyr + "'";
		String fee3 = "update feemanagement set paymentstatus = 1 where studentID = '" + studentID + "' and academicYear = '" + acadyr + "'";
		int count= 0;
		
		try {
			count = st.executeUpdate(fee) + st.executeUpdate(fee2) + st.executeUpdate(fee3);
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	ResultSet viewReceipts(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String fee = "select feeID,totalFee,amountPaid, receiptNumber,S.firstName,S.lastname from FEEMANAGEMENT JOIN STUDENT S ON studentID = S.ID WHERE studentID = '" + studentID + "' and receiptNumber is not null";
		ResultSet resultSet = null;
		
		try {
			
			//System.out.println("Student ID\tName\t\tFee ID\t  Receipt Number\tTotal Fee\tAmount Paid");
			resultSet = st.executeQuery(fee);
			/*
			while (resultSet.next())
			{
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				int feeID = resultSet.getInt("feeID");
				int receiptNumber = resultSet.getInt("receiptNumber");
				int totalFee = resultSet.getInt("totalFee");
				int amountPaid = resultSet.getInt("amountPaid");
				System.out.println(studentID + "\t" + name + "\t" + feeID + "\t  " + receiptNumber + "\t\t" + totalFee + "\t\t" + amountPaid);			
			}
			*/
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	ResultSet viewDues(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String fee = "select feeID,academicYear, academicYear,totalFee,dueDate,S.firstName,S.lastName from FEEMANAGEMENT JOIN STUDENT S ON studentID = S.ID WHERE studentID = '" + studentID + "' and paymentStatus = 0";
		ResultSet resultSet = null;
		
		try {;
			//System.out.println("Fee ID\tStudent ID\tName\t\tAcademic Year\tTotal Fee\tDue Date");
			resultSet = st.executeQuery(fee);
			/*while (resultSet.next())
			{
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				System.out.println(resultSet.getInt("feeID") + "\t" + studentID + "\t" + name + "\t" + resultSet.getString("academicYear") + "\t" + resultSet.getString("totalFee") + "\t\t" + resultSet.getDate("dueDate") );			
			} */
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
	ResultSet viewAllFacilities()
	{
		Statement st = dbCommands.getStatement();
		String facility = "select * from facility" ;
		ResultSet resultSet = null;
		
		try {
			resultSet = st.executeQuery(facility);
			/*
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
			*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
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
	
	int reserveFacility(String studentID,String facilityName,String buildingName,String dateForReservation,String startTime,String endTime)
	{
		Statement st = dbCommands.getStatement();
		String facility = "insert into RESERVATION values ('" + studentID +"',(select facilityID from facility where facilityName = '" + facilityName + "' and buildingName = '" + buildingName +"'),'" + dateForReservation + "','" + startTime +"','" + endTime +"')";
		String status = "select status,facilityID,capacity from facility where facilityName = '" + facilityName + "' and buildingName = '" + buildingName +"'";
		ResultSet resultSet;
		int count = 0;
		int currentCap;
		try {
			resultSet = st.executeQuery(status);
			resultSet.next();
			int cap = resultSet.getInt("capacity");
			if (!resultSet.getBoolean("status"))
			{
				return -1;
			}
			String facilityID = resultSet.getString("facilityID");
			String capacity = "select count(*) as currentCap from reservation where facilityID = '" + facilityID + "' and startTime = '" + startTime + "' and endTime = '" + endTime + "' and dateForReservation = '" + dateForReservation + "' ";
			resultSet = st.executeQuery(capacity);
			resultSet.next();
			currentCap = resultSet.getInt("currentCap");
			if (cap == currentCap)
			{
				return -2;
			} 
			count = st.executeUpdate(facility);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	int cancelReservation(String studentID,String facilityName,String buildingName,String dateForReservation,String startTime,String endTime)
	{
		Statement st = dbCommands.getStatement();
		String cancel ="delete from reservation where facilityID = (select facilityID from facility where buildingName = '" + buildingName +"' and facilityName = '" + facilityName +"') and studentID = '" + studentID + "' and startTime = '" + startTime + "' and endTime = '" + endTime + "' and dateForReservation = '" + dateForReservation + "'";
		int count = 0;
		
		try {
			count = st.executeUpdate(cancel);
			System.out.println("reservationCancelled");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	ResultSet viewReservations(String studentID)
	{
		Statement st = dbCommands.getStatement();
		String reservation = "select *,F.facilityName,F.buildingName from reservation join facility F on reservation.facilityID = F.facilityID where studentID = '" + studentID +"'";
		ResultSet resultSet = null;
		
		System.out.println("Facility Name\tBuilding Name\tDate of Reservation\t");
		try {
			resultSet = st.executeQuery(reservation);
			/*
			resultSet.next();
			String facilityName = resultSet.getString("facilityName");
			String buildingName = resultSet.getString("buildingName");
			String date = resultSet.getString("dateForReservation");
			String startTime = resultSet.getTime("startTime").toString();
			String endTime = resultSet.getTime("endTime").toString();
			System.out.println(facilityName + buildingName + date + startTime + endTime);
			*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}

	void giveFeedback (String feedback)
	{
		
	}
	
	//room admin
	void RequestRoomAllotment(String studentID)
	{
		
	}
	
	void RequestRoomChange(String studentID)
	{
		
	}
	
	void RequestForLeave(String studentID,String msg)
	{
		
	}
	
	void RequestForGuests(String msg,String dateOfJoining)
	{
		
	}
	
	//Facility admin
	void RequestNewfacility(String facilityName)
	{
		
	}
	
	void RequestMaintenanceOfFacility(String facilityName)
	{
		
	}

	void RequestRepairs(String facilityName)
	{
		
	}
	
	void RequestPestControl(String buildingName)
	{
		
	}
	
	//General admin
	void RequestDocuments(String studentID)
	{
		
	}
	/*Documents:-
	admission confirmation letter
	no-objection certificate (NOC)
	hostel leaving certificate.*/

	//Fee management admin
	void RequestDueDateExtension(String studentID, int Sem , String msg)
	{
		
	}
	void RequestRefund(String studentID ,int paymentID)
	{
		
	}

}
