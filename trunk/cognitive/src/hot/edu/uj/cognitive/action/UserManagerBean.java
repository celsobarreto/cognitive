package edu.uj.cognitive.action;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.web.RequestParameter;

import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.SpecialPage;
import edu.uj.cognitive.model.User;

@Stateful
@Name("userManager")
public class UserManagerBean implements UserManager
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel
	private List<User> userList;
	
	@RequestParameter 
	private Integer userId;
	
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
		this.userList = this.em.createQuery("select u from User u").getResultList();
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
	
	@SuppressWarnings("unchecked")
	public void show()
	{
		
		this.user = (User) this.em.createQuery("select u from User u where u.id = "+userId.toString()).getSingleResult();


	}
}
