package edu.uj.cognitive.action;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.SpecialPage;
import edu.uj.cognitive.model.User;

@Stateful
@Name("pageEdit")
public class PageEditAction implements PageEdit
{
	@Logger private Log log;
	
	
	@Out(required = false)
	private SpecialPage sPage;
	
	@DataModel
	private List<SPage> pageList;
	
	@DataModelSelection(value = "pageList")	
	
	@Out(required = false)
	private String pageID;
	
	@PersistenceContext
	private EntityManager em;
	
	public void showPage(String page)
	{
		log.info("authenticating {0}", page);
		this.sPage = this.em.find(SpecialPage.class, page);
		this.pageID = (this.sPage == null ? page : this.sPage.getId());
	}
	public void savePage(String page){
		
	}
	public void showList(){
		this.pageList = this.em.createQuery("select u from SpecialPage u").getResultList();
	}
	
	@Remove
	public void destroy()
	{
		if (this.sPage != null)
			this.sPage = null;
		if (this.pageID != null)
			this.pageID = "";
	}
}
