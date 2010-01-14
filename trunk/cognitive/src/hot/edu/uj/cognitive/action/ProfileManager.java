package edu.uj.cognitive.action;

public interface ProfileManager {

	public String changeRequest();
	public String changePerform();
	
	public void setNewAddress(String s);
	public void setNewAddressConfirm(String s);
	public String getNewAddressConfirm();
	public String getNewAddress();
	public String getMessage();
	public void setMessage(String s);
}

