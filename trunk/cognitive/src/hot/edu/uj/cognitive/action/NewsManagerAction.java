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
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import edu.uj.cognitive.model.News;

@Stateful
@Name("newsManager")
public class NewsManagerAction implements NewsManager 
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel
	private List<News> newsList;
	
	@DataModelSelection(value = "newsList")
	@Out(required = false)
	private News news;
	
	private String tmpTitle;
	private String tmpContent;
	
	@SuppressWarnings("unchecked")
	@Factory("newsList")
	public void getNews()
	{
		this.newsList = this.em.createQuery("select n from News n order by n.date DESC").getResultList();
	}
	
	@Override
	public void addNews() 
	{		
		System.out.println("ADD NEWS:");
		System.out.println(" Title: " + news.getTitle());
		System.out.println(" Content: " + news.getContent());
		
		news.setDate(new Date());
		em.persist(news);
	}
	
	@Factory("news")
	public void createNews() 
	{
		news = new News();
	}
	
	@Remove
	public void destroy() 
	{
		this.news = null;
		
		if(this.newsList != null)
		{
			this.newsList.clear();
			this.newsList = null;
		}
	}
}
