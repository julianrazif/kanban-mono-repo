package com.julian.razif.kanban.common.security;

import com.julian.razif.kanban.common.util.ValidationUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom security filter that implements JWT-based authentication.
 * This filter processes requests to validate JWT credentials and maintain security context.
 * It extends OncePerRequestFilter to ensure a single execution per request.
 *
 * @author Julian Razif Figaro
 * @version 1.0
 * @since version 1.0
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * Header name for authorization token
   */
  private static final String AUTHORIZATION_HEADER = "Authorization";

  /**
   * Header name for username
   */
  private static final String USERNAME_HEADER = "Username";

  /**
   * Prefix for Bearer token authentication
   */
  private static final String BEARER_PREFIX = "Bearer ";

  private final JWTUtil jwtUtil;

  public JwtAuthenticationFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Determines whether the JWT authentication filter should be bypassed for specific requests.
   * This method is called before filter execution to check if the request should skip JWT authentication.
   *
   * <p>The following endpoints are excluded from JWT authentication:
   * <ul>
   *   <li>/register - User registration endpoint that doesn't require authentication</li>
   *   <li>/login - User login endpoint that doesn't require authentication</li>
   * </ul>
   *
   * @param request The HTTP servlet request to evaluate
   * @return {@code true} if the filter should not be applied to this request (i.e., for registration or login endpoints),
   * {@code false} otherwise to proceed with JWT authentication
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return "/register".equals(path) || "/login".equals(path);
  }

  /**
   * Main filter method that handles the authentication flow
   *
   * @param request     The HTTP request
   * @param response    The HTTP response
   * @param filterChain The filter chain
   */
  @Override
  protected void doFilterInternal(
    @Nonnull HttpServletRequest request,
    @Nonnull HttpServletResponse response,
    @Nonnull FilterChain filterChain) throws ServletException, IOException {

    JWTCredentialObject credentials = new JWTCredentialObject();

    try {
      // Extracts and sets authenticated JWT credentials from headers
      extractCredsFromRequest(request, credentials);

      if (!(SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthenticationToken)) {
        SecurityContextHolder.clearContext();
        return;
      }

      // Authenticates a user from JWT if context is valid
      if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthenticationToken jwtAuthenticationToken) {
        var creds = ValidationUtils.requireNonNull((JWTCredentialObject) jwtAuthenticationToken.getCredentials(), () -> new BadCredentialsException("Authentication failed: Invalid JWT token"));
        var token = ValidationUtils.requireNonBlank(creds.getToken(), () -> new BadCredentialsException("Authentication failed: Invalid JWT token"));
        var username = ValidationUtils.requireNonBlank(creds.getUsername(), () -> new BadCredentialsException("Authentication failed: Invalid username"));
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String email = claims.getSubject();
        Number idClaim = claims.get("id", Number.class);
        // Authenticates a user principal and sets security context
        if (email != null && idClaim != null) {
          UserPrincipal principal = new UserPrincipal(idClaim.longValue(), email);
          if (!username.equals(principal.getName())) {
            throw new BadCredentialsException("Authentication failed: Invalid username");
          }
          var auth = new JWTAuthenticationToken(principal, token, Collections.emptyList());
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      throw e;
    }
  }

  private static void extractCredsFromRequest(
    @Nonnull HttpServletRequest request,
    @Nonnull JWTCredentialObject credentials) {

    // Extract JWT token and username from the Authorization header
    var authHeader = ValidationUtils.requireNonBlank(request.getHeader(AUTHORIZATION_HEADER), () -> new BadCredentialsException("Authentication failed: No JWT token found in request"));
    var usernameHeader = ValidationUtils.requireNonBlank(request.getHeader(USERNAME_HEADER), () -> new BadCredentialsException("Authentication failed: No username found in request"));

    // Extracts and sets authenticated JWT credentials from headers
    if (authHeader.startsWith(BEARER_PREFIX)) {
      var token = ValidationUtils.requireNonBlank(StringUtils.trimAllWhitespace(authHeader.substring(BEARER_PREFIX.length())), () -> new BadCredentialsException("Authentication failed: Invalid JWT token"));
      var username = ValidationUtils.requireNonBlank(StringUtils.trimAllWhitespace(usernameHeader), () -> new BadCredentialsException("Authentication failed: Invalid username"));
      credentials.setToken(token);
      credentials.setUsername(username);
      var jwtAuthenticationToken = new JWTAuthenticationToken(credentials);
      SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }
  }

}
