package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface NewsManager 
{
	public void destroy();
	public void getNews();
}
