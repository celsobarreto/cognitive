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
import edu.uj.cognitive.model.Publication;
import edu.uj.cognitive.model.SpecialPage;
import edu.uj.cognitive.model.User;

@Stateful
@Name("userProfil")
public class UserProfilAction implements UserProfil
{
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	@DataModel
	private List<User> userList;
	
	@DataModelSelection(value = "userList")
	@Out(required = false)
	private User user;
	
	@SuppressWarnings("unchecked")
	@Factory("userList")
	public void findUser()
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
	}
	
	public void showPublication()
	{
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void showUser()
	{
		this.userList = this.em.createQuery("select u from User u where u.id = #{publUser.id}").getResultList();
		if (this.userList.size() > 0)
		{
			this.user = this.userList.get(0);
		}
		else
		{
			this.user = null;
		}
	}
}
