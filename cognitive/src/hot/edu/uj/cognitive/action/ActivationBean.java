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
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateful
@Name("activation")
@Scope(ScopeType.SESSION)
public class ActivationBean implements Activation{
	@In
	StatusMessages statusMessages;
	@RequestParameter 
	private Integer userId;
	@RequestParameter 
	private String activationToken;
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	@Override
	public void accept() {
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		if(user !=null&&userId!=null)	
			for(Role role : user.getRoles())//sprawdzamy, czy jest adminem
				if(Role.ADMIN_ROLE.equals(role.getName())){
					User u = this.em.find(User.class, userId);
					u.setAccepted(true);	
					statusMessages.add("Konto uzytkownika "+u.getFullName() + " zostalo aktywowane.");
					return;
				}
		statusMessages.add("Aby aktywowac uzytkownika musisz byc zalogowany na koncie administratora");
	}

	@Override
	public void activate() {
		if(userId ==null) return;
		User u =em.find(User.class, userId);
		if(u ==null || activationToken ==null || !u.getActivationToken().equals(activationToken)) return;
		u.setEmailConfirmed(true);
		em.persist(u);
		try {
				javax.persistence.Query c = em.createQuery("select u " +
						"from User u, IN(u.roles) r " +
						"where r.name = :ruleName");
				c.setParameter("ruleName", Role.ADMIN_ROLE);
				for(User admin : (List<User>)c.getResultList())
					sendAcceptationEmail(admin);
				statusMessages.add("Twoj adresu e-mail zostal potwierdzony, konto oczekuje na akceptacje administratora serwisu");
		} catch (MessagingException e) {
				statusMessages.add("Wystpil nieoczekiwany blad sprobuj ponownie pozniej");
				log.error(e);
				
		}
	}


	private void sendAcceptationEmail(User u) throws MessagingException {
		String message = 
			"W celu potwierdzenia aktywacji konta uzytkownika "+u.getFullName()+" otworz nastepujaca strone:\n"+
			getURL()+"/acceptation.seam?userId="+u.getId();
		
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
