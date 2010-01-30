package edu.uj.cognitive.action;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import edu.uj.cognitive.google.scholar.Downloader;
import edu.uj.cognitive.google.scholar.GoogleScholarResultParser;
import edu.uj.cognitive.google.scholar.GoogleScholarSimpleDownloader;
import edu.uj.cognitive.google.scholar.Parser;
import edu.uj.cognitive.model.KeywordFactory;
import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.User;

@Stateful
@Name("googleScholar")
@Scope(ScopeType.EVENT)
public class GoogleScholarAction implements GoogleScholar
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private int startPage = 1;
	private int parsePages = 1;
	private String keyword = "cognitive";
	
	public void parse()
	{
		if (this.startPage < 1 || this.parsePages < 1 || this.keyword.length() < 3)
			return;
		
		User googleUser;
		try
		{
			googleUser = (User) this.em.createQuery("from User where email = :email").setParameter("email", "scholar@google.com").getSingleResult();
		}
		catch (NoResultException e) 
		{
			googleUser = new User("Google Scholar", "scholar@google.com", "google", true, true);
			this.em.persist(googleUser);
			this.em.flush();
			this.em.refresh(googleUser);
		}
		
		if (googleUser == null)
			return;
		
		Publication publication;
		
		Downloader downloader = new GoogleScholarSimpleDownloader(this.keyword.toLowerCase());
		Parser parser = new GoogleScholarResultParser();
		
		for (int i = this.startPage - 1 ; i < this.startPage + this.parsePages - 1 ; i++)
		{
			parser.parsePage(downloader.getPage(i));
			HashMap<String, ArrayList<String>> result = parser.getData();
			
			if (result == null)
				continue;
			
			for (String key : result.keySet()) // key = publication title
			{
				ArrayList<String> value = result.get(key); // 0 = authors, 1 = link, 2 = keywords
				
				if (value.get(0) == null || value.get(1) == null || value.get(2) == null || value.get(1).length() < 5 || key.length() < 3)
					continue;
				
				try
				{
					this.em.createQuery("from Publication where link = :link").setParameter("link", value.get(1)).getSingleResult();
				}
				catch (NoResultException e) 
				{
					publication = new Publication();
					publication.setTitle(key);
					publication.setAuthors(value.get(0));
					publication.setLink(value.get(1));
					publication.setKeywords(new KeywordFactory(this.em).createFromText(value.get(2)));
					this.em.persist(publication);
					googleUser.getPublications().add(publication);
				}
			}
		}
		downloader.shutdown();
	}

	@Remove
	public void destroy()
	{

	}

	public void setStartPage(int startPage)
	{
		this.startPage = startPage;
	}

	public int getStartPage()
	{
		return this.startPage;
	}

	public void setParsePages(int parsePages)
	{
		this.parsePages = parsePages;
	}

	public int getParsePages()
	{
		return this.parsePages;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getKeyword()
	{
		return this.keyword;
	}
}
