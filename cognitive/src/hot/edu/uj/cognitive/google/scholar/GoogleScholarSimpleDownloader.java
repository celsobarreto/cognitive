package edu.uj.cognitive.google.scholar;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class GoogleScholarSimpleDownloader implements Downloader
{
	private String		keyword;
	private HttpClient	httpclient;

	public GoogleScholarSimpleDownloader(String keyword)
	{
		this.httpclient = new DefaultHttpClient();
	}

	public String getPage(int n)
	{
		String responseBody = "error";
		try
		{
			HttpGet httpget = new HttpGet("http://scholar.google.pl/scholar?start=" + n * 10 + "&q=" + this.keyword + "&hl=pl");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = this.httpclient.execute(httpget, responseHandler);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return responseBody;
	}

	public void shutdown()
	{
		this.httpclient.getConnectionManager().shutdown();
	}
}
