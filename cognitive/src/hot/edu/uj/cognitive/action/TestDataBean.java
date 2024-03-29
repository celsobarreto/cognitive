package edu.uj.cognitive.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.Keyword;
import edu.uj.cognitive.model.KeywordFactory;
import edu.uj.cognitive.model.News;
import edu.uj.cognitive.model.Offer;
import edu.uj.cognitive.model.OfferTypeEnum;
import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.ScienceDomain;
import edu.uj.cognitive.model.SpecialPage;
import edu.uj.cognitive.model.User;

@Stateful
@Name("TestData")
@Scope(ScopeType.SESSION)
/**
 * By wstawic dane testowe otwórz w przeglądarce:
 * /testData.seam?token=6936598e6e25e96bb9694b30187b42a4
 */
public class TestDataBean implements TestData
{
    @Logger private Log log;

    @In StatusMessages statusMessages;

    @RequestParameter
    public String token;
    
    // top secret
    private final String EXPECTED_TOKEN = "6936598e6e25e96bb9694b30187b42a4";
    
	@PersistenceContext
	private EntityManager em;  
	
	private Role adminRole;
	private Role scientistRole;
	private Role entrepreneurRole;
	
	private User admin;
	private User doktor;
	private User milioner;
	private User profesor;
	
	private final String dummyText = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris eget lacus urna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis sodales elit eu eros imperdiet sodales. Donec iaculis leo in felis pretium cursus. Nam pulvinar mattis ornare. Aliquam erat volutpat. Nulla facilisi. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In gravida condimentum elit, eu ultrices enim mattis vitae. Sed volutpat urna eu erat tempor placerat. Suspendisse vestibulum varius quam in lobortis.</p>" +
			"<p>Integer rhoncus tincidunt eleifend. Pellentesque sit amet mi nisi. Phasellus volutpat lacinia nisi, et pellentesque dolor tincidunt ut. Nulla est velit, tempus eget luctus nec, varius eu nisl. Donec at libero sed ante pulvinar lobortis. Nam viverra, nisl sit amet facilisis dapibus, nibh tortor bibendum arcu, ac tincidunt enim mauris non metus. Fusce laoreet malesuada egestas. Aenean at nisi odio, sed laoreet justo. Vivamus vitae dolor eu nisi rutrum ullamcorper a sit amet eros. Nullam scelerisque dignissim purus. Maecenas a est sit amet ante dignissim blandit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sit amet justo lorem, at scelerisque augue. Aenean laoreet tempus pharetra. Sed mattis mollis orci, quis posuere augue sodales id. Nam vestibulum vestibulum sem, sit amet sodales libero tempor in.</p>" +
			"<p>Praesent malesuada consequat venenatis. Donec a nisl eget nisi sollicitudin dignissim porttitor et nisi. Aenean euismod ullamcorper fermentum. In fringilla bibendum nisi non lobortis. Nulla posuere arcu vitae quam iaculis dignissim. Quisque at velit arcu. Vestibulum ut libero metus, id adipiscing lorem. Donec a tristique dui. Quisque commodo ipsum ut mi blandit in aliquet neque lacinia. Praesent iaculis tellus ante. Sed pharetra nulla id lacus volutpat imperdiet. Praesent ullamcorper eleifend ligula, vitae cursus nunc molestie vitae. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla consequat, velit vel semper mollis, orci urna vestibulum turpis, ac vestibulum magna purus sed dolor. Vestibulum quis urna nec turpis ultricies sollicitudin nec sit amet nisi. Ut vel elit quis libero ultricies commodo. Cras a consectetur odio. Phasellus ac magna ipsum.</p>" +
			"<p>Nam ac massa ac magna blandit malesuada vitae posuere nulla. Nam a metus cursus sem dignissim molestie. Cras sed enim tellus. Nulla pretium facilisis dui, ut feugiat velit iaculis in. In consequat feugiat nunc, sit amet placerat nulla ornare non. Suspendisse faucibus, risus a sagittis consequat, purus elit suscipit nibh, quis lobortis ligula tellus quis massa. Sed congue leo sed leo iaculis eget ornare nulla hendrerit. Integer pulvinar quam in lectus pharetra bibendum. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla in eros leo. Nulla vitae elit vitae ligula posuere mattis. Sed mauris turpis, faucibus sit amet ornare vitae, ultricies eget magna. Donec molestie dictum purus nec vestibulum. Aenean semper consectetur sodales. Fusce tempor varius condimentum. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Quisque vehicula, arcu in sollicitudin blandit, mi risus pellentesque urna, ut tempor ipsum lacus nec erat.</p>" +
			"<p>Proin sit amet semper nunc. Nullam urna dolor, placerat at accumsan blandit, dictum in magna. Vivamus semper ornare purus a rhoncus. Sed pretium volutpat gravida. Nunc et mi non nisl accumsan blandit. Sed scelerisque orci et libero vestibulum quis tincidunt nisi sollicitudin. Vivamus ut consequat sem. Pellentesque eu leo ipsum. Fusce leo urna, molestie sed aliquam sed, pharetra eu tellus. Phasellus sed lobortis tortor. Duis convallis sapien quis est suscipit in vestibulum arcu convallis. Sed ac purus diam. Praesent pulvinar rutrum sem, a sodales felis dignissim quis. Suspendisse non risus ipsum. Sed dictum viverra ornare. In hac habitasse platea dictumst." +
			"</p>";
	
	public void insert()
	{
		if (token == null) {
			statusMessages.add("Musisz podać token aby wykonać tę operację.");
		} else if (!token.equals(EXPECTED_TOKEN)) {
			statusMessages.add("Niepoprawny token. Nie możesz wykonać tej operacji.");	
		} else {
			this.clearTables();
			this.insertRoles();
			this.insertUsers();
			this.insertSpecialPages();
			this.insertPublications();
			this.insertPropositions();
			this.insertNews();
			this.insertScienceDomains();
	        statusMessages.add("Dane testowe zostały wstawione do bazy.");
		}
    }
	
	private void clearTables() {
		em.createQuery("DELETE SpecialPage s").executeUpdate();
		
		em.createNativeQuery("DELETE FROM publications_keywords").executeUpdate();
		em.createQuery("DELETE Keyword p").executeUpdate();
		
		em.createNativeQuery("DELETE FROM users_publications").executeUpdate();
		em.createQuery("DELETE Publication p").executeUpdate();
		
		em.createNativeQuery("DELETE FROM users_sciencedomain").executeUpdate();
		em.createQuery("DELETE ScienceDomain sc").executeUpdate();
		
		em.createNativeQuery("DELETE FROM users_roles").executeUpdate();
		em.createQuery("DELETE User u").executeUpdate();
		em.createQuery("DELETE Role r").executeUpdate();
		
		em.createQuery("DELETE News n").executeUpdate();
		em.createQuery("DELETE Offer o").executeUpdate();
		em.flush();		
	}
	
	private void insertRoles() {
		adminRole = new Role();
		adminRole.setName(Role.ADMIN_ROLE);
		this.em.persist(adminRole);
		
		scientistRole = new Role();
		scientistRole.setName(Role.SCIENTIST_ROLE);
		this.em.persist(scientistRole);
		
		entrepreneurRole = new Role();
		entrepreneurRole.setName(Role.ENTREPRENEUR_ROLE);
		this.em.persist(entrepreneurRole);
		
		this.em.flush();
	}

	private void insertUsers() {
		doktor = new User("mgr Doktor", "doktor@uj.pl", "doktor", true, true);
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {scientistRole})));
		this.em.persist(doktor);

		profesor = new User("prof. Wit Ała", "profesor@uj.pl", "profesor", true, true);
		doktor.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {scientistRole})));
		this.em.persist(profesor);		
		
		milioner = new User("the Milioner II", "milioner@uj.pl", "milioner", true, true);
		milioner.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {entrepreneurRole})));
		this.em.persist(milioner);		

		admin = new User("Admin", "admin@uj.pl", "admin", true, true);
		admin.setRoles(new HashSet<Role>(Arrays.asList(new Role[] {adminRole})));
		admin.setAllowedIPs("127.0.0.1"); // oddzielone przecinkiem
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
		sp.setTitle("O projekcie");		
		sp.setContent(dummyText);
		this.em.persist(sp);

		sp = new SpecialPage();		
		sp.setId("staff");
		sp.setTitle("Wykonawcy i współpracownicy");		
		sp.setContent(dummyText);		
		this.em.persist(sp);
		
		sp = new SpecialPage();		
		sp.setId("links");
		sp.setTitle("LINKI");		
		sp.setContent("<ul class='linkList'><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li><li> <a href='onet.pl'>onet.pl</a><p class='description'>najwiekszy polski portal</p></li></ul>");		
		this.em.persist(sp);
		
		sp = new SpecialPage();		
		sp.setId("sgkat1");
		sp.setTitle("NAUKOWCY");		
		sp.setContent("krotki opis co sie zawiera w zakladce naukowcy");		
		this.em.persist(sp);
		
		sp = new SpecialPage();		
		sp.setId("sgkat2");
		sp.setTitle("PUBLIKACJE");		
		sp.setContent("krotki opis co sie zawiera w zakladce publikacje");		
		this.em.persist(sp);		
		
		sp = new SpecialPage();		
		sp.setId("sgkat3");
		sp.setTitle("PRZEDSIEBIORCY");		
		sp.setContent("krotki opis co sie zawiera w zakladce przedsiębiorcy");		
		this.em.persist(sp);		
		
		sp = new SpecialPage();		
		sp.setId("blockextra");
		sp.setTitle("PRAWA BELKA");		
		sp.setContent("prawa belka content");		
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
		
		News news3 = new News();
		news3.setTitle("Trzecia wiadomosc");
		news3.setContent("A to dla odmiany jest zawartosc trzeciej wiadomosci :)");
		news3.setDate(new Date());
		this.em.persist(news3);			
	}
	private void insertScienceDomains(){
		ScienceDomain domain = new ScienceDomain();
		domain.setName("Matematyka dyskretna");
		domain.setDescription("bardzo skomplikowana dziedzina");
		this.em.persist(domain);
		ScienceDomain domain2 = new ScienceDomain();
		domain2.setName("Teoria mnogosci");
		domain2.setDescription("jeszcze bardziej skomplikowana dziedzina");
		this.em.persist(domain2);
	}
	private void insertPublications() {
		Publication publ1 = new Publication();
		publ1.setTitle("publication 1");
		publ1.setAuthors("anonim");
		publ1.setLink("http://www.wp.pl");
		publ1.setJournal("journal");
		publ1.setYear(2000);
		publ1.setVolume(2);
		publ1.setPages(1234);
		publ1.setKeywords(new KeywordFactory(this.em).createFromText("test keyword"));
		this.em.persist(publ1);
		doktor.getPublications().add(publ1);
		
		Publication publ2 = new Publication();
		publ2.setTitle("publication 2");
		publ2.setPages(546);
		publ2.setLink("http://www.onet.pl");
		publ2.setYear(2010);
		publ2.setKeywords(new KeywordFactory(this.em).createFromText("cognitive test"));
		this.em.persist(publ2);
		profesor.getPublications().add(publ1);
		profesor.getPublications().add(publ2);
	}
	
	private void insertPropositions(){
		Offer offer1 = new Offer();
		offer1.setTitle("Tytul 1");
		offer1.setContent("To jest niezmiernie ciekawa praca");
		offer1.setDateAdded(new Date());
		offer1.setEntrepreneur_id(milioner.getId());
		offer1.setOfferType(OfferTypeEnum.JOB.getOfferType());
		this.em.persist(offer1);
		
		Offer offer2 = new Offer();
		offer2.setTitle("Tytul 2");
		offer2.setContent("A tu z kolei mamy niezmiernie ciekawa oferte uslugi");
		offer2.setDateAdded(new Date());
		offer2.setEntrepreneur_id(milioner.getId());
		offer2.setOfferType(OfferTypeEnum.SERVICE.getOfferType());
		this.em.persist(offer2);
	}
	
	@Remove
	public void destroy() {
		this.token = null;
	}	
}
