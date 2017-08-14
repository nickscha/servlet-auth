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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*", 
		   filterName  = "AuthRequestFilter")
public final class AuthRequestFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		String loginURI = req.getContextPath() + "/login";

		boolean loggedIn = session != null && session.getAttribute("user") != null;
		boolean loginRequest = req.getRequestURI().equals(loginURI);

		if (loggedIn || loginRequest) {
			chain.doFilter(new AuthRequestWrapper((HttpServletRequest) request), response);
		} else {
			res.sendRedirect(loginURI);
		}
	}

	@Override
	public void destroy() {}

}
