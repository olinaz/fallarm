package edu.npu.fallarmserver;

import edu.npu.fallarm.dao.AdverseEventDAO;
import edu.npu.fallarm.daojdbcimpl.AdverseEventDAOjdbc;

public class EventLogger {
	
	public static void logEvent(MessageParser msgParser){
		AdverseEventDAO eventDao= new AdverseEventDAOjdbc();
		//log the risky event into database: pid, 6# sensor data, sysdate
		int pid = msgParser.getPatientID();
		String sensorData = msgParser.getSensorData();
		System.out.println("========EventLogger:");
		System.out.println("PID= " + pid + "\t\tSensorData= " + sensorData);
		
		eventDao.createAdverseEvent(msgParser);
	}

}
