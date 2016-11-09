package GmailInbox;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.batch.item.ItemReader;

public class EmailReader implements ItemReader<Message>  {
	private ArrayList<Message> listOfMessages = null; 
	private int nextMessageIndex;
	private int numberOfEmails;
	private String smtpHost;
	private String smtpPort = "465";
	private String socketFactoryPort="465";
	private String emailAddress;
	private String password;
	private String folder;
	
	public Message read() throws MessagingException{
		System.out.println("------------------------");
		if (emailsNotFetch()) {
			System.out.println("Downloading emails from " + emailAddress);

			listOfMessages = fetchEmails();
			nextMessageIndex = 0;
		}
 
		Message nextMessage = null; 
		if (nextMessageIndex < listOfMessages.size()) {
			nextMessage = listOfMessages.get(nextMessageIndex);
            nextMessageIndex++;
        }
 		return nextMessage;
	}
	
	private boolean emailsNotFetch() {
          return this.listOfMessages == null;
    }
	 private ArrayList<Message> fetchEmails() {
		 FetchInbox fetchInvox = new FetchInbox( numberOfEmails,  smtpHost,  smtpPort,  socketFactoryPort,  emailAddress,  password, folder);
		 return fetchInvox.getListOfMessages();
	 }

	public int getNumberOfEmails() {
		return numberOfEmails;
	}

	public void setNumberOfEmails(int numberOfEmails) {
		this.numberOfEmails = numberOfEmails;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSocketFactoryPort() {
		return socketFactoryPort;
	}

	public void setSocketFactoryPort(String socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	 
}
