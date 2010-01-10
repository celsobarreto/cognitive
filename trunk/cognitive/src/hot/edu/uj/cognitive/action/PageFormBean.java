package edu.uj.cognitive.action;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.SpecialPage;

@Stateful
@Name("pageForm")
@Scope(ScopeType.SESSION)
public class PageFormBean implements PageForm {
	@Logger private Log log;
	
		
	@PersistenceContext
	private EntityManager em;
	
	@RequestParameter
	private String pageId;	
	
	
	private String text;
	private String title;
	private String id;
	
	
	@Remove
	public void destroy() {
		this.text = null;
	}

	
	
	@Override
	public String getText() {
		
		return this.text;
	}

	@Override
	public void sendForm() {
		log.info("sendForm() "+this.title);
		Query query = this.em.createQuery("select u from SpecialPage u where u.id = :pageId");
		query.setParameter("pageId", this.id);
		SpecialPage sPage = (SpecialPage) query.getSingleResult();
		sPage.setContent(this.text);
		sPage.setTitle(this.title);
		em.persist(sPage);
	}

	@Override
	public void setText(String t) {
		this.text = t;

	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setTitle(String t) {
		this.title = t;
		
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String t) {
		this.id = t;
		
	}

	@Override
	public void showForm() {
		this.id = this.pageId;
		
		System.out.println(this.pageId); 
		log.info("showForm(): "+ this.pageId);
		Query query = this.em.createQuery("select u from SpecialPage u where u.id = :pageId");
		query.setParameter("pageId", this.pageId);
		SpecialPage sPage = (SpecialPage) query.getSingleResult();
		this.text = sPage.getContent();
		this.title = sPage.getTitle();
	}

}
