package edu.uj.cognitive.action;

import javax.ejb.Local;
import javax.mail.MessagingException;

@Local
public interface EmailSender {
	public void postMail( ) throws MessagingException;

}
