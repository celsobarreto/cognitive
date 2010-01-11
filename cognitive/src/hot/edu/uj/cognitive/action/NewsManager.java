package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface NewsManager 
{
	public void destroy();
	public void getNews();
	public void addNews();
	public void createNews();
}
