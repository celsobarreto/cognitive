package edu.uj.cognitive.action;

import javax.ejb.Local;
import javax.mail.MessagingException;

import edu.uj.cognitive.model.User;

@Local
public interface EmailSender {
	public void postMail( ) throws MessagingException;
	public void updateRecipientAddress( );
	public void setRecipient(String v);
	public void setSubject(String v);
	public void setMessage(String v);
	public String getRecipient();
	public String getSubject();
	public String getMessage();
	public void destroy();
}
