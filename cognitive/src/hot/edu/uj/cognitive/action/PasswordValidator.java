package edu.uj.cognitive.action;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@Local
public interface PasswordValidator {

	public abstract void validateField(FacesContext context,
			UIComponent component, Object value);

	@Remove
	public abstract void destroy();

}