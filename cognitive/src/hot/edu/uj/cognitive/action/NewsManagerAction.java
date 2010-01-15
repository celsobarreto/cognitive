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
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.contexts.Contexts;

import edu.uj.cognitive.model.News;
import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.User;

@Stateful
@Name("newsManager")
@Scope(ScopeType.SESSION)
public class NewsManagerAction implements NewsManager 
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel
	private List<News> newsList;
	
	@DataModelSelection(value = "newsList")
	@Out(required=false, value="news")
	@In(required=false, value="news")
	private News news;
	
	private actionType action;
	
	@SuppressWarnings("unchecked")
	@Factory("newsList")
	public void getListOfNews()
	{
		this.newsList = this.em.createQuery("select n from News n order by n.date DESC").getResultList();
	}
	
	@Override
	public String addNewsAction() 
	{
		action = actionType.ADD;
		System.out.println("CHANGE ACTION TYPE:" + action);
		this.news = new News();
		
		return "/addNewsForm.xhtml";
	}	
	
	@Override
	public String editNewsAction() 
	{
		action = actionType.EDIT;
		
		return "/editNewsForm.xhtml";
	}

	@Override
	public String deleteNewsAction() 
	{
		action = actionType.DELETE;
		
		return performNewsAction();
	}	
	
	@Override
	public String performNewsAction() 
	{
		System.out.println("ACTION: " + action);
		
		if(news != null)
			System.out.println(news);		
		
		switch(action)
		{
			case ADD:
				System.out.println("ADD NEWS ACTION:");
				System.out.println(news);
				news.setDate(new Date());
				em.persist(news);
				
			case EDIT:
				break;
				
			case DELETE:
				em.remove(news);
				news = null;
				break;
			
			default:
				break;
		}
		
		getListOfNews();
		
		return "/News.xhtml";
	}
	
	public News getNews()
	{
		System.out.println("## getNews()");
		if(this.news == null)
			System.out.println("## getNews() return null");
		
		return this.news;
	}
	
	public void setNews(News news)
	{
		System.out.println("## setNews()");
		this.news = news;
	}
	
	@Override
	public boolean manageEnabled() 
	{
		User user  = (User)Contexts.getSessionContext().get("loggedUser");
		
		if(user != null)
		{
			for(Role r: user.getRoles())
			{
				if(r.getName().equals("admin"))
					return true;
			}
		}
		
		return false;
	}
	
	@Remove
	public void destroy() 
	{
		if(this.news != null)
			this.news = null;
		
		if(this.newsList != null)
		{
			this.newsList.clear();
			this.newsList = null;
		}
	}
	
	enum actionType
	{
		ADD, EDIT, DELETE, NONE
	}
}
