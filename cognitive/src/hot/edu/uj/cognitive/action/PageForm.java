package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface PageForm {
	public String getText();
	public void setText(String t);
	public String getTitle();
	public void setTitle(String t);
	public String getId();
	public void setId(String t);
	public void destroy();
	public void sendForm();
	public void showForm();
}
