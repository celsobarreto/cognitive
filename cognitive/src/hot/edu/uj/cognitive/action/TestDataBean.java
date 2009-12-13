package edu.uj.cognitive.action;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

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
import edu.uj.cognitive.model.Role;
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
	
	private Role adminRole;
	private Role scientistRole;
	private Role entrepreneurRole;
	
	private User admin;
	private User doktor;
	private User milioner;
	private User profesor;
	
	public void insert()
	{
		this.clearTables();
		this.insertRoles();
		this.insertUsers();
		this.insertSpecialPages();
		this.insertPublications();
		this.insertNews();
    	
        statusMessages.add("Test data loaded successfully.");
    }
	
	private void clearTables() {
		em.createQuery("DELETE SpecialPage s").executeUpdate();
		em.createNativeQuery("DELETE FROM users_publications").executeUpdate();
		em.createQuery("DELETE Publication p").executeUpdate();
		em.createNativeQuery("DELETE FROM users_roles").executeUpdate();
		em.createQuery("DELETE User u").executeUpdate();
		em.createQuery("DELETE Role r").executeUpdate();
		em.createQuery("DELETE News n").executeUpdate();
		em.flush();		
	}
	
	private void insertRoles() {
		adminRole = new Role();
		adminRole.setName("admin");
		this.em.persist(adminRole);
		
		scientistRole = new Role();
		scientistRole.setName("scientist");
		this.em.persist(scientistRole);
		
		entrepreneurRole = new Role();
		entrepreneurRole.setName("entrepreneur");
		this.em.persist(entrepreneurRole);
		
		this.em.flush();
	}

	private void insertUsers() {
		doktor = new User("mgr Doktor", "doktor@uj.pl", "doktor");
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {scientistRole})));
		this.em.persist(doktor);

		profesor = new User("prof. Wit A³a", "profesor@uj.pl", "profesor");
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {scientistRole})));
		this.em.persist(profesor);		
		
		milioner = new User("the Milioner II", "milioner@uj.pl", "milioner");
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {entrepreneurRole})));
		this.em.persist(milioner);		

		admin = new User("Administrator", "admin@uj.pl", "admin");
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {adminRole})));
		this.em.persist(admin);

		this.em.flush();
		this.em.refresh(doktor);
		this.em.refresh(profesor);
		this.em.refresh(milioner);
		this.em.refresh(admin);
	}
	
	private void insertSpecialPages() {
		SpecialPage sp = new SpecialPage();
		sp.setId("info");
		sp.setContent("content...");
		sp.setTitle("project info");
		this.em.persist(sp);		
	}
	
	private void insertNews() {
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
	}
	

	
	private void insertPublications() {
		Publication publ1 = new Publication();
		publ1.setTitle("publication 1");
		publ1.setYear(2000);
		publ1.setVolume(2);
		publ1.setPages(1234);
		this.em.persist(publ1);
		doktor.getPublications().add(publ1);
		
		Publication publ2 = new Publication();
		publ2.setTitle("publication 2");
		this.em.persist(publ2);
		profesor.getPublications().add(publ1);
		profesor.getPublications().add(publ2);		
	}
}
