package edu.npu.fallarmserver;

/*
 ------------------------------------------------
 Connection 1 received from: localhost
 Got I/O streams
 Sending message "Connection successful"
 Client message: Thank you.
 Transmission complete. Closing socket.
 ------------------------------------------------
 // Fig. 16.3: Server.java
 // Set up a Server that will receive a connection
 // from a client, send a string to the client,
 // and close the connection.
 */

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.mail.MessagingException;

class CloseWindowAndExit extends WindowAdapter {
	   public void windowClosing( WindowEvent e )
	   {
	      System.exit( 0 );
	   }
	}

public class FallArmServer_SingleClient extends Frame {
	private TextArea display;
	private final static int RISK_THRESHOLD = 3;

	private MessageParser msgParser;

	public FallArmServer_SingleClient() {
		super("SocketServer");
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(display, BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	public void runServer() {
		ServerSocket server;
		Socket connection;
		DataOutputStream output;
		DataInputStream input;
		int counter = 1;

		try {
			// Step 1: Create a ServerSocket.
			server = new ServerSocket(9111, 100);

			while (true) {
				// Step 2: Wait for a connection.
				connection = server.accept();

				display.append("Connection " + counter + " received from: "
						+ connection.getInetAddress().getHostName());

				// Step 3: Get input and output streams.
				input = new DataInputStream(connection.getInputStream());
				output = new DataOutputStream(connection.getOutputStream());

				// Step 4: receive data from device and process data then
				// feedback
				String inData = input.readUTF();
				display.append("\nClient message: " + inData);
				// I: analysize the data and calculate the risklevel
				msgParser = new MessageParser(inData);
				if (msgParser.parseMsgFromClient()) {

					int pid = msgParser.getPatientID();
					display.append("\nPID: " + pid);
					int riskLevel = RiskManager.detectFall(msgParser);
					msgParser.setRiskLevel(riskLevel);
					display.append("\nRiskLevel: " + riskLevel);
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

				// Step 5: Close connection.
				display.append("\nTransmission complete. Closing socket.\n\n");
				connection.close();
				++counter;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String args[]) {
		FallArmServer_SingleClient s = new FallArmServer_SingleClient();

		s.addWindowListener(new CloseWindowAndExit());
		s.runServer();
	}
}
