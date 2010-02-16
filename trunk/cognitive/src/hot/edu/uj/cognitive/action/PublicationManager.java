package edu.uj.cognitive.action;

import javax.ejb.Local;

import edu.uj.cognitive.model.SearchParameters;

@Local
public interface PublicationManager 
{
	public void findPublication();
	public void showDetails();
	public void search();
	public void newSearch();
	public void nextPage();
	public void prevPage();
	public boolean isDetails();
	public void setSearchParam(SearchParameters searchParam);
	public SearchParameters getSearchParam();
}
