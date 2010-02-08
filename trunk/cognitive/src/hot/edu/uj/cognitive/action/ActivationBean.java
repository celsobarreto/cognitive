package edu.uj.cognitive.action;

import java.util.Properties;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;

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
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.User;

@Stateful
@Name("activation")
@Scope(ScopeType.SESSION)
public class ActivationBean implements Activation{
	@RequestParameter 
	private Integer userId;
	@RequestParameter 
	private String userToken;
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@Override
	public void accept() {
		this.em.find(User.class, userId).setAccepted(true);		
	}

	@Override
	public void activate() {
		User u =this.em.find(User.class, userId);
		if(u.getActivationToken().equalsIgnoreCase(userToken))
		{	
			u.setEmailConfirmed(true);
			em.persist(u);
			try {
				sendAcceptationEmail(u);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				
			}
		}	
		
	}


	private void sendAcceptationEmail(User u) throws MessagingException {
		EmailSenderBean.sendMail("za3maj@gmail.com", "Activation Confirm link", getURL()+"/activation.seam&userId="+u.getId());
		
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
