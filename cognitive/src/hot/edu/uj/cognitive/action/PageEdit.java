package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface PageEdit
{
	public void showPage(String page);
	public void savePage(String page);
	public void showList();
	public void destroy();
}
