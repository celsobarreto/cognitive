package edu.uj.cognitive.google.scholar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleScholarResultParser implements Parser
{
	private HashMap<String, ArrayList<String>>	data	= new HashMap<String, ArrayList<String>>();
	private final String 						pattern = "<div class=gs_r><h3><a href=\"(.*?)\">(.*?)</a></h3>.*?<span class=gs_a>(.*?)-.*?</span>.*?</div>";

	public boolean parsePage(String page)
	{
		String tmpTitle, tmpAuthor = null;
		Pattern pattern = Pattern.compile(this.pattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(page);

		while (matcher.find())
		{
			tmpTitle = matcher.group(2).replaceAll("\\<.*?\\>|&.*?;|and|the|, ", "");
			tmpAuthor = matcher.group(3).replaceAll("(,.*?)?\\<b\\>Null\\</b\\>|&.*?;|\\s,\\s?", "");
			this.addNewPublication(tmpTitle, matcher.group(1), tmpAuthor, tmpTitle);
		}
		return true;
	}

	public ArrayList<String> getKeywordsForTitle(String title)
	{
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\s");
		String[] items = p.split(title);
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].length() > 2)
			{
				list.add(items[i]);
			}
		}
		return list;
	}

	public boolean addNewPublication(String title, String link, String author, String keywords)
	{
		ArrayList<String> info = new ArrayList<String>();
		info.add(author);
		info.add(link);
		info.add(keywords);
		this.data.put(title, info);
		return true;
	}

	public boolean isTitleUnique(String title)
	{
		return !this.data.containsKey(title);
	}

	public boolean isKeywordUnique(String keyword)
	{
		Collection<ArrayList<String>> c = this.data.values();
		Iterator<ArrayList<String>> itr = c.iterator();

		while (itr.hasNext())
		{
			if (itr.next().contains(keyword))
				return false;
		}
		return true;
	}

	public boolean areKeywordsUnique(ArrayList<String> keywords)
	{
		Iterator<String> itr = keywords.iterator();
		while (itr.hasNext())
		{
			if (!this.isKeywordUnique(itr.next()))
				return false;
		}
		return true;
	}
	
	public HashMap<String, ArrayList<String>> getData()
	{
		return this.data;
	}
}
