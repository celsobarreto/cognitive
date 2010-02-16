package edu.uj.cognitive.model;

public class SearchParameters 
{
	private String searchString;
	private int pageSize = 5;
	private String criterion = "";
	private int page;
	private boolean nextPageAvailable;
	private boolean prevPageAvailable;
	private int rowIndex = 0;

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	public String getSearchString()
	{
		return this.searchString;
	}

	public int getPageSize()
	{
		return this.pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getCriterion()
	{
		return this.criterion;
	}

	public void setCriterion(String criterion)
	{
		this.criterion = criterion;
	}
	
	public int getPage() 
	{
		return this.page;
	}

	public void setPage(int page) 
	{
		this.page = page;
	}
	
	public boolean isNextPageAvailable() 
	{
		return this.nextPageAvailable;
	}
	
	public void setNextPageAvailable(boolean nextPageAvailable) 
	{
		this.nextPageAvailable = nextPageAvailable;
	}

	public boolean isPrevPageAvailable()
	{
		return this.prevPageAvailable;
	}
	
	public void setPrevPageAvailable(boolean prevPageAvailable) 
	{
		this.prevPageAvailable = prevPageAvailable;
	}
	
	public int getRowIndex()
	{
		return ++this.rowIndex + this.page * this.pageSize;
	}

	public void setRowIndex(int rowIndex) 
	{
		this.rowIndex = rowIndex;
	}
}
