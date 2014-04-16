package edu.npu.fallarmserver;

import edu.npu.fallarm.model.Acceleration;
import edu.npu.fallarm.model.Orientation;

public class MessageParser {
	// Seq. of msg from device
	private static final int INDEX_PID = 0;
	private static final int INDEX_ACC = 1;
	private static final int INDEX_ORI = 2;
	// Seq. of msg to device
	private static final int INDEX_RISK = 0;

	private String[] msgArr;
	private Acceleration acc;
	private Orientation ori;
	private int patientID;
	private int riskLevel;

	public Acceleration getAcc() {
		return acc;
	}

	public Orientation getOri() {
		return ori;
	}

	public int getPatientID() {
		return patientID;
	}

	public int getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(int riskLevel) {
		this.riskLevel = riskLevel;
	}

	public MessageParser(String msg) {
		this.msgArr = msg.split(";");
	}

	public boolean parseMsgFromClient() {
		int len = msgArr.length;
		if (len != 3)
			return false;

		//System.out.println("length of client-msg:" + len);
		return parsePatientID() && parseAcc()
				&& parseOri();
	}

	public String getSensorData() {
		return acc + ";" + ori;
	}

	public boolean parseAcc() {
		float x, y, z;

		String accStr = msgArr[INDEX_ACC];
		int spliter = accStr.indexOf(':');
		accStr = msgArr[INDEX_ACC].substring(spliter + 1);
		String[] values = accStr.split(",");
		try {
			x = Float.parseFloat(values[0]);
			y = Float.parseFloat(values[1]);
			z = Float.parseFloat(values[2]);
		} catch (Exception ex) {
			return false;
		}

		acc = new Acceleration(x, y, z);
		return true;
	}

	public boolean parseOri() {
		float x, y, z;

		String oriStr = msgArr[INDEX_ORI];
		int spliter = oriStr.indexOf(':');
		oriStr = msgArr[INDEX_ORI].substring(spliter + 1);
		String[] values = oriStr.split(",");
		try {
			x = Float.parseFloat(values[0]);
			y = Float.parseFloat(values[1]);
			z = Float.parseFloat(values[2]);
		} catch (Exception ex) {
			return false;
		}

		ori = new Orientation(x, y, z);
		return true;
	}

	public boolean parsePatientID() {

		String pidStr = msgArr[INDEX_PID];
		int spliter = pidStr.indexOf(':');
		pidStr = msgArr[INDEX_PID].substring(spliter + 1);
		try {
			patientID = Integer.parseInt(pidStr);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public boolean parseRiskLevel() {
		String riskStr = msgArr[INDEX_RISK];
		int spliter = riskStr.indexOf(':');
		riskStr = msgArr[INDEX_RISK].substring(spliter + 1);
		try {
			riskLevel = Integer.parseInt(riskStr);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
