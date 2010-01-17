package edu.uj.cognitive.action;

import java.util.Properties;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.drools.lang.DRLParser.unary_constr_return;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.User;
import org.jboss.seam.log.Log;

@Stateful
@Name("passwordChanger")
@Scope(ScopeType.SESSION)
@Restrict("#{identity.loggedIn}")
public class PasswordChangerBean implements PasswordChanger {

    @Logger private Log log;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	


	private String oldPassword; 
	private String newPassword; 
	private String newPasswordRepeated;

    @In private FacesContext facesContext;  	

	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	

	public String getNewPasswordRepeated() {
		return newPasswordRepeated;
	}
	public void setNewPasswordRepeated(String newPasswordRepeated) {
		this.newPasswordRepeated = newPasswordRepeated;
	}
	

	public void changePassword() {
		log.info("Got old password #0 and new #1, repated #2", oldPassword, newPassword, newPasswordRepeated);
		
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		assert user.hasPassword(oldPassword);
		
		user = em.merge(user);
		user.setPassword(newPassword);
		
		facesContext.addMessage(null, new FacesMessage("Hasło zostało zmienione."));
		
	}
	
	public void validateOldPasswordField(FacesContext context, UIComponent component, Object value) {
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		if (!user.hasPassword((String) value)) {
			((EditableValueHolder) component).setValid(false);
				context.addMessage(component.getClientId(context),
					new FacesMessage("Nieprawidłowe stare hasło."));
		}
	}
	
	
	
	@Remove
	public void destroy() 
	{
		
	}		

	

}
