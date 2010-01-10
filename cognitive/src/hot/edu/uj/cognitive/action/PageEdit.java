package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface PageEdit
{
	public void showPage();
	public void savePage();
	public void showList();
	public void destroy();
	public String getText();
	public void setText(String text);
}
