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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class AuthRequestWrapper extends HttpServletRequestWrapper {

	private final HttpServletRequest realRequest;
	private final MyCustomPrincipal principal;

	public AuthRequestWrapper(HttpServletRequest request) {
		super(request);
		this.realRequest = request;
		
		// Assign your custom principal here to make it available
		// when using #{request.userPrincipal}, ...
		this.principal = Optional.ofNullable(request.getSession(false))
                                 .filter(e -> e.getAttribute("user") != null)
                                 .map(session -> (MyCustomPrincipal) session.getAttribute("user"))
                                 .orElse(new MyCustomPrincipal("guest", null));
	}

	@Override
	public String getRemoteUser() {
		return Optional.ofNullable(principal).map(e -> e.getName()).orElseGet(realRequest::getRemoteUser);
	}

	@Override
	public Principal getUserPrincipal() {
		return Optional.ofNullable(principal).map(e -> ((Principal) e)).orElseGet(realRequest::getUserPrincipal);
	}

	@Override
	public boolean isUserInRole(String role) {
		return Optional.ofNullable(principal).map(e -> e.isUserInRole(role)).orElse(realRequest.isUserInRole(role));
	}

}
