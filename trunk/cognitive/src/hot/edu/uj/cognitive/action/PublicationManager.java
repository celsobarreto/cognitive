package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface PublicationManager 
{
	public void findPublication();
	public void showDetails();
	public void search();
	public void newSearch();
	public void nextPage();
	public void prevPage();
	public boolean isNextPageAvailable();
	public boolean isPrevPageAvailable();
	public boolean isDetails();
	public int getRowIndex();
	public String getSearchString();
	public void setSearchString(String searchString);
	public String getSearchPattern();
	public int getPageSize();
	public void setPageSize(int pageSize);
	public String getCriterion();
	public void setCriterion(String criterion);
}
