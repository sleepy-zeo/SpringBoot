/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security采用的是filterChain的设计模式，主要的功能都是由过滤器实现，过滤器的核心方法是doFilter
 *
 * Spring Security默认的过滤器:
 * 		org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@57cb70be,
 * 		org.springframework.security.web.context.SecurityContextPersistenceFilter@507d64aa,
 * 		org.springframework.security.web.header.HeaderWriterFilter@80bfa9d,
 * 		org.springframework.security.web.authentication.logout.LogoutFilter@33ecbd6c,
 * 		org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@2abe9173,
 * 		org.springframework.security.web.savedrequest.RequestCacheAwareFilter@60b34931,
 * 		org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@3e67f5f2,
 * 		org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter@43f9dd56,
 * 		org.springframework.security.web.authentication.AnonymousAuthenticationFilter@2d4608a6,
 * 		org.springframework.security.web.session.SessionManagementFilter@4b039c6d,
 * 		org.springframework.security.web.access.ExceptionTranslationFilter@53abfc07,
 * 		org.springframework.security.web.access.intercept.FilterSecurityInterceptor@32177fa5
 *
 * 比较重要的是UsernamePasswordAuthenticationFilter，该过滤器专门用来处理username+password登陆相关
 */
public class UsernamePasswordAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {
	// ~ Static fields/initializers
	// =====================================================================================

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	private boolean postOnly = true;

	// ~ Constructors
	// ===================================================================================================

	public UsernamePasswordAuthenticationFilter() {
		super(new AntPathRequestMatcher("/login", "POST"));
	}

	// ~ Methods
	// ========================================================================================================

	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		// Step 3. 根据username和password构造authToken
		// authToken.principal = username
		// authToken.credential = password
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				username, password);

		// Step 4. 将request整合成details后，authToken.details = details
		// 具体步骤是:
		//		1. AuthenticationDetailsSource调用WebAuthenticationDetails的构造函数构造出details
		//		2. AuthenticationDetailsSource提供buildDetails返回该details
		//		3. setDetails将details赋值给authToken的details属性
		setDetails(request, authToken);

		// Step 5. 通过AuthenticationManager来认证authenToken
		// AuthenticationManager是一个接口，具体的实现类是ProviderManager
		return this.getAuthenticationManager().authenticate(authToken);
	}

	/**
	 * Enables subclasses to override the composition of the password, such as by
	 * including additional values and a separator.
	 * <p>
	 * This might be used for example if a postcode/zipcode was required in addition to
	 * the password. A delimiter such as a pipe (|) should be used to separate the
	 * password and extended value(s). The <code>AuthenticationDao</code> will need to
	 * generate the expected password in a corresponding manner.
	 * </p>
	 *
	 * @param request so that request attributes can be retrieved
	 *
	 * @return the password that will be presented in the <code>Authentication</code>
	 * request token to the <code>AuthenticationManager</code>
	 */
	@Nullable
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	/**
	 * Enables subclasses to override the composition of the username, such as by
	 * including additional values and a separator.
	 *
	 * @param request so that request attributes can be retrieved
	 *
	 * @return the username that will be presented in the <code>Authentication</code>
	 * request token to the <code>AuthenticationManager</code>
	 */
	@Nullable
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	/**
	 * Provided so that subclasses may configure what is put into the authentication
	 * request's details property.
	 *
	 * @param request that an authentication request is being created for
	 * @param authRequest the authentication request object that should have its details
	 * set
	 */
	protected void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from the login
	 * request.
	 *
	 * @param usernameParameter the parameter name. Defaults to "username".
	 */
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	/**
	 * Sets the parameter name which will be used to obtain the password from the login
	 * request..
	 *
	 * @param passwordParameter the parameter name. Defaults to "password".
	 */
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter. If set to
	 * true, and an authentication request is received which is not a POST request, an
	 * exception will be raised immediately and authentication will not be attempted. The
	 * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
	 * authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
