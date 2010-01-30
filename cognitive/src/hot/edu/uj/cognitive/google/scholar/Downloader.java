package edu.uj.cognitive.google.scholar;

public interface Downloader
{
	public String getPage(int n);

	public void shutdown();

}
