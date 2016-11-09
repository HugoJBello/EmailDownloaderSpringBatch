package GmailInbox;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;


public class FetchInbox {
	private ArrayList<Message> listOfMessages = new ArrayList<Message>(); 

	public FetchInbox(int numberOfEmails, String smtpHost, String smtpPort, String socketFactoryPort, String emailAddress, String password, String folder) {
		Properties props = new Properties();
		try {
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.socketFactory.port", socketFactoryPort);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			Session session= Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", emailAddress,password);
			Folder inbox = store.getFolder(folder);
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();
			System.out.println("Total Messages:- " + messageCount);	 
			Message[] messages = inbox.getMessages();
			for (int i = 1; i < 10; i++) {
				System.out.println("Getting message " + inbox.getMessages()[messageCount-1].getSentDate().toString());
				Message message =  messages[messageCount-i];
				this.listOfMessages.add(i-1,message);
			}
			//inbox.close(true);
			//store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Message> getListOfMessages() {
		return listOfMessages;
	}
	public void setListOfMessages(ArrayList<Message> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

}

