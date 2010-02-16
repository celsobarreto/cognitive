package edu.uj.cognitive.action;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.SearchParameters;
import edu.uj.cognitive.model.User;

@Stateful
@Scope(ScopeType.SESSION)
@Name("publicationManager")
public class PublicationManagerAction implements PublicationManager
{
	private SearchParameters searchParam = new SearchParameters();
	private boolean details = false;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@DataModel
	private List<Publication> publicationList;

	@DataModelSelection(value = "publicationList")
	private Publication publicationSelect;
	
	@Out(required = false)
	private Publication publication;

	@DataModel
	private List<User> publUserList;

	@SuppressWarnings("unchecked")
	public void findPublication() 
	{
		Query query;
		if (this.searchParam.getCriterion() != null && (this.searchParam.getCriterion().equalsIgnoreCase("year") || this.searchParam.getCriterion().equalsIgnoreCase("volume") || this.searchParam.getCriterion().equalsIgnoreCase("pages")))
		{
			int value;
			try
			{
				value = ((this.searchParam.getSearchString() != null && this.searchParam.getSearchString().length() > 0) ? Integer.parseInt(this.searchParam.getSearchString()) : 0);
			}
			catch (NumberFormatException e)	
			{
				value = 0;
			}
			query = this.em.createQuery("from Publication where " + this.searchParam.getCriterion() + " = :value").setParameter("value", value);
		}
		else if (this.searchParam.getCriterion() != null && this.searchParam.getCriterion().equalsIgnoreCase("keywords"))
		{
			query = this.em.createQuery("select distinct p " +
										"from Publication p, IN(p.keywords) k " +
										"where lower(k.name) like #{pattern}");
		}
		else if (this.searchParam.getCriterion() != null && this.searchParam.getCriterion().equalsIgnoreCase("users"))
		{
			query = this.em.createQuery("select distinct p " +
										"from Publication p, IN(p.users) u " +
										"where lower(u.fullName) like #{pattern}");
		}
		else if (this.searchParam.getCriterion() != null && this.searchParam.getCriterion().length() > 0)
		{
			query = this.em.createQuery("from Publication where lower(" + this.searchParam.getCriterion() + ") like #{pattern}");
		}
		else
		{
			int value;
			try
			{
				value = ((this.searchParam.getSearchString() != null && this.searchParam.getSearchString().length() > 0) ? Integer.parseInt(this.searchParam.getSearchString()) : 0);
			}
			catch (NumberFormatException e)	
			{
				value = 0;
			}
			query = this.em.createQuery("select distinct p " +
										"from Publication p " +
										"left join p.users u left join p.keywords k " +
										"where lower(u.fullName) like #{pattern} " +
										"or lower(k.name) like #{pattern} " +
										"or lower(title) like #{pattern} " +
										"or lower(link) like #{pattern} " +
										"or lower(authors) like #{pattern} " +
										"or lower(references) like #{pattern} " +
										"or lower(journal) like #{pattern} " +
										"or year = :value " +
										"or volume = :value " +
										"or pages = :value").setParameter("value", value);
		}
		List<Publication> results = query.setMaxResults(this.searchParam.getPageSize() + 1).setFirstResult(this.searchParam.getPage() * this.searchParam.getPageSize()).getResultList();
		this.searchParam.setNextPageAvailable(results.size() > this.searchParam.getPageSize());
		if (this.searchParam.isNextPageAvailable()) 
		{
			this.publicationList = results.subList(0, this.searchParam.getPageSize());
		} 
		else 
		{
			this.publicationList = results;
		}
		this.searchParam.setPrevPageAvailable(this.searchParam.getPage() > 0);
		this.details = false;
		this.searchParam.setRowIndex(0);
	}

	@Factory(value="pattern", scope=ScopeType.EVENT)
	public String getSearchPattern()
	{
		return (this.searchParam.getSearchString() == null ? "%" : '%' + this.searchParam.getSearchString().toLowerCase().replace('*', '%') + '%');
	}

	public void search() 
	{
		this.searchParam.setPage(0);
		this.findPublication();
	}

	@Remove
	public void newSearch() 
	{
		this.publicationList = null;
		this.publUserList = null;
		this.publication = null;
	}

	public void nextPage() 
	{
		this.searchParam.setPage(this.searchParam.getPage() + 1);
		this.findPublication();
	}

	public void prevPage() 
	{
		if (this.searchParam.getPage() > 0)
		{
			this.searchParam.setPage(this.searchParam.getPage() - 1);
			this.findPublication();
		}
	}

	public boolean isDetails()
	{
		return this.details;
	}

	public void showDetails()
	{
		this.publication = this.publicationSelect;
		this.publUserList = this.publication.getUsers();
		this.details = true;
		this.searchParam.setRowIndex(0);
	}

	public List<User> getPublUserList()
	{
		return this.publUserList;
	}

	public List<Publication> getPublicationList()
	{
		return this.publicationList;
	}

	public void setSearchParam(SearchParameters searchParam) 
	{
		this.searchParam = searchParam;
	}

	public SearchParameters getSearchParam() 
	{
		return this.searchParam;
	}
}
