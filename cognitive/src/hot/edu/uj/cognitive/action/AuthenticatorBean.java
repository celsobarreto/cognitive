package edu.uj.cognitive.action;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

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
             
             if (!user.getAccepted() || !user.getActivated()) {
            	 log.info("User account is not enabled. Cannot login.");
            	 return false;
             }

             Contexts.getSessionContext().set("loggedUser", user);             
             
             return true;

          }

          catch (NoResultException ex) {

             return false;

          }    	
    }

}
