package edu.uj.cognitive.action;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;

import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.User;

@Stateless
@Name("profileManager")
public class ProfileManagerBean implements ProfileManager {
	@PersistenceContext
	private EntityManager em;


	private String token;
	private String newAddress;

	private String newAddressConfirm;

	private String message;
	
	private List<Publication> userPublications;
	

	@Remove
	public void destroy() {
	}

	private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
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
		this.message = "";
		System.out.println(this.newAddress);
		if (!this.newAddress.equals(this.newAddressConfirm))
			this.message = "emaile są różne";
		else if (!this.validateEmail(this.newAddress))
			this.message = "niewłaściwy email";
		else {
			// wysylanie maila z informacja
			// tworzony link do zmiany maila
			User user = (User) Contexts.getSessionContext().get("loggedUser");
			
			String token = this.makeMD5(this.newAddress+user.getPasswordHash()+"jakisTekst"+this.getCurrentDate());
			
			EmailSender mailer = new EmailSenderBean();
			mailer.setRecipient(this.newAddress);
			mailer.setSubject("Zmiana adresu e-mail");
			
			
			String body = "Chcesz zmienić swój adres mailowy w portalu cognitive\n";
			body += "Jeśli uznasz, że ta wiadomośc nie jest dla Ciebie to ją zignoruj\n";
			body += "Jesli chcesz zmienić adres e-mail to przejdź do kroku drugiego w swoim profilu.\n";
			body += "Uzupełnij tam wymagane pola. Twój token to: "+token;
			
			//mailer.postMail();
			System.out.println("Token: "+token);
			this.message = "dalsze informacje zostały wysłane na podany email";
			
		}

		return null;
	}

	@Override
	public String changePerform() {
		this.message = "błędne dane";
		
		//liczymy ponownie hash
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		
		String token = this.makeMD5(this.newAddress+user.getPasswordHash()+"jakisTekst"+this.getCurrentDate());
		
		if(this.token.equals(token)){
			//poprawne zadanie
			user = em.merge(user);
			user.setEmail(this.newAddress);
			
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
	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public void setToken(String s) {
		this.token = s;
	}

	@Override
	public List<Publication> getUserPublications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserPublications(List<Publication> p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editPublication() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePublication() {
		// TODO Auto-generated method stub
		
	}
}
