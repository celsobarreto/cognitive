package edu.uj.cognitive.action;

import javax.ejb.Local;

import edu.uj.cognitive.model.Offer;

@Local
public interface OfferManager {
	public void destroy();
	public void getOffer();
	public String getEntrepreneurName(Offer o);
	public String addOffer();
	public String saveOffer();
	public String editOffer();
	public void setOfferUpdate(Offer o);
	public Offer getOfferUpdate();
	public String removeOffer();
	public boolean isAllowed();
}
