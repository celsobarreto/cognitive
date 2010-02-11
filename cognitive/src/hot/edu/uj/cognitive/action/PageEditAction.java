package edu.uj.cognitive.action;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.SpecialPage;

@Stateful
@Name("pageEdit")
public class PageEditAction implements PageEdit
{
	@Logger private Log log;
	
	@In private FacesContext facesContext;  
	@Out(required = false)
	private SpecialPage sPage;
	
	@DataModel
	private List<SPage> pageList;
	
	@DataModelSelection(value = "pageList")	
	
	@Out(required = false)
	private String pageID;
 
	private String text;

	@PersistenceContext
	private EntityManager em;
	
	@RequestParameter
	private String pageId;
	
	@SuppressWarnings("unchecked")
	public void showPage()
	{
		System.out.println(this.pageId); 
		log.info(this.pageId);
		Query query = this.em.createQuery("select u from SpecialPage u where u.id = :pageId");
		query.setParameter("pageId", this.pageId);
		this.sPage = (SpecialPage) query.getSingleResult();
		this.pageID = (this.sPage == null ? "error": this.sPage.getId());
		this.text = this.sPage.getContent();
	}

	public void showList(){
		log.info("showList");
		this.pageList = this.em.createQuery("select u from SpecialPage u").getResultList();
	}
	
	@Remove
	public void destroy()
	{
		if (this.sPage != null)
			this.sPage = null;
		if (this.pageID != null)
			this.pageID = "";
		if (this.text != null)
			this.text = "";
	}
	@Restrict("#{s:hasRole('admin')}")
	public void savePage() {
		log.info("savePage");
		log.info(this.pageId);
		Query query = this.em.createQuery("select u from SpecialPage u where u.id = :pageId");
		query.setParameter("pageId", this.pageId);
		this.sPage = (SpecialPage) query.getSingleResult();
		this.sPage.setContent(this.text);
		this.em.persist(this.sPage);
		this.pageID = (this.sPage == null ? "error": this.sPage.getId());
		facesContext.addMessage(null, new FacesMessage("Strona została zapisana"));
	}
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.sPage.getContent();
	}
}
