package GmailInbox;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.batch.item.ItemWriter;

public class EmailSaverWriter implements ItemWriter<Message>{


	private String cleanString (String str){
		return str.replaceAll(" ", "_").replaceAll("\\W+", "_");
	}
	@Override
	public void write(List<? extends Message> listMessage) throws Exception {
		// TODO Auto-generated method stub
		for (Message message: listMessage){
			String subject = message.getSubject();
			String cleanSubject = this.cleanString(subject);
			System.out.println(cleanSubject);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss_z");		
			String fileName = cleanString(dateFormat.format(message.getSentDate()).toString()
					+"_"+InternetAddress.toString(message.getFrom())
					+"_"+cleanSubject)+".eml";
			System.out.println("writing email:- " + fileName); 
			OutputStream out = new FileOutputStream(new File(fileName));
			try {
				message.writeTo(out);
			}
			finally {
				if (out != null) { out.flush(); out.close(); }
			} 
		}

	}

}
