package edu.npu.fallarm.daojdbcimpl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.npu.fallarm.dao.AdverseEventDAO;
import edu.npu.fallarmserver.MessageParser;

public class AdverseEventDAOjdbc implements AdverseEventDAO {

	private Connection dbConn;
	private String dbSourceURL = new String("jdbc:mysql://localhost/cs595_project");
	
	/*public AdverseEventDAOjdbc(){
		dbSourceURL = new String("jdbc:mysql://localhost/cs595_project");
		try {
			// Load the JDBC driver
			String driver = "com.mysql.jdbc.Driver";
			Class driver_class = Class.forName(driver);
			Driver dbDriver = (Driver) driver_class.newInstance();
			DriverManager.registerDriver(dbDriver);

			System.out.println("----JDBC driver registered");
		} catch (Exception ex) {
			System.out.println("----Error in loading JDBC driver:" + ex);
			System.exit(1);
		}
	}*/
	
	@Override
	public void createAdverseEvent(MessageParser msg) {
		int count =0;
		try {// connect to mySQL
			dbConn = DriverManager.getConnection(dbSourceURL, "root", "");

			String insertStr = "insert into adverse_event (time,patientid, acc, ori,risklevel) values(now(), ?,?,?,?)";
			PreparedStatement insertStmt = dbConn.prepareStatement(insertStr);
			insertStmt.setInt(1, msg.getPatientID());
			insertStmt.setString(2, msg.getAcc().toString());
			insertStmt.setString(3, msg.getOri().toString());
			insertStmt.setInt(4, msg.getRiskLevel());

			insertStmt.executeUpdate();
			System.out.println("adverse event inserted: ");
			insertStmt.close();
			
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
