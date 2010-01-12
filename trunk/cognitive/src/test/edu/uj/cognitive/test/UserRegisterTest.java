package edu.uj.cognitive.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class UserRegisterTest extends SeamTest {

	@Test
	public void test_userRegister() throws Exception {
		new FacesRequest("/userRegister.xhtml") {
			@Override
			protected void updateModelValues() throws Exception {				
				//set form input to model attributes
				setValue("#{UserRegister.value}", "seam");
			}
			@Override
			protected void invokeApplication() {
				//call action methods here
				invokeMethod("#{UserRegister.userRegister}");
			}
			@Override
			protected void renderResponse() {
				//check model attributes if needed
				assert getValue("#{UserRegister.value}").equals("seam");
			}
		}.run();
	}
}
