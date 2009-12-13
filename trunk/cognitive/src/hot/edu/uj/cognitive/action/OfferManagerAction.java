package edu.uj.cognitive.action;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import edu.uj.cognitive.model.News;
import edu.uj.cognitive.model.Offer;

@Stateful
@Name("offerManager")
public class OfferManagerAction implements OfferManager {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel
	private List<News> offerList;
	
	@DataModelSelection(value = "offerList")
	@Out(required = false)
	private Offer offer;
	
	@SuppressWarnings("unchecked")
	@Factory("offerList")
	public void getOffer()
	{
		this.offerList = this.em.createQuery("select o from Offer o").getResultList();
	}
	
	@Remove
	public void destroy() 
	{
		this.offer = null;
		this.offerList.clear();
		this.offerList = null;
	}
}
