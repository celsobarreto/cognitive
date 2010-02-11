package edu.uj.cognitive.action;

import java.util.List;


import edu.uj.cognitive.model.Publication;

public interface ProfileManager {

	public String changeRequest();
	public String changePerform();
	public void destroy();	
	public void setNewAddress(String s);
	public void setNewAddressConfirm(String s);
	public String getNewAddressConfirm();
	public String getNewAddress();
	public String getToken();
	public void setToken(String s);

	public void setPublications(List<Publication> p);
	public List<Publication> getPublications();
	public Publication getPublication();
	public void setPublication(Publication p);
	public void removePublication();
	public String editPublication();
	public void newPublication();
	public String savePublication();
	public String getAction();
	public void setAction(String s);
	public String getPublicationKeywords();
	public void setPublicationKeywords(String k);
}

