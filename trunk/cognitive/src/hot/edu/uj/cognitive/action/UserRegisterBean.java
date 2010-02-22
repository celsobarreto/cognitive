package edu.uj.cognitive.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.digest.DigestUtils;

import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateful
@Name("UserRegister")
public class UserRegisterBean implements UserRegister {
	@Logger
	private Log log;

	@In
	StatusMessages statusMessages;

	@PersistenceContext
	private EntityManager em;

	// private String value;

	private String fullName;

	private String email;
	private String competences;
	private String allowedIPs;

	private String password;
	private String confirmPassword;

	private Boolean isEntrepreneur;
	private Boolean isScientist;
	
	private final boolean REQUIRE_EMAIL_CONFIRMATION = true;
	private final boolean REQUIRE_ADMIN_ACCEPTATION = true;

	private ArrayList<Role> roles = new ArrayList<Role>();
	
	@In
	private FacesContext facesContext;

	public void userRegister() {
		roles.clear();

		
		System.out.println(" userRegister ");
		
		if (isScientist) {
			roles.add(getRole(Role.SCIENTIST_ROLE));
		} 
		
		if (isEntrepreneur) {
			roles.add(getRole(Role.ENTREPRENEUR_ROLE));
		} 
		
		if (roles.isEmpty()) {
			statusMessages.add("Wybierz grupe do ktorej chcesz nalezec");
			
		}
		else {

			if (fullName.equals(null) || fullName == "") {
				statusMessages.add("Podaj Tytul Imie i Nazwisko");
			} 
			else if (email.equals(null) || email == "") {
				statusMessages.add("Podaj E-mail");
			}
			else if(password.equals(null) || password == "")
			{
				statusMessages.add("Podaj haslo");
			}
			else if (password.equals(confirmPassword) == false) {
				statusMessages.add("Podane hasla sa rozne");
			} 
			else {

				User newUser = new User(fullName, email, password);

				newUser.setRoles(new HashSet<Role>(roles));

				em.persist(newUser);
				if(REQUIRE_EMAIL_CONFIRMATION){
					newUser.setEmailConfirmed(false);
				    try {
						sendActivationEmail(newUser);
					} catch (MessagingException e) {
						statusMessages.add("Nie wyslano e-maila z potwierdzeniem skontaktuj sie z administratorem.");
						log.error(e);
					}
				}
				else {
					newUser.setEmailConfirmed(true);
				}
				
				newUser.setAccepted(!REQUIRE_ADMIN_ACCEPTATION);
				
				log
				.info("UserRegister.userRegister() action called with: #{UserRegister.fullName}");
				statusMessages.add("Zarejestrowano uzytkownika #{UserRegister.fullName}");
			
			//	return "/profileManager.xhtml";
				
				//czyscimy zawartosc formularza
				
			}
			fullName 		= null;
			email			= null;
			password 		= null;
			confirmPassword	= null;
		}
		// implement your business logic here
		
	//	return "/userRegister.xhtml";
	}

	private Role getRole(String roleName) {

		System.out.println("[UserRegisterBean] getRole() - roleName " + roleName  );
		
		Role role = (Role) em.createQuery("from Role where name = :name")
				.setParameter("name", roleName).getSingleResult();

		if (role != null) {
			return role;
		}

		return null;

	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompetences() {
		return competences;
	}

	public void setCompetences(String competences) {
		this.competences = competences;
	}

	public String getAllowedIPs() {
		return allowedIPs;
	}

	public void setAllowedIPs(String allowedIPs) {
		this.allowedIPs = allowedIPs;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Remove
	public void destroy() {
	}

	@Override
	public Boolean getEntrepreneurRole() {
		// TODO Auto-generated method stub
		return isEntrepreneur;
	}

	@Override
	public Boolean getScientistRole() {
		// TODO Auto-generated method stub
		return isScientist;
	}

	@Override
	public void setEntrepreneurRole(Boolean isEntrepreneur) {
		this.isEntrepreneur = isEntrepreneur;

	}

	@Override
	public void setScientistRole(Boolean isScientist) {
		this.isScientist = isScientist;

	}

	private void sendActivationEmail(User u) throws MessagingException {
		String token = getRanomToken(20);
		u.setActivationToken(token);
		
		String message = 
			"W celu potwierdzenia adresu e-mail otworz nastepujaca strone:\n"+
			getURL()+"/activation.seam?activationToken="+token+"&userId="+u.getId();
		
		EmailSenderBean.sendMail(u.getEmail(), "Aktywacja konta w serwisie Cognitive", message);
		
	}
	
	/**
	 * @return Case sensitive alphanumeric (a-z, A-Z, 0-9) random char
	 */
	private static char getRandom() {
		int random =  (int) (Math.random()*62);
		if(random<10){
			return (char)(random+48);
		}else if(random<36){
			return (char)(random+55);
		}else{
			return (char)(random+61);
		}
		}

	public static String getRanomToken(int lenght){
		StringBuilder sb = new StringBuilder();
		for(int i= 0; i<lenght; i++ ){
			sb.append(getRandom());
		}
		return sb.toString();
		
	}
	
	private String getURL(){

		HttpServletRequest request= (HttpServletRequest)facesContext.getExternalContext().getRequest();
		return request.getScheme()+ "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}
}
