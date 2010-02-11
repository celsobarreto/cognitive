package edu.uj.cognitive.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.contexts.Contexts;


import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.ScienceDomain;
import edu.uj.cognitive.model.SpecialPage;

import edu.uj.cognitive.model.Role;

import edu.uj.cognitive.model.User;

@Stateful
@Scope(ScopeType.SESSION)
@Name("userManager")
public class UserManagerBean implements UserManager
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel(scope = ScopeType.PAGE)
	private List<User> userList;
	
	@DataModel
	private List<ScienceDomain> sdList;
	
	@RequestParameter 
	private Integer userId;
	

	private Integer editUserId;
	

	private String mode;
	
	private String searchText;
	
	private String searchCriteria;
	

	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer id) {
		this.userId = id;
	}
	
	@In(required = false)
	@Out(required = false)	
	private User user;
	
	@Out(required=false, value="userProfile")
	private User userProfile;
	
	@In 
	private FacesContext facesContext;
	
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

	
	@SuppressWarnings("unchecked")
	@Factory("sdList")
	public void scienceDomainList()
	{
		User loggedUser = (User) Contexts.getSessionContext().get("loggedUser");
		sdList = em.createQuery("select sc from ScienceDomain sc").getResultList();
		for (ScienceDomain sd : sdList)
		{
			boolean sel = false;
			for (ScienceDomain userSd : loggedUser.getScienceDomains())
			{
				if (userSd.getName().equals(sd.getName()))
				{
					sel = true;
					break;
				}
			}
			sd.setSelected(sel);
		}
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
		userProfile = null;
		sdList = null;
	}

	public void show()
	{
		this.userProfile = (User) this.em.createQuery("select u from User u where u.id = " + userId.toString()).getSingleResult();
		editUserId = userId;
		user = userProfile;
	}
	
	public void edit()
	{
		List<ScienceDomain> newList = new ArrayList<ScienceDomain>();
		for(ScienceDomain sd : sdList)
		{
			if (sd.getSelected())
				newList.add(sd);
		}
		
		User logUsr = (User) Contexts.getSessionContext().get("loggedUser");
		boolean changeAllowedIp = false;
		if (logUsr != null && logUsr.getId().equals(editUserId)) 
		{
			String IP = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRemoteAddr();
			for (String userIp : user.getAllowedIPs().split(","))
			{
				if (userIp.equals(IP))
				{
					changeAllowedIp = true;
					break;
				}
			}
			if (!changeAllowedIp)
			{
				userProfile.setAllowedIPs(logUsr.getAllowedIPs());
			}
		}

		userProfile.setScienceDomains(newList);
		em.merge(userProfile);
		em.flush();
		sdList = null;
		System.out.println(userProfile.getAllowedIPs());
	}
}
