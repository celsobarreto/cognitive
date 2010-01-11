package edu.uj.cognitive.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


@Entity
@Name("news")
@Scope(ScopeType.EVENT)
@Table(name = "news")
@SequenceGenerator(name = "news_seq", sequenceName = "news_seq", initialValue = 1, allocationSize = 1)

public class News implements Serializable
{
        private static final long serialVersionUID = 1551491368638860686L;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_seq")
        private Integer id;

        @NotNull
        private Date date;

        @NotNull
        @Length(max = 255)
        private String title;

        @NotNull
        @Length(max = 2000)
        private String content;

		public Integer getId() 
		{
			return id;
		}

		public void setId(Integer id) 
		{
			this.id = id;
		}

		public Date getDate() 
		{
			return date;
		}

		public void setDate(Date date) 
		{
			this.date = date;
		}

		public String getTitle() 
		{
			return title;
		}

		public void setTitle(String title) 
		{
			this.title = title;
		}

		public String getContent() 
		{
			return content;
		}

		public void setContent(String content) 
		{
			this.content = content;
		}		
}
