package edu.uj.cognitive.action;

import javax.ejb.Local;

import edu.uj.cognitive.model.Offer;

@Local
public interface OfferManager {
	public void destroy();
	public void getOffer();
	public String getEntrepreneurName(Offer o);
}
