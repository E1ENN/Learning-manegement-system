package com.program.user_service.security.jwt;

import com.program.user_service.security.auth.UserDetailsImpl;
import com.program.user_service.security.auth.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @NonNull private UserDetailsServiceImpl userDetailsService;
    @NonNull private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null) {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        } catch (JwtException | IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
