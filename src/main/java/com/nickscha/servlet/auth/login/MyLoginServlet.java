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
import java.util.Arrays;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public final class MyLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 3329450219328582888L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/login.html").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("user");
		}
		
		// Do your crazy complex login procedure here
		if (username.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
			request.getSession().setAttribute("user", new MyCustomPrincipal(username, Arrays.asList("manager", "admin")));
			System.out.println("Principal is: " + request.getSession(false).getAttribute("user"));
		} else {
			// TODO what should happen when login fails
			try {
				throw new LoginException("You're not test=test user !");
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}

		response.sendRedirect(request.getContextPath() + "/");
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.logout();
	}

}
