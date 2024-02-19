package com.daniel.security_jwt.config;

import lombok.NonNull;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.daniel.security_jwt.service.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        final Optional<String> possibleAuthentication = Optional.ofNullable(request.getHeader("Authorization"));
        if(possibleAuthentication.isEmpty() || !possibleAuthentication.get().startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authentication = possibleAuthentication.get();

        final String jwt = authentication.substring(7);
        final Optional<String> userEmail = Optional.of(jwtService.extractUsername(jwt));

        userEmail.ifPresent(email -> {
            if(SecurityContextHolder.getContext().getAuthentication() == null) {
               final UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

               if(this.jwtService.isTokenValid(jwt, userDetails)) {
                   final UsernamePasswordAuthenticationToken authToken =
                           new UsernamePasswordAuthenticationToken(
                                   userDetails,
                                   null,
                                   userDetails.getAuthorities()
                           );

                   authToken.setDetails(
                           new WebAuthenticationDetailsSource().buildDetails(request)
                   );

                   SecurityContextHolder.getContext().setAuthentication(authToken);
               }
            }
        });

        filterChain.doFilter(request, response);
    }
}