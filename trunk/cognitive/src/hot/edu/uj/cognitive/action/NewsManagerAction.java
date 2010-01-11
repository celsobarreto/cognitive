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
	
	@SuppressWarnings("unchecked")
	@Factory("newsList")
	public void getNews()
	{
		this.newsList = this.em.createQuery("select n from News n order by n.date DESC").getResultList();
	}
	
	@Remove
	public void destroy() 
	{
		this.news = null;
		this.newsList.clear();
		this.newsList = null;
	}
}
