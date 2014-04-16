package edu.npu.fallarmserver;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.smtp.SMTPSSLTransport;

public class EmailSender {
	private static String from = "fallarm_system@gmail.com";
	private static String to = "fallarm_system@gmail.com";
	private static String host = "smtp.gmail.com";
	private static String userid = "fallarm.sys";
	private static String password = "fallarmsys";
	
	public static void sendAlertMailWithRiskLevel(int pid, int riskLevel) 
			throws MessagingException {
		String subject = "FallArm Alert for Patient ID :" + pid; 
		String text = "FallArm Alert Critical Level - " + riskLevel; 

		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.setProperty("mail.transport.protocol", "smtps");
		props.put("mail.smtp.user", userid);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtps.auth", "true");
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;

		try {
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
		} catch (AddressException e) {
			e.printStackTrace();
		}
		message.setFrom(fromAddress);
		message.setRecipient(RecipientType.TO, toAddress);
		message.setSubject(subject);
		message.setText(text);

		Transport transport = session.getTransport("smtps");
		transport.connect(host, userid, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}
