package edu.uj.cognitive.action;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.jboss.seam.annotations.Name;

@Stateful
@Name("passwordValidator")
public class PasswordValidatorBean implements PasswordValidator {
	private String input1;
	private String input2;
	private boolean input1Set;

	public void validateField(FacesContext context, UIComponent component,
			Object value) {
		if (input1Set) {
			input2 = (String) value;
			if (input1 == null || input1.length() < 5)
			{
				((EditableValueHolder) component).setValid(false);
				context.addMessage(component.getClientId(context),
						new FacesMessage("  Hasło musi mieć co najmniej 5 znaków."));				
				
			} else if (!input1.equals(input2)) {
				((EditableValueHolder) component).setValid(false);
				context.addMessage(component.getClientId(context),
						new FacesMessage("  Hasło i hasło wpisane ponownie różnią się."));
			}
		} else {
			input1Set = true;
			input1 = (String) value;
		}
	}

	@Remove
	public void destroy() {

	}
}
