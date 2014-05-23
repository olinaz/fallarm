package edu.npu.fallarmserver;

import java.net.*;
import java.io.*;

import javax.mail.MessagingException;

public class FallArmServer_MultiClient extends Thread {
	public final static int defaultPort = 9111;
	ServerSocket theServer;
	static int num_threads = 10;

	private final static int RISK_THRESHOLD = 3;
	private MessageParser msgParser;
	
	public static void main(String[] args) {
		int port = defaultPort;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = defaultPort;
		}
		if (port <= 0 || port >= 65536)
			port = defaultPort;
		try {
			ServerSocket ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			System.out.println("Server Socket Start!!");
			for (int i = 0; i < num_threads; i++) {
				System.out.println("Create num_threads " + i + " Port:" + port);
				FallArmServer_MultiClient pes = new FallArmServer_MultiClient(ss);
				pes.start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	FallArmServer_MultiClient(ServerSocket ss){ 
		theServer = ss;
	}

	public void run() {
		Socket connection = null;
		while (true) {
			try {
				DataOutputStream output;
				DataInputStream input;
				connection = theServer.accept();
				System.out.println( "Connection received from: " +
			               connection.getInetAddress().getHostName() );

				input = new DataInputStream(connection.getInputStream());
				output = new DataOutputStream(connection.getOutputStream());

				System.out.println("Client Connected and Start get I/O!!");
				//while (true) {
				String inData = input.readUTF();
					System.out.println("==> Input from Client: " + inData);
					msgParser = new MessageParser(inData);
					if (msgParser.parseMsgFromClient()) {

						int pid = msgParser.getPatientID();
						System.out.println("\nPID: " + pid);
						int riskLevel = RiskManager.detectFall(msgParser);
						msgParser.setRiskLevel(riskLevel);
						System.out.println("\nRiskLevel: " + riskLevel);
						// II: send mail alert and log the event if risklevel to
						// high
						if (riskLevel >= RISK_THRESHOLD) {
							EmailSender.sendAlertMailWithRiskLevel(pid,riskLevel);
							EventLogger.logEvent(msgParser);
						}
						// III: feedback the risklevel to device
						String outData = new String("" + riskLevel);
						output.writeUTF(outData);
					}
				//} // end while
				connection.close();	
			} // end try
			catch (Exception e) {
			}
			
		} // end while
	} // end run

}