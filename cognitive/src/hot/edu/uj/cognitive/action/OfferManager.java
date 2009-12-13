package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface OfferManager {
	public void destroy();
	public void getOffer();
}
