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
@Name("offer")
@Scope(ScopeType.EVENT)
@Table(name = "offer")
@SequenceGenerator(name = "offer_seq", sequenceName = "offer_seq", initialValue = 1, allocationSize = 1)
public class Offer implements Serializable{
	
	private static final long serialVersionUID = 7759813144512363139L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_seq")
	private Integer id;
	@NotNull
    private Integer entrepreneur_id;
	@NotNull
	@Length(max = 255)
	private String title;
	@NotNull
	@Length(max = 2000)
    private String content;
	@NotNull
    private Date dateAdded;
	@NotNull
	@Length(max = 10)
    private String offerType;
    
    public Offer(){    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEntrepreneur_id() {
		return entrepreneur_id;
	}

	public void setEntrepreneur_id(Integer entrepreneur_id) {
		this.entrepreneur_id = entrepreneur_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date date) {
		this.dateAdded = date;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
    
}
