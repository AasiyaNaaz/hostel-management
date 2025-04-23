package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeManagement {
	
	int generateStudentFee(String studentID,String acadyr,int totalFee,String dueDate)
	{

		Statement st = dbCommands.getStatement();
		Statement st2 = dbCommands.getStatement();
		String category = "Select category from student where ID = '" + studentID +"'";
		ResultSet resultSet;
		int count = 0;
		
		try {
			resultSet = st2.executeQuery(category);
			resultSet.next();
			char cat = resultSet.getString("category").charAt(0);
					
			if (cat == 'B')
			{
				totalFee = (int)(0.8 * totalFee);
			}
			if (cat == 'C')
			{
				totalFee = (int)(0.6 * totalFee);
			}
			String generateFee = "insert into FEEMANAGEMENT (studentID,academicYear,totalFee,dueDate,paymentStatus) VALUES ('" + studentID + "','" + acadyr + "'," + totalFee + ",'" + dueDate + "'," + ",0)";
			count = st.executeUpdate(generateFee);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	int generateStudentFee(String acadyr,int totalFee,String dueDate)
	{
		Statement st = dbCommands.getStatement();
		Statement st2 = dbCommands.getStatement();
		Statement st3 = dbCommands.getStatement();
		String student = "SELECT ID FROM STUDENT WHERE roomID IS NOT NULL";
		ResultSet resultSet;
		int count = 0;
		List<String> studentIDs = new ArrayList<>();
		
		try {
			resultSet = st.executeQuery(student);
			while (resultSet.next()) 
			{
				studentIDs.add(resultSet.getString("ID"));
			}
			int totalFeedub = totalFee;
			for (String studentID : studentIDs)
			{
				String category = "Select category from student where ID = '" + studentID +"'";
				resultSet = st3.executeQuery(category);
				resultSet.next();
				char cat = resultSet.getString("category").charAt(0);
						
				if (cat == 'B')
				{
					totalFeedub = (int)(0.8 * totalFee);
				}
				if (cat == 'C')
				{
					totalFeedub = (int)(0.6 * totalFee);
				}
				String generateFee = "insert into FEEMANAGEMENT (studentID,academicYear,totalFee,paymentStatus,dueDate) VALUES ('" + studentID + "','" + acadyr + "'," + totalFeedub + ",0,'" + dueDate + "')";
				count += st2.executeUpdate(generateFee);
				totalFeedub = totalFee;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int updateStudentFee(String acadyr,int newAmount)
	{
		Statement st = dbCommands.getStatement();
		Statement st2 = dbCommands.getStatement();
		Statement st3 = dbCommands.getStatement();
		String fee = "update feemanagement SET totalFee = " + newAmount + " where academicYear = '" + acadyr + "'";
		ResultSet resultSet;
		
		int count = 0;
		
		try {
			String category = "Select ID from student where category = 'B'";
			resultSet = st3.executeQuery(category);
			resultSet.next();
			String studentID = resultSet.getString("ID");
			String feeB = "update feemanagement SET totalFee = " + ((int)(0.8 * newAmount)) + " where academicYear = '" + acadyr + "' and studentID = '" + studentID + "'";
			
			String category2 = "Select ID from student where category = 'B'";
			resultSet = st2.executeQuery(category2);
			resultSet.next();
			studentID = resultSet.getString("ID");
			String feeC = "update feemanagement SET totalFee = " + ((int)(0.8 * newAmount)) + " where academicYear = '" + acadyr + "' and studentID = '" + studentID + "'";
			
			count = st.executeUpdate(fee) + st.executeUpdate(feeB) + st.executeUpdate(feeC) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int changeCat(String studentID, char cat)
	{
		Statement st = dbCommands.getStatement();
		String fee = "update student SET category = '" + cat + "' where ID = '" + studentID + "'";
		int count = 0;
		
		try {
			count = st.executeUpdate(fee);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int processRefund(String studentID)
	{
		//call method to send notice.
		return 1;
	}
	
	int deleteAcadFee(String acadyr)
	{
		Statement st = dbCommands.getStatement();
		String fee = "delete from feemanagement where academicYear = '" + acadyr + "'";
		int count = 0;
		
		try {
			count = st.executeUpdate(fee);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	int changeDueDate(String acadyr,String newDueDate)
	{
		Statement st = dbCommands.getStatement();
		String fee = "update feemanagement set dueDate = '" + newDueDate + "' where academicYear = '" + acadyr + "'";
		int count = 0;
		
		try {
			count = st.executeUpdate(fee);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	//for just one student
	int changeDueDate(String studentID,String acadyr,String newDueDate)
	{
		Statement st = dbCommands.getStatement();
		String fee = "update feemanagement set dueDate = '" + newDueDate + "' where academicYear = '" + acadyr + "' and studentID = '" + studentID + "'";
		int count = 0;
		
		try {
			count = st.executeUpdate(fee);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	void viewStudentFeeRecords(String studentID)
	{
		Statement st=dbCommands.getStatement();
		String student = "select firstName,lastName from Student where ID = '" + studentID + "'";
		String fee = "select feeID,academicYear, academicYear,totalFee,dueDate, paymentStatus from FEEMANAGEMENT WHERE studentID = '" + studentID + "'";
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(student);
			resultSet.next();
			String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
			System.out.println("Fee ID\tStudent ID\tName\t\tAcademic Year\tTotal Fee\tDue Date\tPayment Status");
			resultSet = st.executeQuery(fee);
			while (resultSet.next())
			{
				String status = resultSet.getInt("paymentStatus") == 1 ? "Paid" : "Not Paid";
				System.out.println(resultSet.getInt("feeID") + "\t" + studentID + "\t" + name + "\t" + resultSet.getString("academicYear") + "\t" + resultSet.getString("totalFee") + "\t\t" + resultSet.getDate("dueDate") + "\t" + status );			
			} 
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewStudentFeeRecords()
	{
		Statement st=dbCommands.getStatement();
		String student = "select ID, firstName,lastName,F.feeID,F.academicYear, F.totalFee, F.dueDate from STUDENT JOIN FEEMANAGEMENT F ON STUDENT.ID = F.studentID ";
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(student);
			System.out.println("Fee ID\tStudent ID\tName\t\t\tAcademic Year\tTotal Fee\tDue Date");
			while (resultSet.next())
			{
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				System.out.println(resultSet.getInt("feeID") + "\t" + resultSet.getString("ID") + "\t" + name + "\t" + resultSet.getString("academicYear") + "\t" + resultSet.getString("totalFee") + "\t\t" + resultSet.getDate("dueDate") );			
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void viewAllPendingDueStudents()
	{
		Statement st=dbCommands.getStatement();
		String student = "select ID, firstName,lastName,F.feeID,F.academicYear, F.totalFee, F.dueDate from STUDENT JOIN FEEMANAGEMENT F ON STUDENT.ID = F.studentID where paymentStatus = 0";
		ResultSet resultSet;
		
		try {
			resultSet = st.executeQuery(student);
			System.out.println("Fee ID\tStudent ID\tName\t\t\tAcademic Year\tTotal Fee\tDue Date");
			while (resultSet.next())
			{
				String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
				System.out.println(resultSet.getInt("feeID") + "\t" + resultSet.getString("ID") + "\t" + name + "\t" + resultSet.getString("academicYear") + "\t" + resultSet.getString("totalFee") + "\t\t" + resultSet.getDate("dueDate") );			
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	ResultSet viewAcadyrs()
	{
		Statement st=dbCommands.getStatement();
		String student = "SELECT DISTINCT academicYear FROM feemanagement";
		ResultSet resultSet = null;
		
		try {
			resultSet = st.executeQuery(student);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
}
