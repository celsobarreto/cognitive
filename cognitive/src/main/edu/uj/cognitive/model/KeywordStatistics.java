package edu.uj.cognitive.model;

public class KeywordStatistics implements Comparable<KeywordStatistics> {

	
	private String name;
	private long count;
	private String linkedKeywords;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getLinkedKeywords() {
		return linkedKeywords;
	}
	public void setLinkedKeywords(String linkedKeywords) {
		this.linkedKeywords = linkedKeywords;
	}
	
	
	@Override
	public int compareTo(KeywordStatistics o) {
		
		if(this.getCount() == o.getCount())
		{
			return 0;
		}
		else if( this.getCount() > o.getCount() )
		{
			return -1;
		}
		else
		{
			return 1;
		}
		
	}
	
	
	
}
