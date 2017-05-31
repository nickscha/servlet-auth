package com.nickscha.servlet.auth.login.modules;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

public class MyLoginModule {

	public static MyCustomPrincipal login(final String username, final String password) throws LoginException {
		Logger.getLogger("MyLoginModule").info(() -> "Calling login module by user: " + username);
		if (username.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
			return new MyCustomPrincipal(username, Arrays.asList("manager", "auditor"));
		}
		throw new LoginException("You're not test=test user !");
	}

}
