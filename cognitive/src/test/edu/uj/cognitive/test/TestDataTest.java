package edu.uj.cognitive.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class TestDataTest extends SeamTest {

	@Test
	public void test_testData() throws Exception {
		new FacesRequest("/testData.xhtml") {
			@Override
			protected void invokeApplication() {
				//call action methods here
				invokeMethod("#{TestData.testData}");
			}
		}.run();
	}
}
