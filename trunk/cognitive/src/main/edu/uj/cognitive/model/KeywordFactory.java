package edu.uj.cognitive.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class KeywordFactory {
	private EntityManager em;
	
	public KeywordFactory(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * Nie testowałem! [Adam]
	 */
	public List<Keyword> createFromText(String text) {
		String[] kwNames = text.split("\\W"); // podziel na wszystkim co nie jest alfanumeryczne
		List<Keyword> keywords = new ArrayList<Keyword>();
		for (String name: kwNames) {
			if(name.length()<3) 
				continue;
			
			try {
				Keyword kw = (Keyword) this.em.createQuery("select kw from Keyword kw where name=:name")
					.setParameter("name", name).getSingleResult();
				keywords.add(kw);
			} catch (NoResultException e) {
				Keyword kw = new Keyword();
				kw.setName(name);
				this.em.persist(kw);
				keywords.add(kw);
			}
		}
		return keywords;
	}
}
