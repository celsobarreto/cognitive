package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface UserProfil
{
	public void createTestData();
	public void findUser();
	public void delete();
	public void destroy();
	public void showPublication();
	public void showUser();
}
