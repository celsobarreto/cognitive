package edu.uj.cognitive.action;

import javax.ejb.Local;
import javax.mail.MessagingException;

import edu.uj.cognitive.model.User;

@Local
public interface Activation {
	public void activate();
	public void accept();
}
