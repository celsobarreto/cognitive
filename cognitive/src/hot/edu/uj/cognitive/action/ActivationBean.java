package edu.uj.cognitive.action;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateful
@Name("activation")
@Scope(ScopeType.SESSION)
public class ActivationBean implements Activation{
	@RequestParameter 
	private Integer userId;
	@RequestParameter 
	private String activationToken;
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@Override
	public void accept() {
		User user = (User) Contexts.getSessionContext().get("loggedUser");
			for(Role role : user.getRoles())//sprawdzamy, czy jest adminem
				if(Role.ADMIN_ROLE.equals(role.getName())){
					this.em.find(User.class, userId).setAccepted(true);		
					return;
				}
	}

	@Override
	public void activate() {
		User u =em.find(User.class, userId);
		if(u.getActivationToken().equals(activationToken))
		{	
			u.setEmailConfirmed(true);
			em.persist(u);
			try {
				javax.persistence.Query c = em.createQuery("select u " +
						"from User u, IN(u.roles) r " +
						"where r.name = :ruleName");
				c.setParameter("ruleName", Role.ADMIN_ROLE);
				for(User admin : (List<User>)c.getResultList())
					sendAcceptationEmail(admin);
			} catch (MessagingException e) {
				log.error(e);
				
			}
		}	
		
	}


	private void sendAcceptationEmail(User u) throws MessagingException {
		String message = 
			"W celu potwierdzenia aktywacji konta użytkownika "+u.getFullName()+" otwórz kolejno następujące strony:\n"+
			getURL()+"/activation.seam\n"+
			getURL()+"/activation.seam?userId="+u.getId()+"&actionMethod=activation.xhtml:activation.accept()";
		
		EmailSenderBean.sendMail(u.getEmail(), "Akceptacja konta użytkownika", message);
		
	}
	@Logger
	private Log log;
	@In
	private FacesContext facesContext;
	private String getURL(){

		HttpServletRequest request= (HttpServletRequest)facesContext.getExternalContext().getRequest();
		return request.getScheme()+ "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}
	@Remove
	public void destroy() {
	}

}
