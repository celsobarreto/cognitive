package edu.uj.cognitive.action;

import javax.ejb.Local;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

import org.jboss.seam.contexts.Contexts;

import edu.uj.cognitive.model.User;

@Local
public interface PasswordChanger {
	public void changePassword();
	public void destroy();
	public String getOldPassword();
	public void setOldPassword(String s);
	public String getNewPassword();
	public void setNewPassword(String s);
	public String getNewPasswordRepeated();
	public void setNewPasswordRepeated(String s);
	public void validateOldPasswordField(FacesContext context, UIComponent component, Object value);
}
