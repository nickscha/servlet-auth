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

/**
 * The {@link AuthRequestFilter} redirects the user to the login page one he
 * accesses a restricted resource. If the user is loggin in the
 * {@link AuthRequestWrapper} is added to the filter chain.
 * 
 * @author nickscha
 * @see AuthRequestWrapper
 *
 */
@WebFilter(urlPatterns = "/*", filterName = "AuthRequestFilter")
public final class AuthRequestFilter implements Filter {

    /**
     * JSF resource identifier
     */
    private static final String RESOURCE_IDENTIFIER = "/javax.faces.resource";

    /**
     * Instructs JSF AJAX to send an redirect
     */
    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"%s\"></redirect></partial-response>";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);
        boolean resourceRequest = req.getRequestURI().startsWith(req.getContextPath() + RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(req.getHeader("Faces-Request"));

        if (loggedIn || loginRequest || resourceRequest) {
            if (!resourceRequest) {
                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                res.setHeader("Pragma", "no-cache");
                res.setDateHeader("Expires", 0);
            }
            chain.doFilter(new AuthRequestWrapper((HttpServletRequest) request), response);
        } else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, loginURI);
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }

}
