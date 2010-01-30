package edu.uj.cognitive.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class KeywordManagerTest extends SeamTest {

	@Test
	public void test_keywordManager() throws Exception {
		new FacesRequest("/keywordManager.xhtml") {
			@Override
			protected void invokeApplication() {
				//call action methods here
				invokeMethod("#{KeywordManager.keywordManager}");
			}
		}.run();
	}
}
