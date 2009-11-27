package edu.uj.cognitive.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("publication")
@Scope(ScopeType.EVENT)
@Table(name = "publications")
@SequenceGenerator(name = "publ_seq", sequenceName = "publication_seq", initialValue = 10, allocationSize = 1)
public class Publication implements Serializable
{
	private static final long	serialVersionUID	= 2415558501274828607L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publ_seq")
	private Integer				id;

	@NotNull
	@Length(max = 255)
	private String				title;

	@Length(max = 1000)
	private String				keywords;

	@Length(max = 1000)
	private String				authors;

	@Length(max = 255)
	private String				link;

	private Integer				year;

	@Length(max = 255)
	private String				references;

	@Length(max = 255)
	private String				journal;

	private Integer				volume;

	private Integer				pages;

	@ManyToMany(mappedBy = "publications")
	private List<User>			users;

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	public String getKeywords()
	{
		return this.keywords;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	public String getAuthors()
	{
		return this.authors;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getLink()
	{
		return this.link;
	}

	public void setYear(Integer year)
	{
		this.year = year;
	}

	public Integer getYear()
	{
		return this.year;
	}

	public void setReferences(String references)
	{
		this.references = references;
	}

	public String getReferences()
	{
		return this.references;
	}

	public void setJournal(String journal)
	{
		this.journal = journal;
	}

	public String getJournal()
	{
		return this.journal;
	}

	public void setVolume(Integer volume)
	{
		this.volume = volume;
	}

	public Integer getVolume()
	{
		return this.volume;
	}

	public void setPages(Integer pages)
	{
		this.pages = pages;
	}

	public Integer getPages()
	{
		return this.pages;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public List<User> getUsers()
	{
		return this.users;
	}

}
