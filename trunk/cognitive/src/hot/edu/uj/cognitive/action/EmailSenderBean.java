package edu.uj.cognitive.action;

import java.util.Properties;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.drools.lang.DRLParser.unary_constr_return;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

@Stateful
@Name("emailSender")
@Scope(ScopeType.SESSION)
//@Restrict("#{identity.loggedIn}")
public class EmailSenderBean implements EmailSender{
	public static String FROM =  "cognitiveportal@gmail.com";
	public static String HOST = "smtp.gmail.com";
	public static String USERNAME = "cognitiveportal";
	public static String PASSWORD = "cognitive1234";
	
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	private String recipient; 
	private String subject; 
	private String message ; 

	
	
	public void postMail( ) throws MessagingException
	{
		
		String recipients[] = {recipient}; 
	    boolean debug = false;

	     //Set the host smtp address
	     Properties props = new Properties();

	     props.put("mail.smtps.auth", "true");

	    // create some properties and get the default Session
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(debug);
	    Transport t = session.getTransport("smtps");
	    // create a message
	    Message msg = new MimeMessage(session);

	    // set the from and to address
	    
	    InternetAddress addressFrom = new InternetAddress(FROM);
	    msg.setFrom(addressFrom);

	    InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
	    for (int i = 0; i < recipients.length; i++)
	    {
	        addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   

	    // Optional : You can also set your custom headers in the Email if you Want
	    msg.addHeader("MyHeaderName", "myHeaderValue");

	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    msg.setContent(message, "text/plain");
	    try {
	    	t.connect(HOST, USERNAME, PASSWORD);
	    	t.sendMessage(msg, msg.getAllRecipients());
	    } 
	    finally {
	    	t.close();
	    }    
	    
	}
	
	@Remove
	public void destroy() 
	{
		this.recipient = null;
		this.subject = null;
		this.message = null;
		this.subject = null;
	}		
	/*
public static void main(String[] args) throws MessagingException {
	EmailData emailData = new EmailData();
	emailData.from = "za3maj@gmail.com";
	emailData.recipient = "za3maj@gmail.com";
	emailData.subject = "za3maj@gmail.com";
	emailData.message = "za3maj@gmail.com";
	EmailSenderBean esb= new EmailSenderBean();
	esb.emailData = emailData;
	esb.postMail();
}*/
}
