package edu.uj.cognitive.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("specialPage")
@Scope(ScopeType.EVENT)
@Table(name = "specialpages")
public class SpecialPage implements Serializable
{
	private static final long	serialVersionUID	= 7222720030512478049L;

	@Id
	private String				id;

	@NotNull
	@Length(min = 3, max = 255)
	private String				title;

	@NotNull
	@Lob
	private String				content;

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
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

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getContent()
	{
		return this.content;
	}
}
