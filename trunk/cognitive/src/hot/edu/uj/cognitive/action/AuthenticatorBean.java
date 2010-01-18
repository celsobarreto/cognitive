package edu.uj.cognitive.action;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.digest.DigestUtils;

import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator
{
    @Logger private Log log;

	@PersistenceContext
    EntityManager entityManager;
	
    @In Credentials credentials;
    @In Identity identity;
    
    @In private FacesContext facesContext;  
    
    public boolean authenticate()
    {
    	 try {
             User user = (User) entityManager.createQuery(
                "from User where email = :username and passwordHash = :password")
                .setParameter("username", credentials.getUsername())
                .setParameter("password", DigestUtils.md5Hex(credentials.getPassword()))
                .getSingleResult();
             
             if (user.getRoles() != null) {
                for (Role mr : user.getRoles())
                   identity.addRole(mr.getName());
             }
             
             if (!user.getEmailConfirmed()) {
            	 facesContext.addMessage(null, new FacesMessage("Konto nie jest aktywne. Musisz potwierdzić adres email otwierają przesłany link."));				 
            	 return false;
             }
  
             if (!user.getAccepted()) {
            	 facesContext.addMessage(null, new FacesMessage("Konto nie jest aktywne. Oczekuje na akceptację przed administratora."));				 
            	 return false;
             }             
             
             String ip = getIP();
             log.info("IP = "+ip);
             if (!hasAllowedIP(user, ip)) {
            	 log.info("Adres IP tego komputera ("+ip+") nie znajduje się wśród dopuszczonych ("+user.getAllowedIPs()+")");
            	 return false;
             }

             log.info("Uzytkownik zalogowany.");
             Contexts.getSessionContext().set("loggedUser", user);             
             
             return true;

          }
          catch (NoResultException ex) {

             return false;

          }    	
    }
    
    private boolean hasAllowedIP(User user, String ip) {
    	String allowedIPs = user.getAllowedIPs();
    	if (allowedIPs == null) {
    		log.info("Uzytkownik nie ma ustalonych dopuszczalnych IP logowania. Nie sprawdzam IP.");
    		return true;
    	}
    	
    	String[] allAllowed = allowedIPs.split(",");
    	for(String allowed: allAllowed) {
    		if (allowed.equals(ip)) {
    			log.info("IP się zgadza.");
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private String getIP() {
    	return ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRemoteAddr();    	
    }

}
