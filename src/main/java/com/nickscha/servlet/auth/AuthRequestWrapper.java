/*
 * Copyright (C) 2017 nickscha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nickscha.servlet.auth;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AuthRequestWrapper extends HttpServletRequestWrapper {

	private final HttpServletRequest realRequest;

	public AuthRequestWrapper(HttpServletRequest request) {
		super(request);
		this.realRequest = request;
	}

	@Override
	public void login(String username, String password) throws ServletException {

		// Custom authentication here
		if (username.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
			realRequest.getSession().setAttribute("user", "F");
		}
	}

	@Override
	public void logout() throws ServletException {
		super.logout();
	}

	@Override
	public String getRemoteUser() {
		if (true) {
			return realRequest.getRemoteUser();
		}
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		if (true) {
			return realRequest.getUserPrincipal();
		}
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (true) {
			return realRequest.isUserInRole(role);
		}
		return false;
	}

}
