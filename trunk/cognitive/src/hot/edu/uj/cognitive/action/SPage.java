package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface SPage
{
	public void toPage(String page);
	public String getPageContent(String page);
	public void destroy();
}
