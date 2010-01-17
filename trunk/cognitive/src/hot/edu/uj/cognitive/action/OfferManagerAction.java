package edu.uj.cognitive.action;

import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.Offer;
import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateful
@Scope(ScopeType.SESSION)
@Name("offerManager")
public class OfferManagerAction implements OfferManager {

	@Logger 
	private Log log;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel(scope=ScopeType.PAGE)
	private List<Offer> offerList;
	
	@DataModelSelection(value = "offerList")
	@In(required=false)
	@Out(required=false)
	private Offer offer;
	
	private Offer offerUpdate;
	
	private String removeParagraphTags(String content){
		return content.replaceAll("<p>", "").replaceAll("</p>", "");
	}
	
	@SuppressWarnings("unchecked")
	@Factory("offerList")
	public void getOffer()
	{
		this.offerList = this.em.createQuery("select o from Offer o").getResultList();
	}
	
	
	public String getEntrepreneurName(Offer o){
		int entrepreneurId = o.getEntrepreneur_id();
		User entrepreneur = (User)this.em.createQuery("select u from User u where id="+entrepreneurId).getSingleResult();
		
		return entrepreneur.getFullName();
	}
	
	
	public String addOffer() {
		User user  = (User)Contexts.getSessionContext().get("loggedUser");
		
		offer.setDateAdded(new Date());
		offer.setEntrepreneur_id(user.getId());
		
		String tmpContent = removeParagraphTags(offer.getContent());
		offer.setContent(tmpContent);
		
		em.persist(offer);
		this.getOffer();
		log.info("Oferta "+ offer.getTitle() +" zostala zapisana");
		return "/offer.xhtml";
	}

	
	public String removeOffer() {
		em.remove(offer);
		
		this.getOffer();
		log.info("Oferta "+ offer.getTitle() +" zostala usunieta");
		return "/offer.xhtml";
	}

	
	public String saveOffer() {
		offerUpdate.setDateAdded(new Date());
		
		String tmpContent = removeParagraphTags(offerUpdate.getContent());
		offerUpdate.setContent(tmpContent);
		
		em.merge(offerUpdate);
		log.info("Oferta "+ offerUpdate.getTitle() +" zostala zaktualizowana");
		return "/offer.xhtml";
	}
	
	
	public String editOffer(){
		offerUpdate = offer;
		return "/editOffer.xhtml";
	}
	
	
	public Offer getOfferUpdate(){
		return offerUpdate;
	}
	
	
	public void setOfferUpdate(Offer o){
		offerUpdate = o;
	}
	
	
	public boolean isAllowed() {
		User user  = (User)Contexts.getSessionContext().get("loggedUser");
		
		if(user != null){
			Role[] roles = user.getRoles().toArray(new Role[0]);
			
			if(roles != null){
				for(Role r:roles){
					if(r.getName().equals("entrepreneur")){
						return true;
					}
				}
			}
			
		}
		
		return false;
	}
	
	@Remove
	public void destroy() 
	{
		this.offer = null;
		this.offerList.clear();
		this.offerList = null;
	}
	
}
