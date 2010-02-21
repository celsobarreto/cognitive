package edu.uj.cognitive.action;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import edu.uj.cognitive.model.SpecialPage;

@Stateful
@Name("specialPages")
public class SPageAction implements SPage
{
	@Out(required = false, scope = ScopeType.EVENT)
	private SpecialPage sPage;
	
	@Out(required = true, scope = ScopeType.EVENT)
	private String pageID;
	
	@PersistenceContext
	private EntityManager em;
	
	public void toPage(String page)
	{
		this.sPage = this.em.find(SpecialPage.class, page);
		this.pageID = (this.sPage == null ? page : this.sPage.getId());
	}
	
	@Remove
	public void destroy()
	{
		if (this.sPage != null)
			this.sPage = null;
		if (this.pageID != null)
			this.pageID = "";
	}
	
	public String getPageContent(String pageId)
	{
		SpecialPage page = null;
		
		try
		{
			page = (SpecialPage)this.em.createQuery("select u from SpecialPage u where id=:id").setParameter("id", pageId).getSingleResult();
			System.out.println(page.getContent());
			this.sPage = page;
			this.pageID = pageId;			
		}
		catch (NoResultException e) 
		{
			this.pageID = "";
			return "";
		}
		
		return page.getContent();
	}
	
	public String getPageTitle(String pageId){
		SpecialPage page = (SpecialPage)this.em.createQuery("select u from SpecialPage u where id=:id").setParameter("id", pageId).getSingleResult();
		this.sPage = page;
		this.pageID = pageId;
		return page.getTitle();
	}	
}
