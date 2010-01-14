package edu.uj.cognitive.action;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.contexts.Contexts;

import edu.uj.cognitive.model.User;

@Stateless
@Name("profileManager")
public class ProfileManagerBean implements ProfileManager {
	@PersistenceContext
	private EntityManager em;

	@RequestParameter
	private String token;
	@RequestParameter
	private String adres;

	private String newAddress;

	private String newAddressConfirm;

	private String message;

	@Remove
	public void destroy() {
	}


	private String makeMD5(String text){
		String hash = text;
	    MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(text.getBytes(),0,text.length());
			hash = new BigInteger(1,m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    
	    return hash; 
	}
	private boolean validateEmail(String mail) {
		if (mail == null)
			return false;

		if (mail.indexOf("@") < 0)
			return false;

		if (mail.indexOf(".") < 0)
			return false;

		StringTokenizer st = new StringTokenizer(mail, ".");
		String lastToken = null;
		while (st.hasMoreTokens()) {
			lastToken = st.nextToken();
		}

		if (lastToken.length() < 2) {

			return false;
		}

		return true;
	}

	@Override
	public String changeRequest() {
		this.message = null;
		System.out.println(this.newAddress);
		if (!this.newAddress.equals(this.newAddressConfirm))
			this.message = "emaile są różne";
		else if (!this.validateEmail(this.newAddress))
			this.message = "niewłaściwy email";
		else {
			// wysylanie maila z informacja
			// tworzony link do zmiany maila
			User user = (User) Contexts.getSessionContext().get("loggedUser");
			
			String token = this.makeMD5(this.adres+user.getPasswordHash());
			
			EmailSender mailer = new EmailSenderBean();
			mailer.setRecipient(this.newAddress);
			mailer.setSubject("Zmiana adresu e-mail");
			
			
			String body = "Chcesz zmienić swój adres mailowy w portalu cognitive\n";
			body += "Jeśli uznasz, że ta wiadomośc nie jest dla Ciebie to ją zignoruj\n";
			body += "Jesli chcesz zmienić adres e-mail to otwórz adres: \n";
			body += "http://cognitive_address/profileManager.seam?new="+this.newAddress+"&token="+token;
			
			//mailer.postMail();
			System.out.println(token);
			this.message = "dalsze informacje zostały wysłane na podany email";
			
		}

		return null;
	}

	@Override
	public String changePerform() {
		this.message = "";
		
		//liczymy ponownie hash
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		
		String token = this.makeMD5(this.adres+user.getPasswordHash());
		
		if(this.token.equals(token)){
			//poprawne zadanie
			user.setEmail(this.adres);
			em.persist(user);
			//zmieniamy usera w sesji, by odpowiadala stanowi aktualnemu
			Contexts.getSessionContext().set("loggedUser", user);
			this.message = "adres e-mail został zmieniony";
		}
		
		return null;
	}	
	
	@Override
	public String getNewAddress() {

		return this.newAddress;
	}

	@Override
	public String getNewAddressConfirm() {
		return this.newAddressConfirm;
	}

	@Override
	public void setNewAddress(String s) {
		this.newAddress = s;
	}

	@Override
	public void setNewAddressConfirm(String s) {
		this.newAddressConfirm = s;

	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public void setMessage(String s) {
		this.message = s;
	}

}
