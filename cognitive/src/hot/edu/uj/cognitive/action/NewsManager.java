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
	public boolean manageEnabled();
	public String getSearchString();
	public void setSearchString(String searchString);
	public String getCriterion();
	public void setCriterion(String criterion);
	public void searchNews();
	public void getListOfNewsForHomePage();
	public String getDate();
	public void setDate(String date);
}
