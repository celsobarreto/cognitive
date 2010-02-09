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
@Name("keyword")
@Scope(ScopeType.EVENT)
@Table(name = "keywords")
@SequenceGenerator(name = "kw_seq", sequenceName = "keyword_seq", initialValue = 10, allocationSize = 1)
public class Keyword implements Serializable
{

	private static final long serialVersionUID = -2011171183931241915L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kw_seq")
	private Integer	id;

	@NotNull
	@Length(max = 255)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String toString(){
		return getName();
	}
	

}
