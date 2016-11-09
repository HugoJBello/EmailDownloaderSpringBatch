
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class GmailInbox {

 public static void main(String[] args) {
  GmailInbox gmail = new GmailInbox();
  gmail.read();
 }
 
 private String getTextFromMessage(Message message) throws Exception {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws Exception{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
 
 private String cleanString (String str){
	 return str.replaceAll(" ", "_").replaceAll("\\W+", "");
 }
 public void read() {
  Properties props = new Properties();
  try {
      props.load(new FileInputStream(new File("/home/hugo/workspace/GmailInboxJavamailMaven/stpm.properties")));
//	  props.put("mail.smtp.host", "smtp.gmail.com");
//      props.put("mail.smtp.port", "465");
//      props.put("mail.smtp.socketFactory.port", "465");
//      props.put("mail.smtp.auth", "true");
//      props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
   Session session= Session.getDefaultInstance(props, null);

   Store store = session.getStore("imaps");
   store.connect("smtp.gmail.com", "cagateperro@gmail.com","3141592625");

   Folder inbox = store.getFolder("inbox");
   inbox.open(Folder.READ_ONLY);
   int messageCount = inbox.getMessageCount();

   System.out.println("Total Messages:- " + messageCount);

   Message[] messages = inbox.getMessages();
   System.out.println("------------------------------");
   for (int i = 1; i < 10; i++) {
	   Message message =  messages[messageCount-i];
	   String subject = message.getSubject();
	   String cleanSubject = this.cleanString(subject);
	   System.out.println(cleanSubject);
	   DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss_z");		
	   String fileName = cleanString(dateFormat.format(message.getSentDate()).toString()+message.getFrom()[0].toString());
	   System.out.println(fileName);
       System.out.println("Mail Subject:- " + message.getSubject() 
       +"\n"+ messages[messageCount-i].getContent().toString()); 
	   PrintWriter out = new PrintWriter(fileName+".html");
	   out.println(getTextFromMessage(message));
	   out.close();
   }
   inbox.close(true);
   store.close();

  } catch (Exception e) {
   e.printStackTrace();
  }
 }

}