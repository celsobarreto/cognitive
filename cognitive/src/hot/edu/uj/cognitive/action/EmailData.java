package edu.uj.cognitive.action;

import static org.jboss.seam.ScopeType.SESSION;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
@Scope(SESSION)
@Name("emailData")
public class EmailData {
	@NotNull
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	@NotNull
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@NotNull
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@NotNull
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String recipient; 
	public String subject; 
	public String message ; 
	public String from;
}
