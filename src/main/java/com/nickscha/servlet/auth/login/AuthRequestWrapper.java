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
package com.nickscha.servlet.auth.login;

import java.security.Principal;
import java.util.Optional;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.nickscha.servlet.auth.login.modules.MyCustomPrincipal;
import com.nickscha.servlet.auth.login.modules.MyLoginModule;

public class AuthRequestWrapper extends HttpServletRequestWrapper {

	private final HttpServletRequest realRequest;
	private final MyCustomPrincipal principal;

	public AuthRequestWrapper(HttpServletRequest request) {
		super(request);
		this.realRequest = request;
		this.principal = Optional.ofNullable(request.getSession(false)).filter(e -> e.getAttribute("user") != null)
				.map(session -> (MyCustomPrincipal) session.getAttribute("user"))
				.orElse(new MyCustomPrincipal("guest", null));
	}

	@Override
	public void login(String username, String password) throws ServletException {

		try {
			HttpSession session = this.realRequest.getSession(false);
			if (session != null) {
				session.removeAttribute("user");
			}

			// Custom authentication here
			MyCustomPrincipal principal = MyLoginModule.login(username, password);
			System.out.println("User: " + principal);
			this.realRequest.getSession().setAttribute("user", principal);
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logout() throws ServletException {
		// Remove user from user service ?
		super.logout();
	}

	@Override
	public String getRemoteUser() {
		if (principal == null) {
			return realRequest.getRemoteUser();
		}
		return principal.getName();
	}

	@Override
	public Principal getUserPrincipal() {
		if (principal == null) {
			return realRequest.getUserPrincipal();
		}
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (principal == null) {
			return realRequest.isUserInRole(role);
		}
		return principal.isUserInRole(role);
	}

}
