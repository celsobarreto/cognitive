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
import org.hibernate.validator.Pattern;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("user")
@Scope(ScopeType.SESSION)
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "users_seq", allocationSize = 1, initialValue = 10)
public class User implements Serializable
{
	private static final long	serialVersionUID	= 4996144773844174425L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Integer				id;

	@NotNull
	@Length(min = 3, max = 255)
	@Pattern(regex = "^\\w*$", message = "not a valid username")
	private String				fullName;

	@ManyToMany
	private List<Publication>	publications;

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setPublications(List<Publication> publications)
	{
		this.publications = publications;
	}

	public List<Publication> getPublications()
	{
		return this.publications;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getFullName()
	{
		return this.fullName;
	}
}
