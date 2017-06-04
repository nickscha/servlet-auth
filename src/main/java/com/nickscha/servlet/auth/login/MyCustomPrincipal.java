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

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyCustomPrincipal implements Principal, Serializable {

	private static final long serialVersionUID = 6429527622593486174L;
	private final String name;
	private final List<String> roles;

	public MyCustomPrincipal(String name, List<String> roles) {
		this.name = name;
		this.roles = Optional.ofNullable(roles).orElseGet(ArrayList::new);
	}

	public boolean isUserInRole(final String role) {
		return roles.stream().anyMatch(e -> e.equalsIgnoreCase(role));
	}

	@Override
	public String getName() {
		return name;
	}

	public List<String> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "MyCustomPrincipal [name=" + name + ", roles=" + roles + "]";
	}

}
