package edu.uj.cognitive.google.scholar;

import java.util.ArrayList;
import java.util.HashMap;

public interface Parser
{
	public boolean parsePage(String page);
	public ArrayList<String> getKeywordsForTitle(String title);
	public boolean addNewPublication(String title, String link, String author, String keywords);
	public boolean isTitleUnique(String title);
	public boolean isKeywordUnique(String keywords);
	public boolean areKeywordsUnique(ArrayList<String> keywords);
	public HashMap<String, ArrayList<String>> getData();
}
