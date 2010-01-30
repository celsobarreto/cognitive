package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface KeywordManager
{
    // seam-gen method
    public void keywordManager();

    // add additional interface methods here

    public void list();
    public boolean isAllKeywords();
    public boolean isStatisticKeywords();
    
    
    public void addKeyword();
    public void setSearchKeywords(String searchKeywords);
	public String getSearchKeywords();
	
	public void search();
	public void destroy();
    
    
}
