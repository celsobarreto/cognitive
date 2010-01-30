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
import edu.uj.cognitive.model.User;

@Stateful
@Scope(ScopeType.SESSION)
@Name("publicationManager")
public class PublicationManagerAction implements PublicationManager
{
	private int pageSize = 5;
	private String criterion = "";
	private int page;
	private boolean nextPageAvailable;
	private boolean prevPageAvailable;
	private String searchString;
	private int rowIndex = 0;

	@Out(required = false, scope = ScopeType.EVENT)
	private boolean details;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@DataModel
	private List<Publication> publicationList;

	@DataModelSelection(value = "publicationList")
	@Out(required = false)
	private Publication publication;

	@DataModel
	private List<User> publUserList;

	@SuppressWarnings("unchecked")
	public void findPublication() 
	{
		Query query;
		if (this.criterion != null && (this.criterion.equalsIgnoreCase("year") || this.criterion.equalsIgnoreCase("volume") || this.criterion.equalsIgnoreCase("pages")))
		{
			int value;
			try
			{
				value = ((this.searchString != null && this.searchString.length() > 0) ? Integer.parseInt(this.searchString) : 0);
			}
			catch (NumberFormatException e)	
			{
				value = 0;
			}
			query = this.em.createQuery("from Publication where " + this.criterion + " = :value").setParameter("value", value);
		}
		else if (this.criterion != null && this.criterion.equalsIgnoreCase("keywords"))
		{
			query = this.em.createQuery("select distinct p from Publication p join p.keywords k where lower(k.name) like #{pattern}");
		}
		else if (this.criterion != null && this.criterion.equalsIgnoreCase("users"))
		{
			query = this.em.createQuery("select distinct p from Publication p join p.users u where lower(u.fullName) like #{pattern}");
		}
		else if (this.criterion != null && this.criterion.length() > 0)
		{
			query = this.em.createQuery("from Publication where lower(" + this.criterion + ") like #{pattern}");
		}
		else
		{
			int value;
			try
			{
				value = ((this.searchString != null && this.searchString.length() > 0) ? Integer.parseInt(this.searchString) : 0);
			}
			catch (NumberFormatException e)	
			{
				value = 0;
			}
			query = this.em.createQuery("from Publication where lower(title) like #{pattern} or lower(link) like #{pattern} or lower(references) like #{pattern} or lower(journal) like #{pattern} or year = :value or volume = :value or pages = :value").setParameter("value", value);
		}
		List<Publication> results = query.setMaxResults(this.pageSize + 1).setFirstResult(this.page * this.pageSize).getResultList();
		this.nextPageAvailable = results.size() > this.pageSize;
		if (this.nextPageAvailable) 
		{
			this.publicationList = results.subList(0, this.pageSize);
		} 
		else 
		{
			this.publicationList = results;
		}
		this.prevPageAvailable = (this.page > 0);
		this.details = false;
		this.rowIndex = 0;
	}

	@Factory(value="pattern", scope=ScopeType.EVENT)
	public String getSearchPattern()
	{
		return (this.searchString == null ? "%" : '%' + this.searchString.toLowerCase().replace('*', '%') + '%');
	}

	public void search() 
	{
		this.page = 0;
		this.findPublication();
	}

	@Remove
	public void newSearch() 
	{
		this.publicationList = null;
		this.publUserList = null;
	}

	public boolean isNextPageAvailable() 
	{
		return this.nextPageAvailable;
	}

	public boolean isPrevPageAvailable()
	{
		return this.prevPageAvailable;
	}

	public void nextPage() 
	{
		this.page++;
		this.findPublication();
	}

	public void prevPage() 
	{
		if (this.page > 0)
		{
			this.page--;
			this.findPublication();
		}
	}

	public boolean isDetails()
	{
		return this.details;
	}

	public void showDetails()
	{
		this.publUserList = this.publication.getUsers();
		this.details = true;
	}

	public List<User> getPublUserList()
	{
		return this.publUserList;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	public String getSearchString()
	{
		return this.searchString;
	}

	public int getPageSize()
	{
		return this.pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getCriterion()
	{
		return this.criterion;
	}

	public void setCriterion(String criterion)
	{
		this.criterion = criterion;
	}

	public void setPublicationList(List<Publication> publicationList)
	{
		this.publicationList = publicationList;
	}

	public List<Publication> getPublicationList()
	{
		return this.publicationList;
	}

	public int getRowIndex()
	{
		return ++this.rowIndex + this.page * this.pageSize;
	}
}
