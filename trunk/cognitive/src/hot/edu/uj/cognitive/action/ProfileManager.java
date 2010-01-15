package edu.uj.cognitive.action;

import java.util.List;

import edu.uj.cognitive.model.Publication;

public interface ProfileManager {

	public String changeRequest();
	public String changePerform();
	
	public void setNewAddress(String s);
	public void setNewAddressConfirm(String s);
	public String getNewAddressConfirm();
	public String getNewAddress();
	public String getMessage();
	public void setMessage(String s);
	public String getToken();
	public void setToken(String s);

	public void setPublications(List<Publication> p);
	public List<Publication> getPublications();
	public Publication getPublication();
	public void setPublication(Publication p);
	public void removePublication();
	public void editPublication();
	
}

