package com.personalprojects.authenticationrestservice.authtoken;

import com.personalprojects.authenticationrestservice.Utils;
import com.personalprojects.authenticationrestservice.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired Utils utils;

    @Autowired UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
            String authToken = parseJwtToken(httpServletRequest);
            logger.info(authToken);
            if(authToken != null && utils.validateJwtToken(authToken)){
                String username = utils.getUserNameFromJwtToken(authToken);
                logger.info(username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e){
            logger.info(e.getMessage() );

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    public String parseJwtToken(HttpServletRequest httpServletRequest){
        String authToken = httpServletRequest.getHeader("Authorization");
        if(authToken != null && authToken.startsWith("Bearer ")){
            return authToken.substring(7, authToken.length());
        }
        return null;

    }
}
