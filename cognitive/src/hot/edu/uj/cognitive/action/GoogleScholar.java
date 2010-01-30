package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface GoogleScholar
{
	public void parse();
	public void setStartPage(int startPage);
	public int getStartPage();
	public void setParsePages(int parsePages);
	public int getParsePages();
	public void setKeyword(String keyword);
	public String getKeyword();
	public void destroy();
}
