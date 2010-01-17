package edu.uj.cognitive.action;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.User;

@Stateful
@Name("UserRegister")
public class UserRegisterBean implements UserRegister
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    
	@PersistenceContext
	private EntityManager em;  

    //private String value;
    
    private String fullName;
   

	private String email;
    private String competences;
    private String allowedIPs;
    
    private String password;
    private String confirmPassword;
    
    public void userRegister()
    {
    	if(fullName.equals(null))
    	{
    		
    	}
    	else if(email.equals(null))
    	{
    		
    	}
    	else if(password.equals(confirmPassword) == false)
    	{
    		
    			
    	}
    	else
    	{
    		User newUser = new User(fullName, email, password);
    	//	newUser.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {scientistRole})));
    		
    		em.persist(newUser);
    		
    		newUser.setAccepted(true);
    		newUser.setActivated(true);
    	}
    		
    	
    	// implement your business logic here
        log.info("UserRegister.userRegister() action called with: #{UserRegister.fullName}");
        statusMessages.add("userRegister #{UserRegister.fullName}");
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
    public void destroy() {}

	@Override
	public Boolean getEntrepreneurRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getScientistRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEntrepreneurRole(Boolean isEntrepreneur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScientistRole(Boolean isScientist) {
		// TODO Auto-generated method stub
		
	}

}
