package com.example.springrestdemo.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.springrestdemo.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String jwtToken = getJwtToken(request);
		String username = "";

		if(!jwtToken.isEmpty()) {
			username = getUserName(jwtToken);
		}else{
			logger.info("No Token found for Header Authorization");
		}

		if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			authenticateUser(userDetails, jwtToken, request);
		}

		chain.doFilter(request, response);
	}

	private void authenticateUser(UserDetails userDetails, String jwtToken, HttpServletRequest request ) {

		if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}

	private String getJwtToken(HttpServletRequest request){
		String jwtToken = request.getHeader("Authorization");
		return jwtToken != null ? jwtToken : "";
	}

	private String getUserName(String jwtToken){
		String username = "";
		try {
			username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException e) {
			logger.warn("Unable to get JWT Token");
		} catch (ExpiredJwtException e) {
			logger.warn("JWT Token has expired");
		}
		return username;
	}
}