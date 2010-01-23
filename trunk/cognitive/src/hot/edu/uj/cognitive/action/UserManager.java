package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface UserManager
{
	public void list();
	public void delete();
	public void destroy();
	public void show();

	public void edit();
	public void scienceDomainList();

	public void setMode(String m);
	public String getMode();
	public void setSearchText(String text);
	public String getSearchText();
	public void setSearchCriteria(String criteria);
	public String getSearchCriteria();

	public void setUserId(Integer id);
	public Integer getUserId();
}
