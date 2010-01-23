package edu.uj.cognitive.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.web.RequestParameter;

import edu.uj.cognitive.model.Role;
import edu.uj.cognitive.model.ScienceDomain;
import edu.uj.cognitive.model.User;

@Stateful
@Name("userManager")
@Scope(ScopeType.SESSION)
public class UserManagerBean implements UserManager
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel(scope = ScopeType.PAGE)
	private List<User> userList;
	
	@RequestParameter 
	private Integer userId;
	
	private String mode;
	
	private String searchText;
	
	private String searchCriteria;
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer id) {
		this.userId = id;
	}
	
	@Out(value="userProfile", required = false)	
	private User user;
	
	@SuppressWarnings("unchecked")
	@Factory("userList")
	public void list()
	{
		if(searchCriteria != null && !searchCriteria.equals("scienceDomains")){
			String search = "%"+searchText.replace("*", "%")+"%";
			this.userList = this.em.createQuery("select u from User u where u."+searchCriteria+
					" like '"+search+"' order by u.fullName").getResultList();
		}else{
			this.userList = this.em.createQuery("select u from User u").getResultList();
		}
		selectUsers();
		if(searchCriteria != null && searchCriteria.equals("scienceDomains")){
			searchUserScienceDomains();
		}
		searchText = null;
		searchCriteria = null;
	}
	
	private void searchUserScienceDomains(){
		Set<User> tmpList = new HashSet<User>();
		for(User u : userList){
			if(u.getScienceDomains() != null && u.getScienceDomains().size()>0){
				for(ScienceDomain s : u.getScienceDomains()){
					if(!s.getName().contains(searchText)){
						tmpList.add(u);
					}
				}
			}else{
				tmpList.add(u);
			}
		}
		userList.removeAll(tmpList);
	}
	
	private void selectUsers(){
		Set<User> tmpList = new HashSet<User>();
		for(User u : userList){
			if(u.getRoles() != null && u.getRoles().size()>0){
				for(Role r : u.getRoles()){
					System.out.println(mode);
					if(!r.getName().equals(mode)){
						tmpList.add(u);
					}
				}
			}else{
				tmpList.add(u);
			}
		}
		userList.removeAll(tmpList);
	}

	public void setSearchText(String text){
		this.searchText = text;
	}
	
	public String getSearchText(){
		return searchText;
	}
	
	public String getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.destroy();
		this.mode = mode;
	}
	
	public void delete() 
	{
		if (this.user != null)
		{
			this.userList.remove(this.user);
			this.em.remove(this.user);
			this.em.flush();
			this.user = null;
		}
	}

	@Remove
	public void destroy()
	{
		this.userList = null;
		this.user = null;
	}

	public void show()
	{
		
		this.user = (User) this.em.createQuery("select u from User u where u.id = "+userId.toString()).getSingleResult();


	}
}
