package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface UserManager
{
	public void list();
	public void delete();
	public void destroy();
	public void show();
	
	public void setUserId(Integer id);
	public Integer getUserId();
}
