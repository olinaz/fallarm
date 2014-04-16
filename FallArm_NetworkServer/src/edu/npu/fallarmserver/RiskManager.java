package edu.npu.fallarmserver;

import edu.npu.fallarm.model.Acceleration;

public class RiskManager {
	public final static float CONST_GRAVITY = 9.8f;
	public float x,y,z;
	
	public static int detectFall(MessageParser msgParser){
		//inputs from msgParser:
		// - Acceleration: msgParser.getAcc()
		// - Orientation: msgParser.getOri()
		// - PatientID: msgParser.getPatientID()

		Acceleration acc = msgParser.getAcc();
		int vectorSum = acc.getVectorSum();
		//vectorSum = Math.round(vectorSum - CONST_GRAVITY);
		
		int riskLevel;
		if(vectorSum >= 9) {
			riskLevel = 1;
		}else if(vectorSum >= 7){
			riskLevel = 2;
		}else if(vectorSum >= 4){
			riskLevel = 3;
		}else if(vectorSum >= 1){
			riskLevel = 4;
		}else {
			riskLevel = 5;
		}
		return riskLevel;
	}

}
