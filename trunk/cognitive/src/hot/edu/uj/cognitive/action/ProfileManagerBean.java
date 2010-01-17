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

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.web.RequestParameter;
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
	
	private Publication publication;


	
	private String action = "EDIT";
	
	@RequestParameter
	public Integer publId;
	
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
	public List<Publication> getPublications() {
		//jak to wywalic z gettera?
	
			User user = (User) Contexts.getSessionContext().get("loggedUser");
			
			user = (User)this.em.createQuery("select u from User u where id=:id").setParameter("id", user.getId()).getSingleResult();
			
			this.userPublications = user.getPublications();
		
		
		return this.userPublications;
		
	}

	@Override
	public void setPublications(List<Publication> p) {
		this.userPublications = p;
		
	}

	private Publication validPubl(Integer pId){
		User user = (User) Contexts.getSessionContext().get("loggedUser");
		user = (User)this.em.createQuery("select u from User u where id=:id").setParameter("id", user.getId()).getSingleResult();
		this.userPublications = user.getPublications();
		boolean valid = false;
		Publication p = null;
		for(Publication pub: this.userPublications){
			System.out.println("PubID: "+pub.getId());
			if(pub.getId().equals(pId)){
				valid = true;
				p = pub;
				break;
			}
		}
		return p;
		
	}
	
	@Override
	public void editPublication() {
		action = "EDIT";
		Publication p = validPubl(this.publId);
		if(p!=null){
			this.publication = p;
			
		} else {
			this.message = "błędne ID";
		}
	}

	@Override
	public void removePublication() {
		Publication p = validPubl(this.publId);
		
		if(p!=null){
			//publikacja tego usera	
				this.userPublications.remove(p);
				System.out.println("publ.size(): "+this.userPublications.size());
				if(p.getUsers().size()==0)
					em.remove(p);
			
			this.message = "publikacja usunięta";
		} else {
			this.message = "niewłaściwe ID";
		}
		
	}

	@Override
	public Publication getPublication() {
		
		return this.publication;
	}

	@Override
	public void setPublication(Publication p) {
		this.publication = p;
		
	}

	@Override
	public String savePublication() {
		Publication p = null;
		if(action.equals("EDIT")){
			Publication tmp = validPubl(publication.getId());
			if(tmp!=null){
				//tmp to publikacja z bazy
				//nie mozna zapisac do bazy this.publication:
				//detached entity passed to persist
				
				tmp.setAuthors(publication.getAuthors());
				tmp.setTitle(publication.getTitle());
				tmp.setJournal(publication.getJournal());
				tmp.setKeywords(publication.getKeywords());
				tmp.setLink(publication.getLink());
				tmp.setPages(publication.getPages());
				tmp.setVolume(publication.getVolume());
				tmp.setReferences(publication.getReferences());
				tmp.setYear(publication.getYear());
				p = tmp;
				
			}
			System.out.println("action: EDIT");
		} else {
			p = this.publication;
		}
		if(p!=null){
			em.persist(p);
			if(action.equals("ADD")){
				//dodajemy userowi te publikacje
				User user = (User) Contexts.getSessionContext().get("loggedUser");
				user = (User)this.em.createQuery("select u from User u where id=:id").setParameter("id", user.getId()).getSingleResult();
				user.getPublications().add(p);
			}
			this.message = "zapisano";
			
		} else {
			this.message = "błędne ID";	
		}
		return "/profilemanager.xhtml";
	}

	@Override
	public void newPublication() {
		action = "ADD";
		this.publication = new Publication();
	}
	public String getAction(){
		return this.action;
	}
	public void setAction(String s){
		this.action = s;
	}


}
