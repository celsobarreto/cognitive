package edu.uj.cognitive.action;

import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessages;

import edu.uj.cognitive.model.News;
import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.SpecialPage;
import edu.uj.cognitive.model.User;

@Stateless
@Name("TestData")
public class TestDataBean implements TestData
{
    @Logger private Log log;

    @In StatusMessages statusMessages;

	@PersistenceContext
	private EntityManager em;    
    
	public void insert()
	{
		em.createQuery("DELETE SpecialPage s").executeUpdate();
		em.createNativeQuery("DELETE FROM users_publications").executeUpdate();
		em.createQuery("DELETE Publication p").executeUpdate();
		em.createQuery("DELETE User u").executeUpdate();
		em.createQuery("DELETE News n").executeUpdate();
		em.flush();
		
		SpecialPage sp = new SpecialPage();
		sp.setId("info");
		sp.setContent("content...");
		sp.setTitle("project info");
		this.em.persist(sp);
		
		User testuser = new User();
		testuser.setFullName("testUser");
		this.em.persist(testuser);
		
		User admin = new User();
		admin.setFullName("admin");
		this.em.persist(admin);
		
		this.em.flush();
		this.em.refresh(testuser);
		this.em.refresh(admin);
		
		Publication publ1 = new Publication();
		publ1.setTitle("publication 1");
		publ1.setYear(2000);
		publ1.setVolume(2);
		publ1.setPages(1234);
		this.em.persist(publ1);
		testuser.getPublications().add(publ1);
		
		Publication publ2 = new Publication();
		publ2.setTitle("publication 2");
		this.em.persist(publ2);
		admin.getPublications().add(publ1);
		admin.getPublications().add(publ2);
		
		News news1 = new News();
		news1.setTitle("Pierwsza wiadomosc");
		news1.setContent("To jest zawartosc pierwszej wiadomosci :)");
		news1.setDate(new Date());
		this.em.persist(news1);
		
		News news2 = new News();
		news2.setTitle("Druga wiadomosc");
		news2.setContent("A to dla odmiany jest zawartosc drugiej wiadomosci :)");
		news2.setDate(new Date());
		this.em.persist(news2);
    	
        statusMessages.add("Test data loaded successfully.");
    }
}
