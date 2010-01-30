package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface UserRegister
{
    public void userRegister();
    public void destroy();

    // add additional interface methods here
    
    public void setFullName(String fullName);
    public String getFullName();
    
	public void setEmail(String email);
	public String getEmail();
	
	
	public void setCompetences(String competences);
	public String getCompetences();
	
	public void setAllowedIPs(String allowedIPs);
	public String getAllowedIPs();
	
	public void setPassword(String password);
	public String getPassword();
	
	public void setConfirmPassword(String confirmPassword);
	public String getConfirmPassword();
		
	public Boolean getScientistRole();
	public void setScientistRole(Boolean isScientist);

	public Boolean getEntrepreneurRole();
	public void setEntrepreneurRole(Boolean isEntrepreneur);
	
	
	

}
