package edu.uj.cognitive.action;

import javax.ejb.Local;

import edu.uj.cognitive.model.News;

@Local
public interface NewsManager 
{
	public void destroy();
	public void getListOfNews();
	public String addNewsAction();
	public String editNewsAction();
	public String deleteNewsAction();
	public String performNewsAction();
	public News getNews();
	public void setNews(News news);
}